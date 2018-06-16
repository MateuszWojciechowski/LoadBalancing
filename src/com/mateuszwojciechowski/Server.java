package com.mateuszwojciechowski;

import java.lang.System;
import java.util.ArrayList;

public class Server {
    private int serverID;
    private static int capacity = 1;
    private int load = 0;
    private double averageLoad = 0;
    private int handledRequests = 0;
    private long totalDuration = 0;

    public int getHandledRequests() {
        return handledRequests;
    }

    public void addToAverage(long duration){
        averageLoad += duration*load;
        totalDuration += duration;
    }
    public double getAverageLoad(){
        return averageLoad/totalDuration;
    }
    public int getServerID() {
        return serverID;
    }

    public static void setCapacity(int c) {
        capacity = c;
    }

    public static int getCapacity() {
        return capacity;
    }

    public Server(int serverID) {
        this.serverID = serverID;
    }

    public void increaseLoad() {
        load++;
        handledRequests++;
        if(load > capacity)
            System.out.println("Server overloaded");
    }

    public void decreaseLoad() {
        load--;
        if(load < 0)
            System.out.println("Load smaller than zero");
    }

    public int getLoad() {
        return load;
    }
}
