package com.alextim;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.*;

public class MySerializer {

    public static String toJson(Object object) throws IllegalAccessException {

        if (object instanceof Number || object instanceof Boolean || object instanceof Character || object instanceof String) {
            return object.toString();
        }
        else if (object instanceof Collection) {
            return appendCollection(object);
        }
        else if (object.getClass().isArray()) {
            return appendArray(object);
        }
        else
            return appendObject(object);
    }

    private static String appendObject(Object object) throws IllegalAccessException {
        StringBuilder builder = new StringBuilder();
        String separator = "";
        Field[] fields = object.getClass().getDeclaredFields();
        builder.append("{");
        for (Field field : fields) {
            field.setAccessible(true);
            builder.append(separator).append("\"" + field.getName() + "\"" + " : " +  toJson(field.get(object)));
            separator = ",";
            field.setAccessible(false);
        }
        return builder.append("}").toString();
    }

    private static String appendArray(Object object) throws IllegalAccessException {
        StringBuilder builder = new StringBuilder("[");
        for (int i = 0; i < Array.getLength(object); i++) {
            builder.append(toJson(Array.get(object, i)));
            if (i != Array.getLength(object)-1) {
                builder.append(",");
            }
        }
        return builder.append("]").toString();
    }

    private static String appendCollection(Object object) throws IllegalAccessException {
        StringBuilder builder = new StringBuilder("[");
        String separator = "";
        for (Object value : (Collection) object) {
            builder.append(separator).append(toJson(value));
            separator = ",";
        }
        return builder.append("]").toString();
    }
}