package com.alextim.client;

import com.alextim.workers.SocketMessageWorker;

import java.io.IOException;
import java.net.Socket;

public class ClientSocketMessageWorker extends SocketMessageWorker {

    public ClientSocketMessageWorker(String address, String host, int port) throws IOException {
        super(address, new Socket(host, port));
    }
}
