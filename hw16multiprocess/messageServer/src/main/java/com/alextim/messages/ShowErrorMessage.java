package com.alextim.messages;

import lombok.Data;

@Data
public class ShowErrorMessage extends Message {
    private String messageException;

    public ShowErrorMessage(String messageException) {
        super(ShowErrorMessage.class);
        this.messageException = messageException;
    }
}
