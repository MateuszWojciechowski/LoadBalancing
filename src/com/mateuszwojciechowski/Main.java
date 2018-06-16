package com.mateuszwojciechowski;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.Instant;
import java.util.Random;

public class Main {
    public static System system;

    public static void main(String[] args) {
        // write your code here
        BufferedReader br = new BufferedReader(new InputStreamReader(java.lang.System.in));

        java.lang.System.out.println("------------------------");
        java.lang.System.out.println(" Load Balancing Simulator ");
        java.lang.System.out.println("------------------------");

        java.lang.System.out.println("Choose lambda parameter: ");
        double lambda = 0;
        try {
            lambda = Double.valueOf(br.readLine());
        } catch (IOException e) {
            java.lang.System.out.println(e.getMessage());
        }

        java.lang.System.out.println("Choose mu parameter: ");
        double mu = 0;
        try {
            mu = Double.valueOf(br.readLine());
        } catch (IOException e) {
            java.lang.System.out.println(e.getMessage());
        }

        java.lang.System.out.println("Choose number of servers: ");
        int servers = 0;
        try {
            servers = Integer.valueOf(br.readLine());
        } catch (IOException e) {
            java.lang.System.out.println(e.getMessage());
        }

        java.lang.System.out.println("Choose c parameter (single server capacity): ");
        int c = 0;
        try {
            c = Integer.valueOf(br.readLine());
        } catch (IOException e) {
            java.lang.System.out.println(e.getMessage());
        }

        java.lang.System.out.println("Choose random generator seed: ");
        int seed = 0;
        try {
            seed = Integer.valueOf(br.readLine());
        } catch (IOException e) {
            java.lang.System.out.println(e.getMessage());
        }

        RandomGenerator.setSeed(seed);
        system = new System(lambda, mu, servers);
        Server.setCapacity(c);

        //Simulation loop
        while (Statistics.getNumberOfEvents() < 20000) {
            /*
            1. Process the current event.
            2. Generate new events.
             */
            Event currentEvent = System.getNextEvent();

            java.lang.System.out.println("-------------------------------");
            java.lang.System.out.println("Current event: " + currentEvent.getEventType());
            java.lang.System.out.println("Current time: " + currentEvent.getEventStartTime());

            currentEvent.process();

            //generate next ARRIVAL event
            if (currentEvent.getEventType() == Event.EventType.ARRIVAL) {
                long nextArrivalTime = RandomGenerator.getNextExpDist(System.getLambda());
                System.addEvent(new Event(Instant.ofEpochMilli(currentEvent.getEventStartTime().toEpochMilli() + nextArrivalTime), Event.EventType.ARRIVAL));
            }


            System.setPreviousEventTime(currentEvent.getEventStartTime());
            java.lang.System.out.println("Average number of events in system: " + Statistics.getAverageNumberInSystem());
            java.lang.System.out.println("Average number of waiting tasks in system: " + Statistics.getAverageNumberOfWaitingTasks());
            java.lang.System.out.println("Average service time: " + Statistics.getAverageServiceTime());
            java.lang.System.out.println("Average time to service: " + Statistics.getAverageTimeToService());
            java.lang.System.out.println("Average event time: " + Statistics.getAverageEventTime());
            java.lang.System.out.println("Number of arrivals: " + Statistics.getNumberOfArrivals());
            java.lang.System.out.println("Number of rejected arrivals: " + Statistics.getNumberOfRejectedArrivals());
            java.lang.System.out.println("% rejected arrivals: " + Statistics.getPercentageOfRejectedArrivals());
            java.lang.System.out.println("Handled events: " + Statistics.getNumberOfEvents());
            printServersStats();

        }
    }

    private static void printServersStats() {
        for(int i=0; i < System.getServersNum(); i++) {
            java.lang.System.out.println("Average server #" + i + " load: " + System.getServer(i).getAverageLoad()*100/Server.getCapacity() + "%");
            java.lang.System.out.println("Server #" + i + " handled requests: " + System.getServer(i).getHandledRequests());

        }
    }
}
