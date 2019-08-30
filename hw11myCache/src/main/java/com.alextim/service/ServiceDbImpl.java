package com.alextim.service;

import com.alextim.cache.Cache;
import com.alextim.cache.CacheKey;
import com.alextim.domain.Address;
import com.alextim.domain.Phone;
import com.alextim.domain.User;
import com.alextim.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import static com.alextim.service.HandlerException.handlerException;

@Slf4j
@RequiredArgsConstructor
public class ServiceDbImpl implements ServiceDB {

    private final UserRepository repo;

    private final Cache<User> cache;

    @Override
    public void save(User user) {
        log.info("Save: {}", user);
        try {
            repo.insert(user);
            cache.put(CacheKey.buildKey(user), user);
        }
        catch(Exception exception) {
            handlerException(exception, user.toString());
        }
    }

    @Override
    public User load(long id) {
        log.info("Load by id: {}", id);
        User userFromCache = cache.get(CacheKey.buildKey(User.class, id));
        if(userFromCache == null) {
            User userFromRepository = repo.findById(id);
            cache.put(CacheKey.buildKey(userFromRepository), userFromRepository);
            return userFromRepository;
        }
        return userFromCache;
    }

    @Override
    public List<Phone> getPhone(long id) {
        log.info("Get phone by user id: {}", id);
        return repo.getPhonesByUserId(id);
    }

    @Override
    public List<User> load(String name) {
        log.info("Load by name: {}", name);
        List<User> users = repo.findByName(name);
        users.forEach(user -> cache.put(CacheKey.buildKey(user), user));
        return users;
    }

    @Override
    public List<User> loadAll(int page, int amount) {
        log.info("Load all");
        List<User> users = repo.getAll(page, amount);
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
