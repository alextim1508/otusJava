package com.alextim.banknotes;

public enum RubBanknote implements Banknote {

    Rub100(100, "RUB"),
    Rub200(200, "RUB"),
    Rub500(500, "RUB"),
    Rub1000(1000, "RUB"),
    Rub2000(2000, "RUB"),
    Rub5000(5000, "RUB");

    private final int nominal;

    private final String code;

    RubBanknote(int nominal, String code) {
        this.nominal = nominal;
        this.code = code;
    }

    @Override
    public int getNominal() {
        return nominal;
    }

    @Override
    public String getCode() {
        return code;
    }
}
