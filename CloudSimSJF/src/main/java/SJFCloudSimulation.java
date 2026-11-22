import org.cloudsimplus.brokers.DatacenterBrokerSimple;
import org.cloudsimplus.builders.tables.CloudletsTableBuilder;
import org.cloudsimplus.cloudlets.Cloudlet;
import org.cloudsimplus.cloudlets.CloudletSimple;
import org.cloudsimplus.core.CloudSimPlus;
import org.cloudsimplus.datacenters.DatacenterSimple;
import org.cloudsimplus.hosts.HostSimple;
import org.cloudsimplus.resources.PeSimple;
import org.cloudsimplus.utilizationmodels.UtilizationModelDynamic;
import org.cloudsimplus.vms.VmSimple;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class SJFCloudSimulation {

    public static void main(String[] args) {

        // Step 1: Initialize CloudSim
        CloudSimPlus simulation = new CloudSimPlus();

        // Step 2: Create a Broker
        DatacenterBrokerSimple broker =
                new DatacenterBrokerSimple(simulation);

        // Step 3: Create Host with 4 CPU cores
        HostSimple host = new HostSimple(
                8192,       // RAM in MB
                10000,      // Bandwidth
                100000,     // Storage
                List.of(
                        new PeSimple(2000),
                        new PeSimple(2000),
                        new PeSimple(2000),
                        new PeSimple(2000)
                )
        );

        // Step 4: Create Datacenter
        DatacenterSimple datacenter =
                new DatacenterSimple(simulation, List.of(host));

        // Step 5: Create two Virtual Machines
        VmSimple vm1 = new VmSimple(1000, 2);
        vm1.setRam(2048)
                .setBw(1000)
                .setSize(10000);

        VmSimple vm2 = new VmSimple(1000, 2);
        vm2.setRam(2048)
                .setBw(1000)
                .setSize(10000);

        List<VmSimple> vmList = new ArrayList<>();
        vmList.add(vm1);
        vmList.add(vm2);

        broker.submitVmList(vmList);

        // Step 6: Create Cloudlets
        UtilizationModelDynamic utilization =
                new UtilizationModelDynamic(1.0);

        CloudletSimple c1 = new CloudletSimple(10000, 1, utilization);
        CloudletSimple c2 = new CloudletSimple(5000, 1, utilization);
        CloudletSimple c3 = new CloudletSimple(20000, 1, utilization);
        CloudletSimple c4 = new CloudletSimple(3000, 1, utilization);
        CloudletSimple c5 = new CloudletSimple(8000, 1, utilization);

        List<Cloudlet> cloudletList = new ArrayList<>();

        cloudletList.add(c1);
        cloudletList.add(c2);
        cloudletList.add(c3);
        cloudletList.add(c4);
        cloudletList.add(c5);

        // Step 7: Display Cloudlets before scheduling
        System.out.println("\nCloudlets Before SJF Scheduling:");
        System.out.println("--------------------------------");

        for (Cloudlet cloudlet : cloudletList) {
            System.out.println(
                    "Cloudlet ID: " + cloudlet.getId() +
                            " | Length: " + cloudlet.getLength() + " MI"
            );
        }

        // Step 8: Apply Shortest Job First Algorithm
        cloudletList.sort(
                Comparator.comparingLong(Cloudlet::getLength)
        );

        System.out.println("\nCloudlets After SJF Scheduling:");
        System.out.println("--------------------------------");

        for (Cloudlet cloudlet : cloudletList) {
            System.out.println(
                    "Cloudlet ID: " + cloudlet.getId() +
                            " | Length: " + cloudlet.getLength() + " MI"
            );
        }

        // Step 9: Submit sorted Cloudlets
        broker.submitCloudletList(cloudletList);

        // Step 10: Start Simulation
        simulation.start();

        // Step 11: Display Simulation Results
        System.out.println("\nSimulation Results:");
        System.out.println("-------------------");

        new CloudletsTableBuilder(
                broker.getCloudletFinishedList()
        ).build();
    }
}