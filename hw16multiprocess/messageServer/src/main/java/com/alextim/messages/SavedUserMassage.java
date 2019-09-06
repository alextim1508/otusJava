package com.alextim.messages;

import lombok.Data;

@Data
public class SavedUserMassage extends Message {

    private UserDto user;

    public SavedUserMassage(UserDto user, String waitingAnswerId) {
        super(SavedUserMassage.class, waitingAnswerId);
        this.user = user;
    }
}