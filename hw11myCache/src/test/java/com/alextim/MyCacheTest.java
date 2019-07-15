package com.alextim;

import com.alextim.cache.Cache;
import com.alextim.cache.CacheElement;
import com.alextim.cache.CacheListener;
import com.alextim.cache.MyCache;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

public class MyCacheTest {

    private Cache<CacheElement<String>> cache
            = new MyCache<>(10, TimeUnit.SECONDS.toMillis(1), TimeUnit.SECONDS.toMillis(1));

    private static CacheListener<CacheElement<String>> listener =  Mockito.mock(CacheListener.class); //Todo как написать Mockito.mock(CacheListener<CacheElement<String>>.class)

    @BeforeAll
    public static void setUp() {
        doNothing().when(listener).notify(any(CacheElement.class), anyString());
    }

    @Test
    public void cacheListenerTest() {
        cache.addListener(listener);
        cache.put("key", new CacheElement<>("1"));
        cache.remove("key");

        verify(listener, times(2)).notify(any(CacheElement.class), anyString());
        cache.removeListener(listener);
    }

    @Test
    public void putGetElementTest() {
        CacheElement<String> element = new CacheElement<>("1");
        cache.put("key", element);
        Assertions.assertEquals(element, cache.get("key"));
        cache.remove("key");
    }

    @Test
    public void elementLifeTimeTest() throws InterruptedException {
        CacheElement<String> element = new CacheElement<String>("1") {
            @Override
            protected long getCurrentTime() {
                Calendar calendar = Calendar.getInstance();
                return calendar.getTime().getTime();
            }
        };
        cache.put("key", element);

        Thread.currentThread().sleep(1100);
        //Todo очень плохо. Но так как используется таймер, то ничего не поделаешь... Да?
        Assertions.assertEquals(null, cache.get("key"));
    }
}
