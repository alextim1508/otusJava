package com.alextim.messageSystem;

import com.alextim.messageSystem.message.Message;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;

@NoArgsConstructor
@Component
public class MessageSystem {

    private final List<Thread> workers = new ArrayList<>();
    private final Map<Address, LinkedBlockingQueue<Message>> messagesQueues = new HashMap<>();
    private final Map<Address, MessageSystemClient> clients = new HashMap<>();


    public void addMessageSystemClient(MessageSystemClient clint) {
        clients.put(clint.getAddress(), clint);
        messagesQueues.put(clint.getAddress(), new LinkedBlockingQueue<>());
    }

    public void sendMessage(Message message) {
        messagesQueues.get(message.getTo()).add(message);
    }

    public void start() {
        for (Map.Entry<Address, MessageSystemClient> entry : clients.entrySet()) {
            String name = "MessageSystem-worker-" + entry.getKey().getId();
            Thread thread = new Thread(() -> {
                while (true) {
                    LinkedBlockingQueue<Message> queue = messagesQueues.get(entry.getKey());
                    Message message = null;
                    try {
                        message = queue.take();
                        message.exec(entry.getValue());
                    }
                    catch (InterruptedException e) {
                        return;
                    }
                    catch (RuntimeException e) {
                        message.setException(e);
                        message.errorHandler(entry.getValue());
                    }
                }
            });
            thread.setName(name);
            thread.start();
            workers.add(thread);
        }
    }

    public void dispose() {
        workers.forEach(Thread::interrupt);
    }

}
