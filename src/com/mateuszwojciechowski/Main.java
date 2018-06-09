package com.mateuszwojciechowski;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.Instant;

public class Main {
    public static System system;

    public static void main(String[] args) {
        // write your code here
        BufferedReader br = new BufferedReader(new InputStreamReader(java.lang.System.in));

        java.lang.System.out.println("------------------------");
        java.lang.System.out.println(" M/M/1/c System Simulator ");
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

        java.lang.System.out.println("Choose c parameter (buffer capacity): ");
        int c = 0;
        try {
            c = Integer.valueOf(br.readLine());
        } catch (IOException e) {
            java.lang.System.out.println(e.getMessage());
        }

        system = new System(lambda, mu, c);

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

        }

        java.lang.System.out.println("----------------------");
        java.lang.System.out.println(" Theoretical results: ");
        java.lang.System.out.println("----------------------");
        java.lang.System.out.println("Average number of events in system: " + System.getRho() / (1 - System.getRho()));
        java.lang.System.out.println("Average number of waiting tasks in system: " + Math.pow(System.getRho(), 2) / (1 - System.getRho()));
        java.lang.System.out.println("Average service time: " + 1000 / (System.getMu() - System.getLambda()));
        java.lang.System.out.println("Average time to service: " + System.getRho() * 1000 / (System.getMu() - System.getLambda()));
        java.lang.System.out.println("Average event time: " + Statistics.getAverageEventTime());
    }
}
