package com.alextim.controller;

import com.alextim.client.SocketMessageClient;
import com.alextim.domain.User;
import com.alextim.messages.GetAllUsersMessage;
import com.alextim.messages.SaveUserMassage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor @Slf4j
public class UserController {

    private final SocketMessageClient client;

    @MessageMapping("/user")
    @SendTo("/topic/response3")
    public MessageDto putUser(@Valid UserDto userDto) {
        log.info("PutUser: {}", userDto);
        User user = UserDto.toUser(userDto);
        com.alextim.messages.UserDto userDtoForMessage
                = com.alextim.messages.UserDto.toUserDto(user);

        client.sendMessage(new SaveUserMassage(userDtoForMessage, client.getID()));
        return new MessageDto("Ok");
    }

    @MessageMapping("/users")
    @SendTo("/topic/response3")
    public MessageDto getUsers() {
        log.info("GetUsers");
        client.sendMessage(new GetAllUsersMessage(client.getID()));
        return new MessageDto("Ok");
    }

    @MessageExceptionHandler(Exception.class)
    @SendTo("/topic/response3")
    public MessageDto errorHandler(Exception ex) {
        log.error("Error: {}", ex.getMessage());
        return new MessageDto("Error");
    }
}
