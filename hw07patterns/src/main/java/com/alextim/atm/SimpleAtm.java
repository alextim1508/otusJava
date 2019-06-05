package com.alextim.atm;


import com.alextim.atm.chain.Middleware;
import com.alextim.atm.memento.Memento;
import com.alextim.atm.memento.MementoStorable;
import com.alextim.banknotes.Banknote;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class SimpleAtm extends Middleware implements Atm, MementoStorable {

    private final Map<Banknote, Integer> balance =
            new TreeMap<>(Collections.reverseOrder(Comparator.comparingInt(Banknote::getNominal)));

    private final String title;
    private final Consumer<Atm> initState;

    public SimpleAtm(String title, Consumer<Atm> initState) {
        this.title = title;
        this.initState = initState;
        reset();
    }

    @Override
    public void putDeposit(Banknote banknote, int count) {
        balance.compute(banknote, (banknote1, oldCount) -> Optional.ofNullable(oldCount).orElse(0) + count);
    }

    @Override
    public Map<Banknote, Integer> takeDeposit(int deposit) throws WrongDepositException {
        AtomicInteger currentDeposit = new AtomicInteger(deposit);
        Map<Banknote, Integer> take = balance.entrySet().stream().map(entry -> {
            int fact = Math.min(currentDeposit.get() / entry.getKey().getNominal(), entry.getValue());
            currentDeposit.addAndGet(- fact * entry.getKey().getNominal());
            return new TreeMap.SimpleEntry<>(entry.getKey(), fact);
        }).collect(Collectors.toMap(AbstractMap.SimpleEntry::getKey, AbstractMap.SimpleEntry::getValue));

        if(currentDeposit.get() != 0)
            throw new WrongDepositException("It is not possible to give money");

        take.forEach((banknote, takeCount) ->
                balance.compute(banknote, (banknote1, count) -> count - takeCount));

        return take;
    }

    @Override
    public int getBalance() {
        return balance.entrySet().stream().mapToInt(banknote -> banknote.getKey().getNominal() * banknote.getValue()).sum();
    }

    @Override
    public void reset() {
        clearBalance();
        initState.accept(this);
    }

    private void clearBalance() {
        balance.keySet().forEach(banknote -> balance.compute(banknote, (banknote1, oldCount) -> 0));
    }

    @Override
    public Memento saveState() {
        return new Memento(balance);
    }

    @Override
    public void restoreState(Memento memento) {
        clearBalance();
        balance.putAll(memento.getState());
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("SimpleAtm {\n");
        builder.append("\ttitle: " + title + "\n");
        builder.append("\tbanknotes:\n");
        balance.forEach((banknote, count) ->
                builder.append("\t\t" + banknote + ": " + count + "\n"));
        builder.append("}");
        return builder.toString();
    }

    @Override
    public int check(int i) {
        return checkNext(i + getBalance());
    }
}