package com.alextim.messages;

public class ShownUsersMessage extends Message {

    public ShownUsersMessage(String waitingAnswerId) {
        super(ShownUsersMessage.class, waitingAnswerId);
    }
}
