package com.alextim.messageSystem.message;

import com.alextim.domain.User;
import com.alextim.messageSystem.Address;
import com.alextim.service.FrontendServiceMessageSystemClient;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class ShowAllUsersToFrontendMsg extends ToFrontendMsg {

    private final List<User> users;

    @Setter
    private Exception exception;

    public ShowAllUsersToFrontendMsg(List<User> users) {
        super(new Address("Frontend"));
        this.users = users;
    }

    @Override
    protected void exec(FrontendServiceMessageSystemClient frontendService) {
        log.debug("exec: {}", users);
        frontendService.allUsers(users);
    }

    @Override
    protected void errorHandler(FrontendServiceMessageSystemClient client) {
        log.info("error:");
        client.getMessageSystem().sendMessage(new ErrorToFrontendMsg(exception));
    }

}
