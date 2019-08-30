package com.alextim.messageSystem.message;

import com.alextim.domain.User;
import com.alextim.messageSystem.Address;
import com.alextim.service.FrontendServiceMessageSystemClient;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ShowAddedUserToFrontendMsg extends ToFrontendMsg {

    private final User user;

    @Setter
    private Exception exception;

    public ShowAddedUserToFrontendMsg(User user) {
        super(new Address("Frontend"));
        this.user = user;
    }

    @Override
    protected void exec(FrontendServiceMessageSystemClient frontendService) {
        log.debug("exec: {}", user);
        frontendService.addedUser(user);
    }

    @Override
    protected void errorHandler(FrontendServiceMessageSystemClient client) {
        log.info("error:");
        client.getMessageSystem().sendMessage(new ErrorToFrontendMsg(exception));
    }

}