package ru.olegr.accountapp.model.dto;

import java.util.Date;
import java.util.List;

/**
 * Детали ошибок валидации
 */
public class ArgumentNotValidDetails implements DtoModel {

    /**
     * Сообщение по умолчанию для поля error
     */
    private static final String DEFAULT_ERROR = "Bad Request";

    /**
     * Путь api, по которому выдана ошибка (для унификации)
     */
    private final String path;

    /**
     * Текущее время
     */
    private final Date timestamp;

    /**
     * Сообщение исключения
     */
    private final String message;

    /**
     * Детали ошибок валидации
     */
    private final List<FieldError> details;

    public ArgumentNotValidDetails(String path, Date timestamp, String message, List<FieldError> details) {
        this.path = path;
        this.timestamp = timestamp;
        this.message = message;
        this.details = details;
    }

    public String getError() {
        return DEFAULT_ERROR;
    }

    public String getPath() {
        return path;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public String getMessage() {
        return message;
    }

    public List<FieldError> getDetails() {
        return details;
    }
}
