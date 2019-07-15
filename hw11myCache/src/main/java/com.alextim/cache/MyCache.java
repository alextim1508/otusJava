package com.alextim.cache;

import lombok.*;

import java.lang.ref.SoftReference;
import java.util.*;
import java.util.function.Function;

@Data @RequiredArgsConstructor
public class MyCache<V extends CacheElement<?>> implements Cache<V> {

    private final Map<String, V> cache = new HashMap<>();

    private final Set<CacheListener <V>> listeners = new HashSet<>();

    private final Timer timer = new Timer();

    @NonNull
    private int maxElements;

    @NonNull
    private long lifeTimeMs;

    @NonNull
    private long idleTimeMs;

    @Setter(AccessLevel.NONE)
    private int hit;

    @Setter(AccessLevel.NONE)
    private int miss;

    @Override
    public void put(String key, V value) {
        if (cache.size() == maxElements) {
            String firstKey = cache.keySet().iterator().next();
            cache.remove(firstKey);
        }

        cache.put(key, value);

        if (lifeTimeMs != 0) {
            TimerTask lifeTimerTask = getTimerTask(key, lifeElement -> lifeElement.getCreationTime() + lifeTimeMs);
            timer.schedule(lifeTimerTask, lifeTimeMs);
        }
        if (idleTimeMs != 0) {
            TimerTask idleTimerTask = getTimerTask(key, idleElement -> idleElement.getLastAccessTime() + idleTimeMs);
            timer.schedule(idleTimerTask, idleTimeMs, idleTimeMs);
        }

        listeners.forEach(listener -> listener.notify(value, "put new object"));
    }

    @Override
    public void remove(String key) {
        V removed = cache.remove(key);
        listeners.forEach(listener -> listener.notify(removed, "remove object"));
    }

    @Override
    public V get(String key) {
        V element = cache.get(key);
        if(element != null) {
            if(element.get() == null) {
                remove(key);
                return null;
            }
            hit++;
            element.setAccessed();
        }
        else {
            miss++;
        }
        return element;
    }

    @Override
    public void dispose() {
        timer.cancel();
    }

    private TimerTask getTimerTask(final String key, Function<CacheElement<?>, Long> timeFunction) {
        return new TimerTask() {
            @Override
            public void run() {
                V element = cache.get(key);
                if (element == null || timeFunction.apply(element) < System.currentTimeMillis()) {
                    remove(key);
                    this.cancel();
                }
            }
        };
    }

    @Override
    public void addListener(CacheListener<V> listener) {
        listeners.add(listener);
    }

    @Override
    public void removeListener(CacheListener<V> listener) {
        listeners.remove(listener);
    }

}
