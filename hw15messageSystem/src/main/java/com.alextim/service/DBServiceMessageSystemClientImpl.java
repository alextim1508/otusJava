package com.alextim.service;

import com.alextim.cache.Cache;
import com.alextim.domain.User;
import com.alextim.messageSystem.Address;
import com.alextim.messageSystem.MessageSystem;
import com.alextim.repository.UserRepository;
import lombok.Getter;
import org.springframework.stereotype.Service;


@Service
public class DBServiceMessageSystemClientImpl extends ServiceDbImpl implements DBServiceMessageSystemClient {

    @Getter
    private final Address address = new Address("DBService");

    @Getter
    private final MessageSystem messageSystem;

    public DBServiceMessageSystemClientImpl(UserRepository repository, Cache<User> cache, MessageSystem messageSystem) {
        super(repository, cache);
        this.messageSystem = messageSystem;
    }

}
