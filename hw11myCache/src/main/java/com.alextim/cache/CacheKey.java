package com.alextim.cache;

import com.alextim.domain.DataSet;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE) @EqualsAndHashCode @ToString
public class CacheKey {

    private final String key;

    public static CacheKey buildKey(DataSet dataSet) {
        return new CacheKey(createKey(dataSet.getClass(), dataSet.getId()));
    }

    public static CacheKey buildKey(Class<?> cl, long id) {
        return new CacheKey(createKey(cl, id));
    }

    private static String createKey(Class<?> cl, long id) {
        return cl.getName() + "/" + id;
    }
}
