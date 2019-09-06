package com.alextim.messages;

import lombok.Data;

@Data
public class RegistrationMessage extends Message {

    public RegistrationMessage(String id) {
        super(RegistrationMessage.class, id);
    }
}
