package com.alextim.messages;

import com.alextim.domain.User;
import lombok.Data;

@Data
public class ShowUserMessage extends Message {

    private UserDto user;

    public ShowUserMessage(UserDto user, String waitingAnswerId) {
        super(ShowUserMessage.class, waitingAnswerId);
        this.user = user;
    }
}
