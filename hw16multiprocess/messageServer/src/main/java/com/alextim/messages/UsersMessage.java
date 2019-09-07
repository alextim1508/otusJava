package com.alextim.messages;

import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data @ToString(callSuper = true)
public class UsersMessage extends Message {
    private List<UserDto> users;

    public UsersMessage(List<UserDto> users, String waitingAnswerId) {
        super(UsersMessage.class, waitingAnswerId);
        this.users = users;
    }
}
