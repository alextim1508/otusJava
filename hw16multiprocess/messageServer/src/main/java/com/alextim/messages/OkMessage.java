package com.alextim.messages;

import lombok.ToString;

@ToString(callSuper = true)
public class OkMessage extends Message {

    public OkMessage(String waitingAnswerId) {
        super(OkMessage.class, waitingAnswerId);
    }
}
