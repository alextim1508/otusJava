package com.alextim.atm.memento;

import java.util.HashMap;
import java.util.Map;

public class Caretaker {
    private static Map<String, Memento> mementos = new HashMap<>();

    public static Memento getMemento(String key) {
        return mementos.get(key);
    }

    public static void putMemento(String key, Memento mem) {
        mementos.put(key, mem);
    }
}
