package com.alextim.cache;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.lang.ref.SoftReference;
import java.util.Optional;

public class CacheElement<T> extends SoftReference<T>  {

    @Getter
    private final long creationTime;

    @Getter
    private long lastAccessTime;

    public CacheElement(T value) {
        super(value);
        this.creationTime = getCurrentTime();
        this.lastAccessTime = getCurrentTime();
    }

    protected long getCurrentTime() {
        return System.currentTimeMillis();
    }

    public void setAccessed() {
        lastAccessTime = getCurrentTime();
    }
}
