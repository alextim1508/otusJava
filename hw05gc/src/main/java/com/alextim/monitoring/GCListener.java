package com.alextim.monitoring;

import com.sun.management.GarbageCollectionNotificationInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.management.Notification;
import javax.management.NotificationListener;
import javax.management.openmbean.CompositeData;

public class GCListener implements NotificationListener {
    private static int youngCount;
    private static int oldCount;
    private static long totalDuration;

    private static final Logger logger = LoggerFactory.getLogger(GCListener.class);

    @Override
    public void handleNotification(Notification notification, Object handback) {
        if (notification.getType().equals(GarbageCollectionNotificationInfo.GARBAGE_COLLECTION_NOTIFICATION)) {
            GarbageCollectionNotificationInfo notificationInfo =
                    GarbageCollectionNotificationInfo.from((CompositeData) notification.getUserData());

            GCinfo info = GCinfo.parse(notificationInfo);
            logger.info(info.toString());

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

    public static synchronized int getYoungCount() {
        return youngCount;
    }

    public static synchronized int getOldCount() {
        return oldCount;
    }

    public static synchronized long getTotalDuration() {
        return totalDuration;
    }
}