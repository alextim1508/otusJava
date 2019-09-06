package com.alextim.messages;

import lombok.Data;

@Data
public class SaveUserMassage extends Message {
    private UserDto user;

    public SaveUserMassage(UserDto user, String waitingAnswerId) {
        super(SaveUserMassage.class, waitingAnswerId);
        this.user = user;
    }
}
