package com.alextim.cache;

import lombok.*;

import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;

@Data @RequiredArgsConstructor @Slf4j
public class MyCache<V> implements Cache<V> {

    private final Map<CacheKey, CacheElement<V>> cache = new HashMap<>();

    private final Set<CacheListener<V>> listeners = new HashSet<>();

    private final Map<CacheKey, List<TimerTask>> timerTasks = new HashMap<>();

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
    public void put(CacheKey key, V value) {
        log.info("Put: {} {}", key, value);

        if (cache.size() == maxElements) {
            CacheKey firstKey = cache.keySet().iterator().next();
            cache.remove(firstKey);
        }

        if(cache.containsKey(key))
            remove(key);

        cache.put(key, new CacheElement<>(value));

        List<TimerTask> timerTasksList = new ArrayList<>();
        if (lifeTimeMs != 0) {
            TimerTask lifeTimerTask = getTimerTask(key, lifeElement -> lifeElement.getCreationTime() + lifeTimeMs);
            timer.schedule(lifeTimerTask, lifeTimeMs);
            timerTasksList.add(lifeTimerTask);
        }
        if (idleTimeMs != 0) {
            TimerTask idleTimerTask = getTimerTask(key, idleElement -> idleElement.getLastAccessTime() + idleTimeMs);
            timer.schedule(idleTimerTask, idleTimeMs, idleTimeMs);
            timerTasksList.add(idleTimerTask);
        }

        timerTasks.put(key, timerTasksList);

        listeners.forEach(listener -> listener.notify(value, "put new object"));
    }

    @Override
    public void remove(CacheKey key) {
        CacheElement<V> removed = cache.remove(key);
        log.info("Remove: {} {}", key, removed);

        timerTasks.get(key).forEach(TimerTask::cancel);

        listeners.forEach(listener -> {
            if(removed != null)
                listener.notify(removed.get(), "remove object");
        });
    }

    @Override
    public V get(CacheKey key) {
        CacheElement<V> element = cache.get(key);
        log.info("Get: {} {}", key, element);

        if(element != null) {
            if(element.get() == null) {
                remove(key);
                return null;
            }
            hit++;
            element.setAccessed();
            return element.get();
        }
        else {
            miss++;
            return null;
        }
    }

    @Override
    public void dispose() {
        timer.cancel();
    }

    private TimerTask getTimerTask(final CacheKey key, Function<CacheElement<?>, Long> timeFunction) {
        return new TimerTask() {
            @Override
            public void run() {
                CacheElement<V> element = cache.get(key);
                log.debug("Timer event {} {}", key, element);

                if(element == null || timeFunction.apply(element) < System.currentTimeMillis()) {
                    remove(key);
                }
                else {
                    log.debug("Survived {} {}", key, element);
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

    @Override
    public int size() {
        return cache.size();
    }
}
