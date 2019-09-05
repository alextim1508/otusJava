package com.alextim.workers;

import com.alextim.messages.Message;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

@Data @Slf4j
public class SocketMessageWorker implements MessageWorker {

    private static final int WORKER_COUNT = 2;

    private final ExecutorService executorService;
    private final Socket socket;

    private String id;

    private final BlockingQueue<Message> output = new LinkedBlockingQueue<>();
    private final BlockingQueue<Message> input = new LinkedBlockingQueue<>();

    public SocketMessageWorker(String id, Socket socket) {
        this.socket = socket;
        this.id = id;
        log.info("New SocketMessageWorker id: {}", id);
        executorService = Executors.newFixedThreadPool(WORKER_COUNT);
    }

    public void init() {
        executorService.execute(this::sendMessage);
        executorService.execute(this::receiveMessage);
    }

    @Override
    public String getRemoteSocketAddress() {
        return socket.getRemoteSocketAddress().toString();
    }

    @Override
    public Message pool() {
        Message message = input.poll();
        if(message != null)
            log.info("{}: Receive message {}", id, message);
        return message;
    }

    @Override
    public void send(Message message) {
        log.info("{}: Send message {}", id, message);
        output.add(message);
    }

    @Override
    public Message take() throws InterruptedException {
        Message message = input.take();
        log.info("{}: Receive message {}", id, message);
        return message;
    }

    @Override
    public void close() throws IOException {
        socket.close();
        executorService.shutdown();
    }

    private void sendMessage(){
        try (PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {
            while (socket.isConnected()){
                Message message = output.take();
                String json = getJsonFromMessage(message);
                out.println(json);
                out.println();
            }
        }
        catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void receiveMessage(){
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            String inputLine;
            StringBuilder stringBuilder = new StringBuilder();
            while ((inputLine = reader.readLine()) != null) {
                stringBuilder.append(inputLine);
                if (inputLine.isEmpty()){
                    String json = stringBuilder.toString();
                    Message message = getMessageFromJson(json);
                    input.add(message);
                    stringBuilder = new StringBuilder();
                }
            }
        }
        catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private Message getMessageFromJson(String json) throws ClassNotFoundException {
        JsonParser parser = new JsonParser();
        JsonObject jsonObject = (JsonObject) parser.parse(json);
        Class<?> cl = Class.forName(jsonObject.get(Message.CLASS_NAME_VARIABLE).getAsString());

        return (Message) new Gson().fromJson(json, cl);
    }

    private String getJsonFromMessage(Message message) {
        return new Gson().toJson(message);
    }
}