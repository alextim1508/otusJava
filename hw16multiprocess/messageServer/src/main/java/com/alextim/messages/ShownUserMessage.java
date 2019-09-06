package com.alextim.messages;

public class ShownUserMessage extends Message {

    public ShownUserMessage(String waitingAnswerId) {
        super(ShownUserMessage.class, waitingAnswerId);
    }
}
