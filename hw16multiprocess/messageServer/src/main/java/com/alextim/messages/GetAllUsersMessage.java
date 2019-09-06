package com.alextim.messages;


public class GetAllUsersMessage extends Message {

    public GetAllUsersMessage(String waitingAnswerId) {
        super(GetAllUsersMessage.class, waitingAnswerId);
    }
}
