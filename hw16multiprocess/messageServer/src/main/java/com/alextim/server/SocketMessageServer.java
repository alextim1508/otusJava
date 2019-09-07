package com.alextim.server;


import com.alextim.messages.Message;
import com.alextim.workers.MessageWorker;
import com.alextim.workers.SocketMessageWorker;
import lombok.extern.slf4j.Slf4j;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
public abstract class SocketMessageServer implements SocketMessageServerMBean {
    String ID = "Server";

    private static final int THREADS_COUNT = 1;
    private static final int PORT = 5050;
    private static final int DELAY_MS = 500;

    private final ExecutorService executorService;
    private final Map<String, MessageWorker> workers;


    public SocketMessageServer(){
        log.info("Start server");
        executorService = Executors.newFixedThreadPool(THREADS_COUNT);
        workers = new ConcurrentHashMap<>();
    }


    public void start() throws Exception{
        executorService.submit(this::poolMessages);

        try (ServerSocket serverSocket = new ServerSocket(PORT)){
            while(!executorService.isShutdown()){
                Socket socket = serverSocket.accept();
                SocketMessageWorker worker = new SocketMessageWorker(ID, socket);
                worker.init();
                workers.put(worker.getRemoteSocketAddress(), worker);
            }
        }
    }

    public void sendMessageToClientById(Message message, String idClient) {
        if(message != null)
            workers.get(idClient).send(message);
    }

    public void setClientId(String oldId, IdClient newId) {
        MessageWorker worker = workers.remove(oldId);
        if(worker != null)
            workers.put(newId.toString(), worker);
    }

    public abstract Message handlerMessage(Message message, String idClient);


    private void poolMessages(){
        while (true) {
            workers.forEach((idClient, worker) -> {
                Message message = worker.pool();
                if (message != null) {
                    Message handledMessage = handlerMessage(message, idClient);
                    if(handledMessage != null)
                        worker.send(handledMessage);
                }
            });

            try {
                Thread.sleep(DELAY_MS);
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean getRunning(){
        return true;
    }

    @Override
    public void setRunning(boolean running){
        if (!running){
            executorService.shutdown();
        }
    }
}