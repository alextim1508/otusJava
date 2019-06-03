package com.alextim.atm;

public class WrongDepositException extends Exception {
    public WrongDepositException() {
    }

    public WrongDepositException(String message) {
        super(message);
    }

    public WrongDepositException(String message, Throwable cause) {
        super(message, cause);
    }

    public WrongDepositException(Throwable cause) {
        super(cause);
    }

    public WrongDepositException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
