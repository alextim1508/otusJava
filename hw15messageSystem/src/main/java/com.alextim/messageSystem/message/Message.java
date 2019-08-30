package com.alextim.messageSystem.message;

import com.alextim.messageSystem.Address;
import com.alextim.messageSystem.MessageSystemClient;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data @AllArgsConstructor
public abstract class Message {

    private final Address to;

    public abstract void exec(MessageSystemClient client);
    public abstract void setException(Exception exception);
    public abstract void errorHandler(MessageSystemClient client);
}