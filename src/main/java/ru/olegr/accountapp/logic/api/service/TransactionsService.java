package ru.olegr.accountapp.logic.api.service;

import ru.olegr.accountapp.model.dto.Transaction;
import ru.olegr.accountapp.model.dto.TransactionResponse;

/**
 * Сервис для переводов между счетами
 */
public interface TransactionsService {

    /**
     * Перевод со счёта на счёт
     *
     * @return Результат выполнения перевода
     */
    TransactionResponse process(Transaction transaction);
}
