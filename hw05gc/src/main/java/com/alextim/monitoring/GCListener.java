package com.alextim.monitoring;

import com.sun.management.GarbageCollectionNotificationInfo;

import javax.management.Notification;
import javax.management.NotificationListener;
import javax.management.openmbean.CompositeData;

public class GCListener implements NotificationListener {
    private static int youngCount; /* Todo не будет ли проблем с многопоточностью ?*/
    private static int oldCount;
    private static long totalDuration;

    @Override
    public void handleNotification(Notification notification, Object handback) {
        if (notification.getType().equals(GarbageCollectionNotificationInfo.GARBAGE_COLLECTION_NOTIFICATION)) {
            GarbageCollectionNotificationInfo notificationInfo =
                    GarbageCollectionNotificationInfo.from((CompositeData) notification.getUserData());

            GCinfo info = GCinfo.parse(notificationInfo);
            System.out.println(info);

            totalDuration += info.getDuration();

            switch (info.getGeneration()) {
                case OLD:
                    oldCount++;
                    break;

                case YOUNG:
                    youngCount++;
                    break;

                default:
                    break;
            }
        }
    }

    public static int getYoungCount() {
        return youngCount;
    }

    public static int getOldCount() {
        return oldCount;
    }

    public static long getTotalDuration() {
        return totalDuration;
    }
}