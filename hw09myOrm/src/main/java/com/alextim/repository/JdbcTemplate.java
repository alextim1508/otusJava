package com.alextim.repository;

import java.sql.SQLException;

public interface JdbcTemplate<T> {
    void createTable(Class<?> cl) throws IllegalAccessException, InstantiationException, SQLException;
    long create(T objectData) throws IllegalAccessException, InstantiationException, SQLException;
    void update(T objectData) throws SQLException, IllegalAccessException;
    T load(long id, Class<T> cl) throws SQLException;
}
