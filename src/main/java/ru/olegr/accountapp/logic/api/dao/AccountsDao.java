package ru.olegr.accountapp.logic.api.dao;

import ru.olegr.accountapp.model.entity.Account;

import java.math.BigDecimal;
import java.util.Set;

/**
 * DAO для работы со счетами
 */
public interface AccountsDao {

    /**
     * Получение множества счётов по множеству их номеров
     * Реализация подразумевает блокировку выбираемых счетов в определённом порядке
     * во избежание деадлоков
     *
     * @param number Номер счёта
     * @return Счёт
     */
    Set<Account> getAccountsByNumbersForUpdate(Set<String> number);

    /**
     * Задаёт баланс у счёта
     *
     * @param accountId Идентификатор счёта, которому нужно задать баланс
     * @param balance Баланс, который нужно задать
     */
    void setBalance(int accountId, BigDecimal balance);
}
