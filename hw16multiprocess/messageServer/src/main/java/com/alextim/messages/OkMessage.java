package com.alextim.messages;

public class OkMessage extends Message {

    public OkMessage(String waitingAnswerId) {
        super(OkMessage.class, waitingAnswerId);
    }
}
