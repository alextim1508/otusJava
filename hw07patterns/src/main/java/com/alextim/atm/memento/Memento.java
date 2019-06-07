package com.alextim.atm.memento;

import com.alextim.banknotes.Banknote;

import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

public class Memento {
    private final Map<Banknote, Integer> state;

    public Memento(Map<Banknote, Integer> state) {
        this.state = new TreeMap<>(Collections.reverseOrder(Comparator.comparingInt(Banknote::getNominal)));
        this.state.putAll(state);
    }

    public Map<Banknote, Integer> getState() {
        return state;
    }
}
