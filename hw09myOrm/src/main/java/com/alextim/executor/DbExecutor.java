package com.alextim.executor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public interface DbExecutor<T> {
    void execute(String sql, List<String> params) throws SQLException;
    long executeUpdate(String sql, List<String> params) throws SQLException;
    Optional<T> executeQuery(String sql, long id, Function<ResultSet, T> handler) throws SQLException;
}