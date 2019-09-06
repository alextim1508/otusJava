package com.alextim.socket;

import com.alextim.client.SocketMessageClient;
import com.alextim.domain.User;
import com.alextim.messages.*;
import com.alextim.service.FrontendService;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;


@Slf4j
public class FrontendSocketMessageClient extends SocketMessageClient {

    private final FrontendService frontendService;

    public FrontendSocketMessageClient(FrontendService frontendService, String... args) throws IOException {
        super(args);
        this.frontendService = frontendService;
    }

    @Override
    public Message handlerMessage(Message message) {
        if(message instanceof ShowUserMessage) {
            ShowUserMessage showSavedUserMessage = (ShowUserMessage)message;
            User user = com.alextim.messages.UserDto.toUser(showSavedUserMessage.getUser());
            frontendService.addedUser(user);
            return new ShownUserMessage(ID);
        }
        else if(message instanceof ShowUsersMessage) {
            ShowUsersMessage showUsersMessage = (ShowUsersMessage)message;
            List<User> users = showUsersMessage.getUsers().stream().map(com.alextim.messages.UserDto::toUser).collect(Collectors.toList());
            frontendService.allUsers(users);
            return new ShownUserMessage(ID);
        }
        else if(message instanceof ShowErrorMessage) {
            ShowErrorMessage showErrorMessage = (ShowErrorMessage)message;
            frontendService.errorHandler(new RuntimeException(showErrorMessage.getMessageException()));
            return new ShownErrorMessage(ID);
        }
        else if(message instanceof OkMessage) {
            log.info("{}: Message Ok!", ID);
            return null;
        }
        else
            return new UnknownMessage(ID);
    }
}
