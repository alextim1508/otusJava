package com.alextim.department;

import com.alextim.atm.Atm;
import com.alextim.atm.SimpleAtm;
import com.alextim.atm.chain.Middleware;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class SimpleDepartment implements Department {

    private final List<Atm> atms = new ArrayList<>();

    private Middleware middleware;

    @Override
    public Atm createAtm(String title, Consumer<Atm> initState) {
        SimpleAtm atm = new SimpleAtm(title, initState);

        if(atms.isEmpty())
            setMiddleware(atm);
        else
            ((Middleware) atms.get(atms.size()-1)).linkWith(atm);

        atms.add(atm);
        return atm;
    }

    @Override
    public void resetEvent() {
        atms.stream().forEach(Atm::reset);
    }

    @Override
    public int getTotalBalance() {
        return atms.stream().mapToInt(Atm::getBalance).sum();
    }


    public int getTotalBalance2() {
        return ((Middleware)atms.get(0)).check(0);
    }

    public void setMiddleware(Middleware middleware) {
        this.middleware = middleware;
    }
}
