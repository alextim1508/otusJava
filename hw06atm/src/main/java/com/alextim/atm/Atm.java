package com.alextim.atm;

import java.util.Map;
import com.alextim.banknotes.Banknote;

public interface Atm {

    void putDeposit(Banknote banknote, int count);

    Map<Banknote, Integer> takeDeposit(int deposit);

    long getBalance();
}