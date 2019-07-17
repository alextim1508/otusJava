package com.alextim;

import com.alextim.cache.Cache;
import com.alextim.cache.CacheListener;
import com.alextim.cache.MyCache;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.concurrent.TimeUnit;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

public class MyCacheTest {

    private Cache<String> cache
            = new MyCache<>(10, TimeUnit.SECONDS.toMillis(1), TimeUnit.SECONDS.toMillis(1));

    private static CacheListener<String> listener =  Mockito.mock(CacheListener.class); //Todo как написать Mockito.mock(CacheListener<CacheElement<String>>.class)

    @BeforeAll
    public static void setUp() {
        doNothing().when(listener).notify(any(String.class), anyString());
    }

    @Test
    public void cacheListenerTest() {
        cache.addListener(listener);
        cache.put("key", "1");
        cache.remove("key");
        verify(listener, times(2)).notify(any(String.class), anyString());
        cache.removeListener(listener);
    }

    @Test
    public void putGetElementTest() {
        cache.put("key", "1");
        Assertions.assertEquals("1", cache.get("key"));
        cache.remove("key");
    }

    @Test
    public void elementLifeTimeTest() throws InterruptedException {
        cache.put("key", "1");
        cache.print();

        System.out.println("Sleep...");
        Thread.currentThread().sleep(1050);
        System.out.println("Wake up");

        cache.print();

        Assertions.assertEquals(null, cache.get("key"));
    }

}
