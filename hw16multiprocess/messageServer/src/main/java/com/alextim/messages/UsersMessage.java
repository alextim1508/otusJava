package com.alextim.messages;

import lombok.Data;

import java.util.List;

@Data
public class UsersMessage extends Message {
    private List<UserDto> users;

    public UsersMessage(List<UserDto> users, String waitingAnswerId) {
        super(UsersMessage.class, waitingAnswerId);
        this.users = users;
    }
}
