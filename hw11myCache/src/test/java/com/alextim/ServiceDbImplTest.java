package com.alextim;

import com.alextim.cache.Cache;
import com.alextim.cache.MyCache;
import com.alextim.domain.Address;
import com.alextim.domain.Phone;
import com.alextim.domain.User;
import com.alextim.repository.UserRepository;
import com.alextim.repository.UserRepositoryImpl;
import com.alextim.service.ServiceDB;
import com.alextim.service.ServiceDbImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;


public class ServiceDbImplTest {

    private static UserRepository repo = mock(UserRepositoryImpl.class);

    private static Cache<User> cache =
            new MyCache<>(10, TimeUnit.SECONDS.toMillis(1), TimeUnit.SECONDS.toMillis(1));

    private ServiceDB service = new ServiceDbImpl(repo, cache);


    @BeforeEach
    void setUp() {
        doNothing().when(repo).insert(any(User.class));
    }

    @Test
    public void test() {
        User alex = User.builder()
                .name("Alex")
                .gender(User.Gender.MALE)
                .address(new Address("Nevsky"))
                .phone(new Phone("11"))
                .build();

        service.save(alex);
        User loaded = service.load(alex.getId());

        verify(repo, times(0)).findById(any(long.class));
        Assertions.assertEquals(alex, loaded);
    }
}
