package ru.olegr.accountapp.model.dto;

/**
 * Ошибка валидации поля
 */
public class FieldError implements DtoModel {

    /**
     * Сообщение ошибки валидации
     */
    private final String message;

    /**
     * Название поля
     */
    private final String field;

    /**
     * Значение, валидация которого вызвала ошибку
     */
    private final Object rejectedValue;

    public FieldError(String message, String field, Object rejectedValue) {
        this.message = message;
        this.field = field;
        this.rejectedValue = rejectedValue;
    }

    public String getMessage() {
        return message;
    }

    public String getField() {
        return field;
    }

    public Object getRejectedValue() {
        return rejectedValue;
    }
}
