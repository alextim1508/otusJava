package com.alextim.messages;

import lombok.Data;
import lombok.ToString;

@Data @ToString(callSuper = true)
public class SaveUserMassage extends Message {
    private UserDto user;

    public SaveUserMassage(UserDto user, String waitingAnswerId) {
        super(SaveUserMassage.class, waitingAnswerId);
        this.user = user;
    }
}
