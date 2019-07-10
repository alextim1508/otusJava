package com.alextim.repository;

import com.alextim.domain.User;

import java.util.List;

public interface UserRepository {
    void insert(User user);

    long getCount();
    List<User> getAll(int page, int amountByOnePage);

    User findById(long id);
    List<User> findByName(String name);
    User findUserByStreet(String street);
    List<User> getUsersByPhoneNumber(String number);

    void update(long id, User user);

    void delete(User user);

    void deleteAll();
}
