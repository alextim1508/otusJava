package com.alextim.messages;

import lombok.Data;
import lombok.ToString;

@Data @ToString(callSuper = true)
public class RegistrationMessage extends Message {

    public RegistrationMessage(String id) {
        super(RegistrationMessage.class, id);
    }
}
