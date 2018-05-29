package com.mateuszwojciechowski;

import java.time.Instant;
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
     * Buffer capacity.
     */
    private static int c = 0;
    /**
     * Buffer capacity getter.
     * @return bufferState capacity
     */
    public static int getC() {
        return c;
    }
    /**
     * Buffer capacity setter.
     * @param c buffer capacity
     */
    public static void setC(int c) {
        if(c >= 0)
            System.c = c;
    }

    /**
     * Current buffer state.
     */
    private static int bufferState = 0;

    /**
     * Function increases buffer state.
     */
    public static void increaseBufferState() {
        bufferState++;
    }
    /**
     * Function decreases buffer state.
     */
    public static void decreaseBufferState() {
        bufferState--;
        if(bufferState < 0)
            bufferState = 0;
    }
    /**
     * Buffer state getter.
     * @return current buffer state
     */
    public static int getBufferState() {
        return bufferState;
    }

    /**
     * True if system is busy (is handling an event).
     */
    public static boolean busy = false;

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

    private static long timeRemainingToEmptySystem = 0;

    public static long getTimeRemainingToEmptySystem() {
        return timeRemainingToEmptySystem;
    }

    public static void increaseTimeRemainingToEmptySystem(long additionalTime) {
        System.timeRemainingToEmptySystem += additionalTime;
    }

    public static void decreaseTimeRemainingToEmptySystem(long time) {
        System.timeRemainingToEmptySystem -= time;
        if(timeRemainingToEmptySystem < 0)
            timeRemainingToEmptySystem = 0;
    }
    /**
     * Constructor with lambda and mu parameters
     * @param lambda inflow intensity
     * @param mu service intensity
     */
    public System(double lambda, double mu, int c) {
        setLambda(lambda);
        setMu(mu);
        rho = lambda/mu;

        System.c = c;

        addEvent(new Event(Instant.ofEpochMilli(RandomGenerator.getNextExpDist(lambda)), Event.EventType.ARRIVAL));
    }
}
