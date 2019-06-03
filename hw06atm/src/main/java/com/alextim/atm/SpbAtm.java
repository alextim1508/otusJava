package com.alextim.atm;

import com.alextim.banknotes.Banknote;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class SpbAtm implements Atm {

    private final Map<Banknote, Integer> balance =
            new TreeMap<>(Collections.reverseOrder(Comparator.comparingInt(Banknote::getNominal)));

    public SpbAtm() {
    }

    @Override
    public void putDeposit(Banknote banknote, int count) {
        balance.compute(banknote, (banknote1, oldCount) -> Optional.ofNullable(oldCount).orElse(0) + count);
    }

    @Override
    public long getBalance() {
        return balance.entrySet().stream().mapToInt(banknote -> banknote.getKey().getNominal() * banknote.getValue()).sum();
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

}