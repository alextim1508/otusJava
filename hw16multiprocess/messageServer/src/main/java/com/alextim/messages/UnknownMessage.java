package com.alextim.messages;

public class UnknownMessage extends Message {
    public UnknownMessage(String waitingAnswerId) {
        super(UnknownMessage.class, waitingAnswerId);
    }
}
