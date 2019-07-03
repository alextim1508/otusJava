package com.alextim.repository;

import com.alextim.domain.User;

import java.util.List;

public interface UserRepository {
    void insert(User user);

    long getCount();
    List<User> getAll(int page, int amountByOnePage);

    User findById(long id);

    void update(long id, User user);

    void delete(long id);
}
