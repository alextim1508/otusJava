package com.alextim.department;

import com.alextim.atm.Atm;

import java.util.function.Consumer;

public interface Department {

    Atm createAtm(String title, Consumer<Atm> initState);

    void resetEvent();

    int getTotalBalance();
}
