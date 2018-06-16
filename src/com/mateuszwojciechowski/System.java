package com.mateuszwojciechowski;

import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Class representing a system with its specification.
 */
public class System {
    /**
     * Inflow intensity.
     */
    private static double lambda = 0;
    /**
     * Lambda getter.
     * @return lambda parameter
     */
    public static double getLambda() {
        return lambda;
    }
    /**
     * Lambda setter.
     * @param lambda lambda parameter
     */
    public static void setLambda(double lambda) {
        System.lambda = lambda;
    }

    /**
     * Service intensity.
     */
    private static double mu = 0;
    /**
     * Mu getter.
     * @return mu parameter
     */
    public static double getMu() {
        return mu;
    }
    /**
     * Mu setter
     * @param mu mu parameter
     */
    public static void setMu(double mu) {
        System.mu = mu;
    }

    /**
     * System usage rate.
     */
    private static double rho = 0;
    /**
     * Rho getter.
     * @return rho parameter
     */
    public static double getRho() {
        return rho;
    }

    /**
     * True if system is busy (is handling an event).
     */
    public static boolean busy = false;

    private static int serversNum = 0;
    public static void setServersNum(int serversNum) {
        System.serversNum = serversNum;
    }
    public static int getServersNum() {
        return serversNum;
    }

    /**
     * ID of server which was used to handle the previous request
     */
    private static int lastServerUsed = 0;

    public static int getLastServerUsed() {
        return lastServerUsed;
    }

    public static int getNextServerID() {
        if(lastServerUsed == serversNum - 1)
            return 0;
        else
            return lastServerUsed + 1;
    }
    public static void setLastServerUsed(int lastServerUsed) {
        System.lastServerUsed = lastServerUsed;
    }

    private static ArrayList<Server> serversList = new ArrayList<>();

    public static Server getServer(int id) {
        return serversList.get(id);
    }
    /**
     * Sequence of the events in the system.
     */
    private static LinkedList<Event> eventList = new LinkedList<>();

    /**
     * Function returns number of planned events left
     * @return number of planned events left
     */
    public static int getNumberOfEvents() {
        return eventList.size();
    }

    /**
     * Function which puts a new event to the list of events
     * @param newEvent new event on the time-line
     */
    public static void addEvent(Event newEvent) {
        boolean success = false;
        //Go through list, find an element which is "bigger" and insert new event in that place.
        for(Event e : eventList) {
            if(e.compareTo(newEvent) > 0) {
                eventList.add(eventList.indexOf(e), newEvent);
                success = true;
                break;
            }
        }
        //If new event is the biggest, insert it in the end of the list.
        if(!success)
            eventList.add(newEvent);
    }

    /**
     * Function to get the next event from the list.
     * @return next event
     */
    public static Event getNextEvent() {
        if(!eventList.isEmpty()) {
            Event event = eventList.getFirst();
            eventList.removeFirst();
            return event;
        }
        else return null;
    }

    public static Instant getNextEventStartTime() {
        if(!eventList.isEmpty()) {
            Event event = eventList.getFirst();
            Instant time = event.getEventStartTime();
            return time;
        } else return null;
    }

    /**
     * Queue size.
     */
    private static int eventsInSystem = 0;

    public static int getNumberOfEventsInSystem() {
        return eventsInSystem;
    }
    public static void increaseNumberOfEventsInSystem() {
        eventsInSystem++;
    }
    public static void decreaseNumberOfEventsInSystem() {
        if(eventsInSystem > 0)
            eventsInSystem--;
    }

    /**
     * This field keeps the time when the previous event occured to calculate the duration between two events.
     */
    private static Instant previousEventTime = Instant.EPOCH;

    /**
     * Previous event time setter.
     * @param newValue a new value of the field
     */
    public static void setPreviousEventTime(Instant newValue) {
        previousEventTime = newValue;
    }

    /**
     * Previous event time getter.
     * @return previous event time
     */
    public static Instant getPreviousEventTime() {
        return previousEventTime;
    }

    /**
     * Constructor with lambda and mu parameters
     * @param lambda inflow intensity
     * @param mu service intensity
     */
    public System(double lambda, double mu, int serversNum) {
        setLambda(lambda);
        setMu(mu);
        rho = lambda/mu;
        setServersNum(serversNum);

        for(int i=0; i < serversNum; i++) {
            serversList.add(new Server(i));
        }

        addEvent(new Event(Instant.ofEpochMilli(RandomGenerator.getNextExpDist(lambda)), Event.EventType.ARRIVAL));
    }
}
