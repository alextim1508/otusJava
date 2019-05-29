package com.alextim.monitoring;

public enum Generation {
    YOUNG, OLD;

    public static Generation parseGeneration(String gcMessage) {
        if (gcMessage.equals("end of minor GC")) {
            return YOUNG;
        }
        if (gcMessage.equals("end of major GC")) {
            return OLD;
        }
        else return null;
    }
}
