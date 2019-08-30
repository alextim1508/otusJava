package com.alextim.messageSystem.message;

import com.alextim.domain.User;
import com.alextim.messageSystem.Address;
import com.alextim.service.DBServiceMessageSystemClient;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AddUserToDBmsg extends ToDBMsg {

    private final User user;

    @Setter
    private Exception exception;

    public AddUserToDBmsg(User user) {
        super(new Address("DBService"));
        this.user = user;
    }

    @Override
    protected void exec(DBServiceMessageSystemClient client) {
        log.info("exec: {}", user);
        client.save(user);
        client.getMessageSystem().sendMessage(new ShowAddedUserToFrontendMsg(user));
    }

    @Override
    protected void errorHandler(DBServiceMessageSystemClient client) {
        log.info("error:");
        client.getMessageSystem().sendMessage(new ErrorToFrontendMsg(exception));
    }
}
