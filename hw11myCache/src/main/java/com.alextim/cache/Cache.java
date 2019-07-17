package com.alextim.cache;

public interface Cache<V> {

    void put(String key, V value);

    void remove(String key);

    V get(String key);

    void addListener(CacheListener<V> listener);

    void removeListener(CacheListener<V> listener);

    void dispose();

    void print();
}
