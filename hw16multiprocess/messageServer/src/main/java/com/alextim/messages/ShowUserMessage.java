package com.alextim.messages;

import com.alextim.domain.User;
import lombok.Data;

@Data
public class ShowUserMessage extends Message {

    private UserDto user;

    public ShowUserMessage(UserDto user) {
        super(ShowUserMessage.class);
        this.user = user;
    }
}
