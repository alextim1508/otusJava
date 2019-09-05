package com.alextim;

import com.alextim.client.SocketMessageClient;
import com.alextim.messages.Message;
import com.alextim.messages.PingMessage;
import com.alextim.messages.RegistrationMessage;
import com.alextim.server.IdClient;
import com.alextim.service.FrontendService;
import com.alextim.service.FrontendServiceImpl;
import com.alextim.socket.FrontendSocketMessageClient;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

import java.io.IOException;

@SpringBootApplication
public class Application {
    public static void main(String[] args) throws IOException {
        ConfigurableApplicationContext context = SpringApplication.run(Application.class, args);

        SocketMessageClient client = context.getBean(SocketMessageClient.class);

        client.start();
        client.sendMessage(new RegistrationMessage(client.getID()));
    }

    @Bean
    public SocketMessageClient getSocketMessageClient(FrontendService frontendService, ApplicationArguments arguments) throws IOException {
        return new FrontendSocketMessageClient(frontendService, arguments.getSourceArgs());
    }
}