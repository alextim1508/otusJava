package com.alextim.messages;

import com.alextim.domain.User;
import lombok.Data;

@Data
public class SavedUserMassage extends Message {

    private UserDto user;

    public SavedUserMassage(UserDto user) {
        super(SavedUserMassage.class);
        this.user = user;
    }
}