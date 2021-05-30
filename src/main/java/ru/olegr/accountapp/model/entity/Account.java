package ru.olegr.accountapp.model.entity;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Счёт
 */
public class Account {

    /**
     * Идентификатор счёта
     */
    private final int id;

    /**
     * Номер счёта
     */
    private final String number;

    /**
     * Баланс счёта
     */
    private final BigDecimal balance;

    public Account(int id, String number, BigDecimal balance) {
        this.id = id;
        this.number = number;
        this.balance = balance;
    }

    public int getId() {
        return id;
    }

    public String getNumber() {
        return number;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return id == account.id &&
                Objects.equals(number, account.number) &&
                Objects.equals(balance, account.balance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, number, balance);
    }
}
