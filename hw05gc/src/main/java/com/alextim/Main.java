package com.alextim;

import com.alextim.monitoring.GCmonitoring;

import java.util.Timer;
import java.util.TimerTask;

/*
1) -Xms128m -Xmx128m -XX:+UseSerialGC
2) -Xms128m -Xmx128m -XX:+UseParallelGC
3) -Xms128m -Xmx128m -XX:+UseConcMarkSweepGC
4) -Xms128m -Xmx128m -XX:+UseG1GC
*/

public class Main {

    public static void main(String[] args) {

        GCmonitoring.startMonitoring();

        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                GCmonitoring.printStatisticsGC();
            }
        }, 0, 1000);

        App.startApp();
    }
}
