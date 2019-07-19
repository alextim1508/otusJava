package com.alextim.service;

import com.alextim.cache.Cache;
import com.alextim.cache.CacheElement;
import com.alextim.cache.CacheKey;
import com.alextim.domain.Address;
import com.alextim.domain.Phone;
import com.alextim.domain.User;
import com.alextim.repository.UserRepository;
import com.alextim.repository.UserRepositoryImpl;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@RequiredArgsConstructor
public class ServiceDbImpl implements ServiceDB {

    private final UserRepository repo;

    private final Cache<User> cache;

    @Override
    public void save(User user) {
        repo.insert(user);
        cache.put(CacheKey.buildKey(user), user);
    }

    @Override
    public User load(long id) {
        User userFromCache = cache.get(CacheKey.buildKey(User.class, id));
        if(userFromCache == null) {
            User userFromRepository = repo.findById(id);
            cache.put(CacheKey.buildKey(userFromRepository), userFromRepository);
            return userFromRepository;
        }
        return userFromCache;
    }

    @Override
    public List<User> load(String name) {
        List<User> users = repo.findByName(name);
        users.forEach(user -> cache.put(CacheKey.buildKey(user), user));
        return users;
    }

    @Override
    public void update(long id, String name, User.Gender gender, Address address, List<Phone> phones) {
        User user = User.builder()
                .name(name)
                .gender(gender)
                .address(address)
                .phones(phones)
                .build();

        cache.remove(CacheKey.buildKey(User.class, id));
        repo.update(id, user);
        cache.put(CacheKey.buildKey(user), user);
    }

    @Override
    public void remove(long id) {
        repo.delete(repo.findById(id));
        cache.remove(CacheKey.buildKey(User.class, id));
    }

    @Override
    public void close() throws Exception {
        repo.close();
        cache.dispose();
    }
}
