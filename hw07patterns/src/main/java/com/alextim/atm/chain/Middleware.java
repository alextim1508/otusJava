package com.alextim.atm.chain;

public abstract class Middleware {

    private Middleware next;

    public Middleware linkWith(Middleware next) {
        this.next = next;
        return next;
    }

    public abstract int check(int i);

    public int checkNext(int i) {
        if (next == null) {
            return i;
        }
        return next.check(i);
    }
}