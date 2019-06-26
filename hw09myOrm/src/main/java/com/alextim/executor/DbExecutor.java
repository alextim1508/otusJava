package com.alextim.executor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.function.Function;

public interface DbExecutor<T> {
    void execute(String sql) throws SQLException;
    long executeUpdate(String sql) throws SQLException;
    Optional<T> executeQuery(String sql, Function<ResultSet, T> rsHandler) throws SQLException;
}