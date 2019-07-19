package com.alextim.cache;

public interface Cache<V> {

    void put(CacheKey key, V value);

    void remove(CacheKey key);

    V get(CacheKey key);

    void addListener(CacheListener<V> listener);

    void removeListener(CacheListener<V> listener);

    void dispose();

    int size();
}
