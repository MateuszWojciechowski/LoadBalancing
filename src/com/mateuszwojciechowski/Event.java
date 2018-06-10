package com.mateuszwojciechowski;

import java.time.Duration;
import java.time.Instant;

/**
 * Class representing a single event in the system.
 */
public class Event implements Comparable {
    /**
     * Enum type representing event type - arrival or departure.
     */
    public enum EventType { ARRIVAL, DEPARTURE }

    /**
     * A point on the time-line where event occurs.
     */
    private Instant eventStartTime;
    /**
     * Event type - arrival or departure.
     */
    private EventType eventType;

    private long eventDuration;

    /**
     * Constructor of the event. If event type is ARRIVAL then it creates departure event.
     * @param startTime point in time when event occurs
     * @param eventType type of the event
     */
    public Event(Instant startTime, EventType eventType) {
        this.eventStartTime = startTime;
        this.eventType = eventType;
        java.lang.System.out.println("NEW EVENT - start time: " + startTime.toString() + ", event type: " + eventType);
    }

    public Event(Instant startTime, EventType eventType, long eventDuration) {
        this.eventStartTime = startTime;
        this.eventType = eventType;
        this.eventDuration = eventDuration;
        java.lang.System.out.println("NEW EVENT - start time: " + startTime.toString() + ", event type: " + eventType);
    }

    /**
     * Event start time getter.
     * @return event start time
     */
    public Instant getEventStartTime() {
        return eventStartTime;
    }

    public EventType getEventType() {
        return eventType;
    }

    //Comparable implementation
    public int compareTo(Object o) {
        Event event = (Event) o;
        if(this.getEventStartTime().isBefore(event.getEventStartTime()))
            return -1;
        else if(this.getEventStartTime().isAfter(event.getEventStartTime()))
            return 1;
        else return 0;
    }

    public void process() {
        Statistics.addToAverageNumberInSystem(Duration.between(System.getPreviousEventTime(), eventStartTime).toMillis(), System.getNumberOfEventsInSystem());
        System.decreaseTimeRemainingToEmptySystem(Math.abs(Duration.between(getEventStartTime(), System.getPreviousEventTime()).toMillis()));
        java.lang.System.out.println("Time remaining to empty system: " + System.getTimeRemainingToEmptySystem() + "ms");
        if(eventType == EventType.ARRIVAL) {
            /*
            1. Check if buffer is not full.
            1. Increase number of events in the system.
            2. Generate departure event.
             */

            //if buffer is full
            if(System.getBufferState() >= System.getBufferCapacity()) {
                java.lang.System.out.println("No place in buffer, event rejected!");
                Statistics.increaseNumberOfRejectedArrivals();
                Statistics.increaseNumberOfArrivals();
                return;
            }

            System.increaseNumberOfEventsInSystem();
            if(System.busy == true)
                System.increaseBufferState();
            else
                System.busy = true;

            long newEventDuration = RandomGenerator.getNextExpDist(System.getMu());

            Statistics.addToAverageEventTime(newEventDuration);
            Statistics.addToAverageTimeToService(System.getTimeRemainingToEmptySystem());
            Statistics.increaseNumberOfArrivals();

            java.lang.System.out.println("Event duration: " + newEventDuration + "ms");
            System.addEvent(new Event(Instant.ofEpochMilli(eventStartTime.toEpochMilli() + newEventDuration + System.getTimeRemainingToEmptySystem()), EventType.DEPARTURE, newEventDuration));
            System.increaseTimeRemainingToEmptySystem(newEventDuration);
        }
        else {
            /*
            1. Decrease number of events in the system.
            2. Decrease time remaining to empty system
            If buffer is null, it was the last event and change busy to false.
             */
            Statistics.increaseNumberOfDepartures();
            System.decreaseNumberOfEventsInSystem();

            if(System.getBufferState() == 0)
                System.busy = false;
            else
                System.decreaseBufferState();
        }
        java.lang.System.out.println("Events in system: " + System.getNumberOfEventsInSystem());
    }
}
