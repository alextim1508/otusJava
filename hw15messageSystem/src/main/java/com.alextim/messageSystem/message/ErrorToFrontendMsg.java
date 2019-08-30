package com.alextim.messageSystem.message;

import com.alextim.messageSystem.Address;
import com.alextim.service.FrontendServiceMessageSystemClient;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ErrorToFrontendMsg extends ToFrontendMsg {

    @Setter
    private Exception exception;

    public ErrorToFrontendMsg(Exception exception) {
        super(new Address("Frontend"));
        this.exception = exception;
    }

    @Override
    protected void exec(FrontendServiceMessageSystemClient frontendService) {
        log.debug("exec: {}", exception);
        frontendService.errorHandler(exception);
    }

    @Override
    protected void errorHandler(FrontendServiceMessageSystemClient client) {
    }
}
