package com.alextim;

import com.alextim.monitoring.GCmonitoring;

/*
1) -Xms128m -Xmx128m -XX:+UseSerialGC
2) -Xms128m -Xmx128m -XX:+UseParallelGC
3) -Xms128m -Xmx128m -XX:+UseConcMarkSweepGC
4) -Xms128m -Xmx128m -XX:+UseG1GC
*/

public class Main {

    public static void main(String[] args) {


        GCmonitoring.startMonitoring();
        try {
            App.startApp();
        }
        catch (OutOfMemoryError er) {
            System.out.println("OutOfMemoryError");
        }
        finally {
            GCmonitoring.printStatisticsGC();
        }
    }
}
