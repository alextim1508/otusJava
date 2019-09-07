package com.alextim.messages;

import lombok.ToString;

@ToString(callSuper = true)
public class UnknownMessage extends Message {
    public UnknownMessage(String waitingAnswerId) {
        super(UnknownMessage.class, waitingAnswerId);
    }
}
