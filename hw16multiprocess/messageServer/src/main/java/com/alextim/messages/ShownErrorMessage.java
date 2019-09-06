package com.alextim.messages;

public class ShownErrorMessage extends Message {

    public ShownErrorMessage(String waitingAnswerId) {
        super(ShownErrorMessage.class, waitingAnswerId);
    }
}
