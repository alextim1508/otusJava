package com.alextim.cache;

public interface CacheListener <V> {
    void notify(V value, String action);
}
