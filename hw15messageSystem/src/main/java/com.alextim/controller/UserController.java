package com.alextim.controller;

import com.alextim.messageSystem.MessageSystem;
import com.alextim.messageSystem.message.AddUserToDBmsg;
import com.alextim.messageSystem.message.GetUsersToDBMsg;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import javax.validation.Valid;

import static com.alextim.controller.UserDto.toUser;

@Controller
@Slf4j
public class UserController {

    @Autowired
    private MessageSystem messageSystem;

    @MessageMapping("/user")
    @SendTo("/topic/response3")
    public MessageDto putUser(@Valid UserDto userDto) {
        log.info("PutUser: {}", userDto);
        messageSystem.sendMessage(new AddUserToDBmsg(toUser(userDto)));
        return new MessageDto("Ok");
    }

    @MessageMapping("/users")
    @SendTo("/topic/response3")
    public MessageDto getUsers() {
        log.info("GutUsers");
        messageSystem.sendMessage(new GetUsersToDBMsg());
        return new MessageDto("Ok");
    }

    @MessageExceptionHandler(Exception.class)
    @SendTo("/topic/response3")
    public MessageDto errorHandler(Exception ex) {
        log.error("Error: {}", ex.getMessage());
        return new MessageDto("Error");
    }
}
