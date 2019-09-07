package com.alextim;

import com.alextim.client.SocketMessageClient;
import com.alextim.messages.RegistrationMessage;
import com.alextim.service.ServiceDB;
import com.alextim.socket.DBServiceSocketMessageClient;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

import java.io.IOException;

@SpringBootApplication
public class Application {

    public static void main(String[] args ) throws IOException {
        ConfigurableApplicationContext context = SpringApplication.run(Application.class, args);

        SocketMessageClient client = context.getBean(SocketMessageClient.class);

        client.start();
        client.sendMessage(new RegistrationMessage(client.getID()));
    }

    @Bean
    public SocketMessageClient getSocketMessageClient(ServiceDB serviceDB, ApplicationArguments arguments) throws IOException {
        return new DBServiceSocketMessageClient(serviceDB, arguments.getSourceArgs());
    }
}
