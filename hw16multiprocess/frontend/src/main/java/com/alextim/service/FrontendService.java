package com.alextim.service;

import com.alextim.domain.User;

import java.util.List;

public interface FrontendService {
    void allUsers(List<User> users);
    void addedUser(User user);
    void errorHandler(Exception exception);
}