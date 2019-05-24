package com.alextim.monitoring;


import javax.management.NotificationEmitter;
import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.util.Date;
import java.util.List;

public class GCmonitoring {
    private static Date timeStartMonitoring;

    public static void startMonitoring() {
        timeStartMonitoring = new Date();

        List<GarbageCollectorMXBean> gcBeans = ManagementFactory.getGarbageCollectorMXBeans();
        for (GarbageCollectorMXBean gcBean : gcBeans) {
            NotificationEmitter emitter = (NotificationEmitter) gcBean;
            emitter.addNotificationListener(new GCListener(), null, null);
        }
    }

    public static void printStatisticsGC() {
        int youngCount = GCListener.getYoungCount();
        int oldCount = GCListener.getOldCount();
        long totalDuration = GCListener.getTotalDuration();
        long timeMonitoring = (new Date().getTime()- timeStartMonitoring.getTime())/1000;

        System.out.println(
                "=========================================\n" +
                "Finish: YOUNG: " + youngCount +
                " OLD: " + oldCount +
                " Total duration: " + totalDuration + "ms," +
                " Total app life time: " + timeMonitoring + " s\n" +
                "=========================================");
    }
}