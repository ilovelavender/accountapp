package ru.olegr.accountapp.model.exception;

/**
 * Исключение, когда на счёте недостаточно средств
 */
public class InsufficientBalanceException extends RuntimeException {

    private static final String MESSAGE_FORMAT = "Not enough funds for transaction on account - %s";

    /**
     * @param accountNumber номер счёта, на котором недостаточно средств
     */
    public InsufficientBalanceException(String accountNumber) {
        super(String.format(MESSAGE_FORMAT, accountNumber));
    }
}
