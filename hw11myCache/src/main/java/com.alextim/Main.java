package com.alextim;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

public class Main {
    public static void main(String[] args) {
        Map<String, String> map= new HashMap<>();

        map.forEach((key, value) -> System.out.println("Key: " + key + " value: " + value));

        map.put("key", "value1");
        map.put("key", "value2");
        map.put("key", "value3");

        map.remove("key");
        map.forEach((key, value) -> System.out.println("Key: " + key + " value: " + value));

    }
}
