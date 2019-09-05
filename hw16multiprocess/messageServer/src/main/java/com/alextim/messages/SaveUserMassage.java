package com.alextim.messages;

import lombok.Data;

@Data
public class SaveUserMassage extends Message {
    private UserDto user;

    public SaveUserMassage(UserDto user) {
        super(SaveUserMassage.class);
        this.user = user;
    }
}
