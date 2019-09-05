package com.alextim.messages;

import lombok.Data;

import java.util.List;

@Data
public class UsersMessage extends Message {
    private List<UserDto> users;

    public UsersMessage(List<UserDto> users) {
        super(UsersMessage.class);
        this.users = users;
    }
}
