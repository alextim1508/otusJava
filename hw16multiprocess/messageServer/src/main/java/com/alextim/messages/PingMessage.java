package com.alextim.messages;

import lombok.Data;

@Data
public class PingMessage extends Message {

    private long time;

    public PingMessage() {
        super(PingMessage.class);
        time = System.currentTimeMillis();
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("PingMessage {");
        sb.append("time = ").append(time).append(" }");
        return sb.toString();
    }
}