package com.alextim.atm;

import com.alextim.banknotes.Banknote;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class SpbAtm implements Atm {

    TreeMap<Banknote, Integer> balance = new TreeMap<>(Collections.reverseOrder(Comparator.comparingInt(Banknote::getNominal)));

    public SpbAtm() {
    }

    @Override
    public void putDeposit(Banknote banknote, int count) {
        balance.compute(banknote, (banknote1, oldCount) -> Optional.ofNullable(oldCount).orElse(0) + count);
    }

    @Override
    public long getBalance() {
        AtomicInteger amount = new AtomicInteger(0);
        balance.forEach((banknote, count) -> amount.addAndGet(banknote.getNominal() * count));
        return amount.longValue();
    }

    @Override
    public Map<Banknote, Integer> takeDeposit(int deposit) {
        Map<Banknote, Integer> take = new HashMap<>();
        AtomicInteger currentDeposit = new AtomicInteger(deposit);

        balance.keySet().forEach(banknote -> {
            int desiredBanknoteCount =  currentDeposit.intValue() / banknote.getNominal();

            if(desiredBanknoteCount == 0)
                return;

            int existingBanknoteCount = balance.get(banknote);

            int takenBanknoteCount = desiredBanknoteCount < existingBanknoteCount ?
                    desiredBanknoteCount : existingBanknoteCount;

            take.put(banknote, takenBanknoteCount);
            currentDeposit.getAndUpdate(amount -> amount - takenBanknoteCount * banknote.getNominal());
        });

        if(currentDeposit.intValue() != 0)
            throw new RuntimeException("It is not possible to give money");

        take.forEach((banknote, takeCount) ->
                balance.compute(banknote, (banknote1, count) -> count - takeCount));

        return take;
    }
}