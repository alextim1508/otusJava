package com.alextim.service;

import com.alextim.domain.Address;
import com.alextim.domain.Phone;
import com.alextim.domain.User;

import java.util.List;

public interface ServiceDB extends AutoCloseable{
    void save(User user);
    User load(long id);
    List<User> load(String name);
    void update(long id, String name, User.Gender gender, Address address, List<Phone> phones);
    void remove(long id);
}
