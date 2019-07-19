package com.alextim;

import com.alextim.cache.Cache;
import com.alextim.cache.CacheKey;
import com.alextim.cache.CacheListener;
import com.alextim.cache.MyCache;
import com.alextim.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;

import java.util.concurrent.TimeUnit;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

@Slf4j
public class MyCacheTest {

    private Cache<String> cache
            = new MyCache<>(10, TimeUnit.SECONDS.toMillis(1), TimeUnit.SECONDS.toMillis(1));

    private static CacheListener<String> listener =  Mockito.mock(CacheListener.class);

    private CacheKey key = CacheKey.buildKey(User.class, 1L);

    @BeforeAll
    public static void setUp() {
        doNothing().when(listener).notify(any(String.class), anyString());
    }

    @Test
    public void cacheListenerTest() {
        cache.addListener(listener);
        cache.put(key, "1");
        cache.remove(key);
        verify(listener, times(2)).notify(any(String.class), anyString());
        cache.removeListener(listener);
    }

    @Test
    public void putGetElementTest() {
        cache.put(key, "1");
        Assertions.assertEquals("1", cache.get(key));
        cache.remove(key);
    }

    @Test
    public void elementLifeTimeTest() throws InterruptedException {
        cache.put(key, "1");
        log.debug("Size: {}", cache.size());

        log.debug("Sleep...");
        Thread.currentThread().sleep(1100);
        log.debug("Wake up!");

        log.debug("Size: {}", cache.size());

        Assertions.assertEquals(null, cache.get(key));
    }

}
