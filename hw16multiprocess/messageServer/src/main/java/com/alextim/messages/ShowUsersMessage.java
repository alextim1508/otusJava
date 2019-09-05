package com.alextim.messages;

import com.alextim.domain.User;
import lombok.Data;

import java.util.List;

@Data
public class ShowUsersMessage extends Message{

    private List<UserDto> users;

    public ShowUsersMessage(List<UserDto> users) {
        super(ShowUsersMessage.class);
        this.users = users;
    }
}
