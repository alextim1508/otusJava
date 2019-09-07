package com.alextim.messages;

import lombok.Data;
import lombok.ToString;

@Data @ToString(callSuper = true)
public class ShowErrorMessage extends Message {
    private String messageException;

    public ShowErrorMessage(String messageException, String waitingAnswerId) {
        super(ShowErrorMessage.class, waitingAnswerId);
        this.messageException = messageException;
    }
}
