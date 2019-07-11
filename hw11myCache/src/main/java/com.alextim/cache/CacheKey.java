package com.alextim.cache;

import com.alextim.domain.DataSet;

public class CacheKey {
    private Class cl;

    public CacheKey(Class cl) {
        this.cl = cl;
    }

    public static String getKey(DataSet dataSet) {
        return new CacheKey(dataSet.getClass())
                .createKey(dataSet.getId());
    }

    public String createKey(long id) {
        return cl.getName() + "-" + id;
    }
}
