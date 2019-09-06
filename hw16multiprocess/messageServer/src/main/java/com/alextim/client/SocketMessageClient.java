package com.alextim.client;

import com.alextim.messages.Message;
import com.alextim.workers.SocketMessageWorker;
import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Data @Slf4j
public abstract class SocketMessageClient {

    @Parameter(names = "-host")
    public String HOST;

    @Parameter(names = "-port")
    public int PORT;

    @Parameter(names = "-id")
    public String ID;

    private SocketMessageWorker messageWorker;

    private ExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

    public SocketMessageClient(String[] args) throws IOException {
        initFields(this, args);

        messageWorker = new ClientSocketMessageWorker(ID, HOST, PORT);
    }

    private static void initFields(Object object, String[] args) {
        JCommander jCommander = new JCommander(object);
        jCommander.setAcceptUnknownOptions(true);
        try {
            jCommander.parse(args);
        }
        catch (ParameterException e) {
            log.error("jCommander error: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void start() throws IOException {
        messageWorker.init();
        log.info("{}: Start client {} {}", ID, HOST, PORT);

        executorService.submit(() -> {
            while (true){
                Message msg = messageWorker.take();
                Message answer = handlerMessage(msg);
                if(answer != null)
                    messageWorker.send(answer);
            }
        });
    }

    public void sendMessage(Message message) {
        messageWorker.send(message);
    }

    public void stop() throws IOException {
        log.info("shutdown");
        messageWorker.close();
        executorService.shutdown();
    }

    public abstract Message handlerMessage(Message message);
}
