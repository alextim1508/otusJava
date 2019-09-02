package com.alextim;

import com.alextim.messageSystem.MessageSystem;
import com.alextim.messageSystem.MessageSystemClient;
import com.alextim.service.FrontendServiceMessageSystemClient;
import com.alextim.service.ServiceDB;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(Main.class, args);

        MessageSystem messageSystem = context.getBean(MessageSystem.class);
        messageSystem.addMessageSystemClient(context.getBean(FrontendServiceMessageSystemClient.class));
        messageSystem.addMessageSystemClient((MessageSystemClient)context.getBean(ServiceDB.class));

        messageSystem.start();
    }
}
