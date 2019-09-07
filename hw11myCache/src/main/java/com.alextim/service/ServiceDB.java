package com.alextim.service;

import com.alextim.domain.Address;
import com.alextim.domain.Phone;
import com.alextim.domain.User;

import java.util.List;

public interface ServiceDB extends AutoCloseable{
    User save(User user);
    User load(long id);
    List<User> load(String name);
    List<User> loadAll(int page, int amount);
    List<Phone> getPhone(long id);
    User update(long id, String name, User.Gender gender, Address address, List<Phone> phones);
    void remove(long id);
}
