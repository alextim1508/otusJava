package com.alextim.repository;

import java.sql.SQLException;

public interface JdbcTemplate<T> {
    void createTable(Class<?> cl) throws SQLException;
    long create(T objectData) throws SQLException;
    void update(T objectData) throws SQLException;
    T load(long id, Class<T> cl) throws SQLException;
}
