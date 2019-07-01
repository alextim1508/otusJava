package com.alextim.executor;

import java.sql.*;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class DbExecutorImpl<T> implements DbExecutor<T> {

    private final Connection connection;

    public DbExecutorImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void execute(String sql, List<String> params) throws SQLException {
        try(PreparedStatement pst = connection.prepareStatement(sql)) {
            for(int idx = 0; idx < params.size(); idx++) {
                pst.setString(idx + 1, params.get(idx));
            }
            pst.executeUpdate();
        }
    }

    @Override
    public long executeUpdate(String sql, List<String> params) throws SQLException {
        Savepoint savePoint = connection.setSavepoint("savePointName");
        try (PreparedStatement pst = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            for(int idx = 0; idx < params.size(); idx++) {
                pst.setString(idx + 1, params.get(idx));
            }
            pst.executeUpdate();
            try (ResultSet rs = pst.getGeneratedKeys()) {
                rs.next();
                return rs.getInt(1);
            }
        }
        catch (SQLException ex) {
            connection.rollback(savePoint);
            throw ex;
        }
    }

    @Override
    public Optional<T> executeQuery(String sql, long id, Function<ResultSet, T> handler) throws SQLException {
        try (PreparedStatement pst = connection.prepareStatement(sql)) {
            pst.setLong(1, id);
            try (ResultSet rs = pst.executeQuery()) {
                return Optional.ofNullable(handler.apply(rs));
            }
        }
    }
}