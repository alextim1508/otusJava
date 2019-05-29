package com.alextim.banknotes;

public enum RubBanknote implements Banknote {

    Rub100(100),
    Rub200(200),
    Rub500(500),
    Rub1000(1000),
    Rub2000(2000),
    Rub5000(5000);

    private int nominal;

    RubBanknote(int nominal) {
        this.nominal = nominal;
    }

    @Override
    public int getNominal() {
        return nominal;
    }
}
