package com.alextim.cache;

import lombok.Getter;
import lombok.ToString;

import java.lang.ref.SoftReference;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class CacheElement<T> extends SoftReference<T>  {

    private final String name;

    @Getter
    private final long creationTime;

    @Getter
    private long lastAccessTime;

    public CacheElement(T value) {
        super(value);
        this.name = value.getClass().getName();
        this.creationTime = getCurrentTime();
        this.lastAccessTime = getCurrentTime();
    }

    private long getCurrentTime() {
        return System.currentTimeMillis();
    }

    public void setAccessed() {
        lastAccessTime = getCurrentTime();
    }

    @Override
    public String toString() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss:SSS");

        cal.setTime(new Date(creationTime));
        String formattedCreationTime = format.format(cal.getTime());
        cal.setTime(new Date(lastAccessTime));
        String formattedLastAccessTime = format.format(cal.getTime());

        return "CacheElement{" + "name='" + name + '\'' +
                ", creationTime=" + formattedCreationTime +
                ", lastAccessTime=" + formattedLastAccessTime + '}';
    }
}
