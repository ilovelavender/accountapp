package ru.olegr.accountapp.logic.impl.dao;

import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import ru.olegr.accountapp.logic.api.dao.AccountsDao;
import ru.olegr.accountapp.logic.stereotype.Dao;
import ru.olegr.accountapp.model.entity.Account;

import java.math.BigDecimal;
import java.util.Set;

import static ru.olegr.accountapp.model.generated.tables.Accounts.ACCOUNTS;

@Dao
public class AccountsDaoImpl implements AccountsDao {

    private final DSLContext dsl;

    @Autowired
    public AccountsDaoImpl(DSLContext dsl) {
        this.dsl = dsl;
    }

    @Override
    public Set<Account> getAccountsByNumbersForUpdate(Set<String> number) {
        return Set.copyOf(dsl.select(ACCOUNTS.ID, ACCOUNTS.BALANCE, ACCOUNTS.NUMBER)
            .from(ACCOUNTS)
            .where(ACCOUNTS.NUMBER.in(number))
            .orderBy(ACCOUNTS.NUMBER)
            .forUpdate()
            .fetch(r -> new Account(r.get(ACCOUNTS.ID), r.get(ACCOUNTS.NUMBER), r.get(ACCOUNTS.BALANCE))));
    }

    @Override
    public void setBalance(int accountId, BigDecimal balance) {
        dsl.update(ACCOUNTS).set(ACCOUNTS.BALANCE, balance).where(ACCOUNTS.ID.eq(accountId)).execute();
    }
}
