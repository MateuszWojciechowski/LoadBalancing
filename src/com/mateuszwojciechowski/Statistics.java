package com.mateuszwojciechowski;

public class Statistics {
    private static int numberOfArrivals = 0;
    private static int numberOfDepartures = 0;
    private static double averageNumberInSystem = 0;
    private static double simulationTime = 0;
    private static double averageTimeToService = 0;
    private static double averageEventTime = 0;
    private static double averageNumberOfWaitingTasks = 0;
    private static int numberOfRejectedArrivals = 0;

    public static void addToAverageNumberInSystem(long duration, int state) {
        averageNumberInSystem += duration * state;
        simulationTime += duration;
        if(state > 1) {
            averageNumberOfWaitingTasks += duration * (state-1);
        }
    }
    public static double getAverageNumberInSystem() {
        return averageNumberInSystem/simulationTime;
    }
    public static double getAverageNumberOfWaitingTasks() {
        return averageNumberOfWaitingTasks/simulationTime;
    }
    public static double getAverageServiceTime() {
        return (averageEventTime+averageTimeToService)/numberOfArrivals;
    }

    public static void increaseNumberOfArrivals() {
        numberOfArrivals++;
    }

    public static void increaseNumberOfDepartures() {
        numberOfDepartures++;
    }

    public static int getNumberOfEvents() {
        return numberOfArrivals + numberOfDepartures;
    }

    public static int getNumberOfArrivals() { return numberOfArrivals; }
    public static int getNumberOfRejectedArrivals() { return numberOfRejectedArrivals; }
    public static double getPercentageOfRejectedArrivals() {
        double result = ((double)numberOfRejectedArrivals)/((double)numberOfArrivals);
        return result*100;
    }

    public static void addToAverageTimeToService(long duration) {
        averageTimeToService += duration;
    }
    public static double getAverageTimeToService() {
        return averageTimeToService/numberOfArrivals;
    }

    public static void addToAverageEventTime(long duration) {
        averageEventTime += duration;
    }
    public static double getAverageEventTime() {
        return averageEventTime/numberOfArrivals;
    }

    public static void increaseNumberOfRejectedArrivals() { numberOfRejectedArrivals++; }

    public static double getSimulationTime() { return simulationTime; }
}
