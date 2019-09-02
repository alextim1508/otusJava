package com.alextim.service;

import com.alextim.domain.User;
import com.alextim.messageSystem.MessageSystemClient;

import java.util.List;

public interface FrontendServiceMessageSystemClient extends MessageSystemClient {
    void allUsers(List<User> users);
    void addedUser(User user);
    void errorHandler(Exception exception);
}