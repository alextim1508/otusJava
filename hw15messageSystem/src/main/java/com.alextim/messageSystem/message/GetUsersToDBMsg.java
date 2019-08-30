package com.alextim.messageSystem.message;

import com.alextim.domain.User;
import com.alextim.messageSystem.Address;
import com.alextim.service.DBServiceMessageSystemClient;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.function.Function;

@Slf4j
public class GetUsersToDBMsg extends ToDBMsg {

    @Setter
    private Exception exception;

    public GetUsersToDBMsg() {
        super(new Address("DBService"));
    }

    @Override
    protected void exec(DBServiceMessageSystemClient client) {
        log.debug("exec");
        List<User> users = client.loadAll(0, Integer.MAX_VALUE);
        users.forEach((user) -> user.setPhones(client.getPhone(user.getId())));

        client.getMessageSystem().sendMessage(new ShowAllUsersToFrontendMsg(users));
    }

    @Override
    protected void errorHandler(DBServiceMessageSystemClient client) {
        log.info("error:");
        client.getMessageSystem().sendMessage(new ErrorToFrontendMsg(exception));
    }
}
