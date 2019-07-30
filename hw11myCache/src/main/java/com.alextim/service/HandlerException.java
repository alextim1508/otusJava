package com.alextim.service;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class HandlerException {

    public static final String ERROR_STRING = "Операция с объектом %s не выполнена";
    public static final String DUPLICATE_ERROR_STRING = "Запись %s существует";
    public static final String FORMAT_ERROR_STRING = "Формат поля объекта %s не корректнен";
    public static final String EMPTY_RESULT_BY_ID_ERROR_STRING = "Объект %s c id %d не найден";
    public static final String ASSOCIATED_ERROR_STRING = "Объект %s не удалить при ссылающего на него %s объектов";

    public static void handlerException(Exception exception, String object) {
        String message = exception.getCause().getCause().getMessage();

        if(message.contains("Нарушение уникального индекса или первичного ключа"))
            throw new RuntimeException(String.format(DUPLICATE_ERROR_STRING, object));
        else if(message.contains("Значение слишком длинное для поля"))
            throw new RuntimeException(String.format(FORMAT_ERROR_STRING, object));
        else
            throw new RuntimeException(String.format(ERROR_STRING, object));
    }
}
