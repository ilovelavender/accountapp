package ru.olegr.accountapp.logic.api.dao;

import java.math.BigDecimal;

/**
 * DAO для работы с переводами
 */
public interface TransactionsDao {

    /**
     * Сохранение данных о транзакции в БД
     *
     * @param txnId Идентификатор транзакции
     * @param fromAccountId Идентификатор счёта списания
     * @param toAccountId Идентификатор счёта зачисления
     * @param amount Размер транзакции
     */
    void saveTransaction(String txnId, int fromAccountId, int toAccountId, BigDecimal amount);
}
