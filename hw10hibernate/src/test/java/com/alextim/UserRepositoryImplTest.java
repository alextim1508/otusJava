package com.alextim;


import com.alextim.domain.Address;
import com.alextim.domain.Phone;
import com.alextim.domain.User;
import com.alextim.repository.UserRepository;
import com.alextim.repository.UserRepositoryImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class UserRepositoryImplTest {

    private static UserRepository repo = new UserRepositoryImpl();

    @Test
    public void createUserTest() {
        User user = User.builder()
                .name("Ivan")
                .gender(User.Gender.MALE)
                .address(new Address("Address"))
                .phone(new Phone("num1"))
                .phone(new Phone("num2"))
                .build();
        repo.insert(user);

        User byId = repo.findById(user.getId());

        Assertions.assertEquals(user, byId);

        repo.delete(byId.getId());
    }


    @Test
    public void getAllUsersTest() {
        User ivan = User.builder()
                .name("Ivan")
                .address(new Address("Address"))
                .gender(User.Gender.MALE)
                .phone(new Phone("num1"))
                .phone(new Phone("num2"))
                .build();
        repo.insert(ivan);

        User alex = User.builder()
                .name("Alex")
                .address(new Address("Address"))
                .gender(User.Gender.MALE)
                .phone(new Phone("num1"))
                .phone(new Phone("num2"))
                .build();
        repo.insert(alex);

        List<User> all = repo.getAll(0, 10);

        Assertions.assertEquals(2, all.size());
        Assertions.assertTrue(all.contains(ivan));
        Assertions.assertTrue(all.contains(alex));

        repo.delete(ivan.getId());
        repo.delete(alex.getId());
    }

    @Test
    public void getCountUsersTest() {
        User ivan = User.builder()
                .name("Ivan")
                .address(new Address("Address"))
                .gender(User.Gender.MALE)
                .phone(new Phone("num1"))
                .phone(new Phone("num2"))
                .build();
        repo.insert(ivan);

        User alex = User.builder()
                .name("Alex")
                .address(new Address("Address"))
                .gender(User.Gender.MALE)
                .phone(new Phone("num1"))
                .phone(new Phone("num2"))
                .build();
        repo.insert(alex);

        long count = repo.getCount();

        Assertions.assertEquals(2, count);

        repo.delete(ivan.getId());
        repo.delete(alex.getId());
    }


    @Test
    public void updateUserTest() {
        User ivan = User.builder()
                .name("Ivan")
                .address(new Address("Address"))
                .gender(User.Gender.MALE)
                .phone(new Phone("num1"))
                .phone(new Phone("num2"))
                .build();
        repo.insert(ivan);

        ivan.setName("Kirill");
        repo.update(ivan.getId(), ivan);

        Assertions.assertEquals("Kirill", repo.findById(ivan.getId()).getName());
        repo.delete(ivan.getId());
    }

}
