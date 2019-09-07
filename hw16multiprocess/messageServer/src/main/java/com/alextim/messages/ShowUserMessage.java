package com.alextim.messages;

import lombok.Data;
import lombok.ToString;

@Data @ToString(callSuper = true)
public class ShowUserMessage extends Message {

    private UserDto user;

    public ShowUserMessage(UserDto user, String waitingAnswerId) {
        super(ShowUserMessage.class, waitingAnswerId);
        this.user = user;
    }
}
