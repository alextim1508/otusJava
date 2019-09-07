package com.alextim.messages;

import lombok.Data;
import lombok.ToString;

@Data @ToString(callSuper = true)
public class SavedUserMassage extends Message {

    private UserDto user;

    public SavedUserMassage(UserDto user, String waitingAnswerId) {
        super(SavedUserMassage.class, waitingAnswerId);
        this.user = user;
    }
}