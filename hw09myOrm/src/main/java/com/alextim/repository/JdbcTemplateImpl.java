package com.alextim.repository;

import com.alextim.executor.DbExecutor;
import com.alextim.executor.DbExecutorImpl;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class JdbcTemplateImpl<T> implements JdbcTemplate<T> {

    private static final String CREATE_TABLE = "create table %s (%s);";
    private static final String INSERT = "insert into %s (%s) values (%s);";
    private static final String GET_BY_ID = "select * from %s where id = ?";
    private static final String UPDATE = "update %s set %s where id = ?;";

    private final DbExecutor<T> executor;

    public JdbcTemplateImpl(Connection connection) {
        executor = new DbExecutorImpl<>(connection);
    }

    @Override
    public void createTable(Class cl) throws SQLException {
        StringBuilder param = new StringBuilder();

        Field fieldId = null;
        String separator = "";
        for(Field field: cl.getDeclaredFields()) {
            param.append(separator).append(field.getName()).append(" ").append(toSqlType(field.getType()));
            if(field.isAnnotationPresent(Id.class)) {
                if(!field.getType().equals(long.class))
                    throw new RuntimeException("Filed with @Id must be long type");
                fieldId = field;
                param.append(" not null auto_increment");
            }
            separator = ", ";
        }

        if(fieldId == null)
            throw new RuntimeException(cl.getName() + "does not contain a field with @Id");

        param.append(separator).append(String.format(" primary key (%s)", fieldId.getName()));

        String sql = String.format(CREATE_TABLE, cl.getSimpleName(), param.toString());
        System.out.println("sql = " + sql);

        executor.execute(sql, Collections.emptyList());
    }

    @Override
    public long create(T objectData) throws SQLException {
        Class<?> cl = objectData.getClass();
        StringBuilder args = new StringBuilder();
        StringBuilder param = new StringBuilder();

        Field fieldId = null;
        String separator = "";
        List<String> values = new ArrayList<>();
        try {
            for(Field field: cl.getDeclaredFields()) {
                if(!field.isAnnotationPresent(Id.class)) {
                    field.setAccessible(true);

                    args.append(separator).append(field.getName());
                    param.append(separator).append("?");
                    values.add(field.get(objectData).toString());

                    field.setAccessible(false);
                    separator = ", ";
                }
                else {
                    fieldId = field;
                }

            }
        }
        catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        if(fieldId == null)
            throw new RuntimeException(cl.getName() + "does not contain a field with @Id");

        String sql = String.format(INSERT, cl.getSimpleName(), args, param);
        System.out.println("sql = " + sql);

       return executor.executeUpdate(sql, values);
    }

    @Override
    public T load(long id, Class<T> cl) throws SQLException {
        String sql = String.format(GET_BY_ID, cl.getSimpleName());
        System.out.println("sql = " + sql);
        Optional<T> result = executor.executeQuery(sql, id, resultSet -> createObjectByResultSet(cl, resultSet));

        if(!result.isPresent())
            throw new RuntimeException("Empty result!");

        return result.get();
    }

    @Override
    public void update(T objectData) throws SQLException {
        Class<?> cl = objectData.getClass();
        StringBuilder args = new StringBuilder();

        String separator = "";
        long id = -1;
        List<String> param = new ArrayList<>();
        try {
            for(Field field: cl.getDeclaredFields()) {
                field.setAccessible(true);
                if(!field.isAnnotationPresent(Id.class)) {

                    args.append(separator).append(field.getName()).append("= ?");
                    param.add(field.get(objectData).toString());
                    separator = ", ";
                }
                else {
                    id = (long)field.get(objectData);
                }
                field.setAccessible(false);
            }
        }
        catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        param.add(String.valueOf(id));
        String sql = String.format(UPDATE, cl.getSimpleName(), args);
        System.out.println("sql = " + sql);

        executor.execute(sql, param);
    }

    private T createObjectByResultSet(Class<T> cl, ResultSet resultSet) {
        T instance = null;
        try {
            if(resultSet.next()) {
                instance = cl.newInstance();
                for (Field field : cl.getDeclaredFields()) {
                    field.setAccessible(true);
                    if (field.getType().equals(boolean.class)) {
                        field.set(instance, resultSet.getBoolean(field.getName()));
                    }
                    else if (field.getType().equals(byte.class)) {
                        field.set(instance, resultSet.getByte(field.getName()));
                    }
                    else if (field.getType().equals(short.class)) {
                        field.set(instance, resultSet.getShort(field.getName()));
                    }
                    else if (field.getType().equals(int.class)) {
                        field.set(instance, resultSet.getInt(field.getName()));
                    }
                    else if (field.getType().equals(long.class)) {
                        field.set(instance, resultSet.getLong(field.getName()));
                    }
                    else if (field.getType().equals(float.class)) {
                        field.set(instance, resultSet.getFloat(field.getName()));
                    }
                    else if (field.getType().equals(double.class)) {
                        field.set(instance, resultSet.getDouble(field.getName()));
                    }
                    else if (field.getType().equals(String.class)) {
                        field.set(instance, resultSet.getString(field.getName()));
                    }
                    field.setAccessible(false);
                }
            }
        }
        catch (IllegalAccessException | SQLException | InstantiationException e) {
            e.printStackTrace();
        }
        return instance;
    }

    private String toSqlType(final Class cl)  {
        if(cl.equals(Boolean.class) || cl.equals(boolean.class))
            return "tinyint";
        else if(cl.equals(Character.class) || cl.equals(char.class))
            return "smallint";
        else if(cl.equals(Short.class) || cl.equals(short.class))
            return "smallint";
        else if(cl.equals(Integer.class) || cl.equals(int.class))
            return "int";
        else if(cl.equals(Long.class) || cl.equals(long.class))
            return "bigint";
        else if(cl.equals(Float.class) || cl.equals(float.class))
            return "float";
        else if(cl.equals(Double.class) || cl.equals(double.class))
            return "number";
        else if(cl.equals(String.class))
            return "varchar(255)";
        else if(cl.isArray())
            return "binary(255)";
        else if(cl.equals(Collection.class) || cl.equals(List.class) || cl.equals(Set.class) || cl.equals(Queue.class))
            return "int";
        else
            throw new RuntimeException("Could not determine type for: " + cl.getName());
    }
}
