package ru.olegr.accountapp.model.validation;

public interface ValidationConstants {

    /**
     * Длина номера счёта
     */
    int ACCOUNT_NUMBER_LENGTH = 20;

    /**
     * Максимальное количество знаков "после запятой" в размере транзакции
     */
    int MAX_AMOUNT_SCALE = 2;
}
