package com.alextim.workers;

import com.alextim.messages.Message;

import java.io.IOException;

public interface MessageWorker {

    String getId();
    void setId(String id);

    Message pool();

    void send(Message message);

    Message take() throws InterruptedException;

    void close() throws IOException;

    String getRemoteSocketAddress();
}