package com.alextim.messages;

import lombok.ToString;

@ToString(callSuper = true)
public class ShownErrorMessage extends Message {

    public ShownErrorMessage(String waitingAnswerId) {
        super(ShownErrorMessage.class, waitingAnswerId);
    }
}
