package com.alextim.messages;

import lombok.Data;

@Data
public class ErrorMessage extends Message {
    private String messageException;

    public ErrorMessage(String messageException, String waitingAnswerId) {
        super(ErrorMessage.class, waitingAnswerId);
        this.messageException = messageException;
    }
}
