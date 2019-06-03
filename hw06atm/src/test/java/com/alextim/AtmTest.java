package com.alextim;

import com.alextim.atm.Atm;
import com.alextim.atm.SpbAtm;
import com.alextim.atm.WrongDepositException;
import com.alextim.banknotes.Banknote;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Optional;

import static com.alextim.banknotes.RubBanknote.*;

public class AtmTest {

    private Atm atm = new SpbAtm();

    @BeforeEach
    void setUp() {
        atm = new SpbAtm();
    }

    @Test
    public void putDepositTest() {
        atm.putDeposit(Rub2000, 5);
        atm.putDeposit(Rub200, 2);
        atm.putDeposit(Rub5000, 6);
        atm.putDeposit(Rub100, 1);
        atm.putDeposit(Rub500, 3);
        atm.putDeposit(Rub1000, 4);

        Assertions.assertEquals(46000, atm.getBalance());
    }

    @Test
    public void takeDepositTest1() throws WrongDepositException {
        atm.putDeposit(Rub1000, 2);
        atm.putDeposit(Rub500, 2);
        atm.putDeposit(Rub5000, 2);
        atm.putDeposit(Rub100, 2);
        atm.putDeposit(Rub2000, 2);
        atm.putDeposit(Rub200, 2);

        Map<Banknote, Integer> takeDeposit = atm.takeDeposit(1200);

        Assertions.assertEquals(0, Optional.ofNullable(takeDeposit.get(Rub100)).orElse(0).intValue());
        Assertions.assertEquals(1, Optional.ofNullable(takeDeposit.get(Rub200)).orElse(0).intValue());
        Assertions.assertEquals(0, Optional.ofNullable(takeDeposit.get(Rub500)).orElse(0).intValue());
        Assertions.assertEquals(1, Optional.ofNullable(takeDeposit.get(Rub1000)).orElse(0).intValue());
        Assertions.assertEquals(0, Optional.ofNullable(takeDeposit.get(Rub2000)).orElse(0).intValue());
        Assertions.assertEquals(0, Optional.ofNullable(takeDeposit.get(Rub5000)).orElse(0).intValue());
    }

    @Test
    public void takeDepositTest2() {
        atm.putDeposit(Rub500, 2);
        atm.putDeposit(Rub200, 2);
        atm.putDeposit(Rub5000, 2);
        atm.putDeposit(Rub1000, 2);
        atm.putDeposit(Rub100, 2);
        atm.putDeposit(Rub2000, 2);

        Assertions.assertThrows(WrongDepositException.class, () -> atm.takeDeposit(7850));
    }
}
