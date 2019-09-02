package com.alextim.messageSystem.message;

import com.alextim.messageSystem.Address;
import com.alextim.messageSystem.MessageSystemClient;
import com.alextim.service.DBServiceMessageSystemClient;
import com.alextim.service.DBServiceMessageSystemClientImpl;

public abstract class ToDBMsg extends Message {

    public ToDBMsg(Address to) {
        super(to);
    }

    @Override
    public void exec(MessageSystemClient client) {
        if (client instanceof DBServiceMessageSystemClientImpl) {
            exec((DBServiceMessageSystemClientImpl) client);
        }
    }

    @Override
    public void errorHandler(MessageSystemClient client) {
        if (client instanceof DBServiceMessageSystemClientImpl) {
            errorHandler((DBServiceMessageSystemClientImpl) client);
        }
    }

    protected abstract void exec(DBServiceMessageSystemClient client);

    protected abstract void errorHandler(DBServiceMessageSystemClient client);
}