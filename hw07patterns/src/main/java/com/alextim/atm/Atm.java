package com.alextim.atm;


import com.alextim.banknotes.Banknote;

import java.util.Map;

public interface Atm {

    void putDeposit(Banknote banknote, int count);

    Map<Banknote, Integer> takeDeposit(int deposit) throws WrongDepositException;

    int getBalance();

    void reset();
}