package com.alextim.messages;

import lombok.ToString;

@ToString(callSuper = true)
public class ShownUsersMessage extends Message {

    public ShownUsersMessage(String waitingAnswerId) {
        super(ShownUsersMessage.class, waitingAnswerId);
    }
}
