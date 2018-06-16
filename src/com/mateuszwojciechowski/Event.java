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

    private int serverID;

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

    public Event(Instant startTime, EventType eventType, long eventDuration, int serverID) {
        this.eventStartTime = startTime;
        this.eventType = eventType;
        this.eventDuration = eventDuration;
        this.serverID = serverID;
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
        for(int i=0; i < System.getServersNum(); i++) {
            System.getServer(i).addToAverage(Duration.between(System.getPreviousEventTime(), eventStartTime).toMillis());
        }
        if(eventType == EventType.ARRIVAL) {
            /*
            1. Find appropiate server (not overloaded)
            2. Send there a request
             */

            //Choose server which should handle the request
            int i = 0;
            int newServerID;
            while(true) {
                if(i == System.getServersNum()) {
                    java.lang.System.out.println("Servers overloaded, request lost.");
                    for(int j=0; j < System.getServersNum(); j++) {
                        java.lang.System.out.println("Current server #" + j + " load: " + System.getServer(j).getLoad());
                    }
                    Statistics.increaseNumberOfRejectedArrivals();
                    return;
                }
                newServerID = System.getNextServerID();
                if(System.getServer(newServerID).getLoad() >= Server.getCapacity()) {
                    java.lang.System.out.println("Server #" + newServerID + " overloaded.");
                    System.setLastServerUsed(newServerID);
                    i++;
                }
                else break;
            }

            System.getServer(newServerID).increaseLoad();

            java.lang.System.out.println("Request sent to server #" + newServerID);
            java.lang.System.out.println("Current server #" + newServerID + " load: " + System.getServer(newServerID).getLoad());

            System.increaseNumberOfEventsInSystem();

            long newEventDuration = RandomGenerator.getNextExpDist(System.getMu());

            Statistics.addToAverageEventTime(newEventDuration);
            Statistics.increaseNumberOfArrivals();

            java.lang.System.out.println("Event duration: " + newEventDuration + "ms");
            System.addEvent(new Event(Instant.ofEpochMilli(eventStartTime.toEpochMilli() + newEventDuration), EventType.DEPARTURE, newEventDuration, newServerID));
        }
        else {
            /*
            1. Decrease number of events in the system.
            2. Decrease time remaining to empty system
            If buffer is null, it was the last event and change busy to false.
             */
            Statistics.increaseNumberOfDepartures();
            System.decreaseNumberOfEventsInSystem();
            System.getServer(serverID).decreaseLoad();
            java.lang.System.out.println("Current server #" + serverID + " load: " + System.getServer(serverID).getLoad());
        }
        java.lang.System.out.println("Events in system: " + System.getNumberOfEventsInSystem());
    }
}
