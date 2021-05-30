package ru.olegr.accountapp.model.exception;

/**
 * Исключение, когда счёт не найден
 */
public class SameAccountException extends RuntimeException {

    private static final String MESSAGE_FORMAT = "Cannot transfer to the same account";

    public SameAccountException() {
        super(MESSAGE_FORMAT);
    }
}
