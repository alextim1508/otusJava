package com.alextim.monitoring;


import com.sun.management.GarbageCollectionNotificationInfo;

public class GCinfo {
    private final String name;
    private final Generation generation;
    private final long startTime;
    private final long duration;

    private GCinfo(String name, Generation generation, long startTime, long duration) {
        this.name = name;
        this.generation = generation;
        this.startTime = startTime;
        this.duration = duration;
    }

    public static GCinfo parse(GarbageCollectionNotificationInfo notificationInfo) {
        String gcName = notificationInfo.getGcName();
        Generation generation = Generation.parseGeneration(notificationInfo.getGcAction());
        long startTime = notificationInfo.getGcInfo().getStartTime();
        long duration = notificationInfo.getGcInfo().getDuration();
        return new GCinfo(gcName, generation, startTime, duration);
    }

    @Override
    public String toString() {
        return "Start: " + startTime +
                ", GC name = " + this.name + " (" + this.generation + ")" +
                ", GC duration = " + this.duration + " ms";
    }

    public String getName() {
        return name;
    }

    public Generation getGeneration() {
        return generation;
    }

    public long getDuration() {
        return duration;
    }

    public long getStartTime() {
        return startTime;
    }
}