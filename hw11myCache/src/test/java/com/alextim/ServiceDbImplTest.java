package com.alextim;

import com.alextim.cache.Cache;
import com.alextim.cache.CacheElement;
import com.alextim.cache.MyCache;
import com.alextim.domain.Address;
import com.alextim.domain.Phone;
import com.alextim.domain.User;
import com.alextim.repository.UserRepository;
import com.alextim.repository.UserRepositoryImpl;
import com.alextim.service.ServiceDB;
import com.alextim.service.ServiceDbImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


public class ServiceDbImplTest {

    private static UserRepository repo = new UserRepositoryImpl();

    private static Cache<User> cache =
            new MyCache<>(10, TimeUnit.SECONDS.toMillis(1), TimeUnit.SECONDS.toMillis(1));

    private ServiceDB service = new ServiceDbImpl(repo, cache);

    @Test
    public void test() {
        User alex = User.builder()
                .name("Alex")
                .gender(User.Gender.MALE)
                .address(new Address("Nevsky"))
                .phone(new Phone("11"))
                .build();

        service.save(alex);
        service.load(alex.getId());
        verify(repo, times(0)).findById(any(long.class));
    }
}
