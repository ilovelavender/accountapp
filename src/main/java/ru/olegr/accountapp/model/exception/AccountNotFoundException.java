package ru.olegr.accountapp.model.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Исключение, когда счёт не найден
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class AccountNotFoundException extends RuntimeException {

    private static final String MESSAGE_FORMAT = "Account not found - %s";

    /**
     * @param accountNumber номер счёта, по которому счёт был не найден
     */
    public AccountNotFoundException(String accountNumber) {
        super(String.format(MESSAGE_FORMAT, accountNumber));
    }
}
