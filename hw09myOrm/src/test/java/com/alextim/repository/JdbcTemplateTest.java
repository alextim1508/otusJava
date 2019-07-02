package com.alextim.repository;

import com.alextim.domain.Account;
import com.alextim.domain.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JdbcTemplateTest {

    private static final String URL = "jdbc:h2:mem:";
    private static Connection connection;

    private static JdbcTemplateImpl<User> jdbcTemplateUser;
    private static JdbcTemplateImpl<Account> jdbcTemplateAccount;

    @BeforeAll
    public static void setUp() throws SQLException {
        connection = DriverManager.getConnection(URL);
        connection.setAutoCommit(false);

        jdbcTemplateUser = new JdbcTemplateImpl<>(connection);
        jdbcTemplateUser.createTable(User.class);

        jdbcTemplateAccount = new JdbcTemplateImpl<>(connection);
        jdbcTemplateAccount.createTable(Account.class);
    }

    @Test
    public void createObjectTest() throws SQLException {
        User user = new User("name", 23);
        long userId = jdbcTemplateUser.create(user);
        User loadedUser = jdbcTemplateUser.load(userId, User.class);
        Assertions.assertEquals(user, loadedUser);

        Account account = new Account("name", 23.9);
        long accountId = jdbcTemplateAccount.create(account);
        Account loadedAccount = jdbcTemplateAccount.load(accountId, Account.class);
        Assertions.assertEquals(account, loadedAccount);
    }


    @Test
    public void updateObjectTest() throws SQLException {
        User user = new User("name", 23);
        long userId = jdbcTemplateUser.create(user);

        User loadedUser = jdbcTemplateUser.load(userId, User.class);
        loadedUser.setName("newName");
        jdbcTemplateUser.update(loadedUser);

        User loadedFromUpdateUser = jdbcTemplateUser.load(userId, User.class);
        Assertions.assertEquals("newName", loadedFromUpdateUser.getName());


        Account account = new Account("name", 23.9);
        long accountId = jdbcTemplateAccount.create(account);

        Account loadedAccount = jdbcTemplateAccount.load(accountId, Account.class);
        loadedAccount.setType("newType");
        jdbcTemplateAccount.update(loadedAccount);

        Account loadedFromUpdateAccount = jdbcTemplateAccount.load(accountId, Account.class);
        Assertions.assertEquals("newType", loadedFromUpdateAccount.getType());
    }
}
