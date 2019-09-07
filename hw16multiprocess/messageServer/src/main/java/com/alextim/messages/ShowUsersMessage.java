package com.alextim.messages;

import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data @ToString(callSuper = true)
public class ShowUsersMessage extends Message{

    private List<UserDto> users;

    public ShowUsersMessage(List<UserDto> users, String waitingAnswerId) {
        super(ShowUsersMessage.class, waitingAnswerId);
        this.users = users;
    }
}
