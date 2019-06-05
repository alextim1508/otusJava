package com.alextim.atm.memento;

public interface MementoStorable {

    Memento saveState();

    void restoreState(Memento memento);
}
