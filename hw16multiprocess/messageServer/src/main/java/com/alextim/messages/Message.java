package com.alextim.messages;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data @NoArgsConstructor
public class Message implements Serializable {

    public static String CLASS_NAME_VARIABLE = "className";

    private String className;

    public Message(Class<?> cl) {
       className = cl.getName();
    }
}