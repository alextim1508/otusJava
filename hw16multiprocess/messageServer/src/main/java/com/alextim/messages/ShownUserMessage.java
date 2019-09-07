package com.alextim.messages;

import lombok.ToString;

@ToString(callSuper = true)
public class ShownUserMessage extends Message {

    public ShownUserMessage(String waitingAnswerId) {
        super(ShownUserMessage.class, waitingAnswerId);
    }
}
