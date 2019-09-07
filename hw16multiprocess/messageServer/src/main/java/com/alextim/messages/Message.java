package com.alextim.messages;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

@Data @NoArgsConstructor
public class Message implements Serializable {

    public static String CLASS_NAME_VARIABLE = "className";

    private String className;

    private String waitingAnswerId;

    public Message(Class<?> cl, String waitingAnswerId) {
       className = cl.getName();
       this.waitingAnswerId = waitingAnswerId;
    }
}