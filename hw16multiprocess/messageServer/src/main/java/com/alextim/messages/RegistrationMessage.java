package com.alextim.messages;

import lombok.Data;

@Data
public class RegistrationMessage extends Message {

    private String id;

    public RegistrationMessage(String id) {
        super(RegistrationMessage.class);
        this.id = id.toString();
    }
}
