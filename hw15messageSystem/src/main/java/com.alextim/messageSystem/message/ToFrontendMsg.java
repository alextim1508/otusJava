package com.alextim.messageSystem.message;

import com.alextim.messageSystem.Address;
import com.alextim.messageSystem.MessageSystemClient;
import com.alextim.service.FrontendServiceMessageSystemClient;

public abstract class ToFrontendMsg extends Message {

    public ToFrontendMsg(Address to) {
        super(to);
    }

    @Override
    public void exec(MessageSystemClient client) {
        if (client instanceof FrontendServiceMessageSystemClient) {
            exec((FrontendServiceMessageSystemClient) client);
        }
    }

    @Override
    public void errorHandler(MessageSystemClient client) {
        if (client instanceof FrontendServiceMessageSystemClient) {
            errorHandler((FrontendServiceMessageSystemClient) client);
        }
    }

    protected abstract void exec(FrontendServiceMessageSystemClient frontendService);

    protected abstract void errorHandler(FrontendServiceMessageSystemClient client);
}