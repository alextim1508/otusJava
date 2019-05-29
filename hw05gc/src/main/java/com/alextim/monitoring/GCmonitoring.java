package com.alextim.monitoring;


import javax.management.NotificationEmitter;
import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.Date;
import java.util.List;

import com.alextim.App;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GCmonitoring {

    private static Date timeStartMonitoring;

    private static final Logger logger = LoggerFactory.getLogger(GCmonitoring.class);

    private static final String argGC = getArgGC();

    public static void startMonitoring() {
        timeStartMonitoring = new Date();

        List<GarbageCollectorMXBean> gcBeans = ManagementFactory.getGarbageCollectorMXBeans();
        for (GarbageCollectorMXBean gcBean : gcBeans) {
            NotificationEmitter emitter = (NotificationEmitter) gcBean;
            emitter.addNotificationListener(new GCListener(), null, null);
        }
    }

    private static String getArgGC() {
        RuntimeMXBean runtimeMxBean = ManagementFactory.getRuntimeMXBean();
        List<String> arguments = runtimeMxBean.getInputArguments();
        for (String arg: arguments) {
            if(arg.startsWith("-XX:")) {
                return arg;
            }
        }
        return null;
    }

    public static void printStatisticsGC() {
        int youngCount = GCListener.getYoungCount();
        int oldCount = GCListener.getOldCount();
        long totalDuration = GCListener.getTotalDuration();
        long timeMonitoring = (new Date().getTime()- timeStartMonitoring.getTime())/1000;

        logger.info(
                "Performance (current list size): " + App.getSizeList() +
                " Arg GC: " + argGC +
                " YOUNG: " + youngCount +
                " OLD: " + oldCount +
                " Total duration: " + totalDuration + "ms," +
                " Total app life time: " + timeMonitoring + " s");
    }
}