package ru.olegr.accountapp.logic.impl.dao;

import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import ru.olegr.accountapp.logic.api.dao.TransactionsDao;
import ru.olegr.accountapp.logic.stereotype.Dao;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static ru.olegr.accountapp.model.generated.tables.Transactions.TRANSACTIONS;

@Dao
public class TransactionsDaoImpl implements TransactionsDao {

    private final DSLContext dsl;

    @Autowired
    public TransactionsDaoImpl(DSLContext dsl) {
        this.dsl = dsl;
    }


    @Override
    public void saveTransaction(String txnId, int fromAccountId, int toAccountId, BigDecimal amount) {
        dsl.insertInto(TRANSACTIONS,
            TRANSACTIONS.CREATED_AT,
            TRANSACTIONS.TXN_ID,
            TRANSACTIONS.FROM_ACCOUNT_ID,
            TRANSACTIONS.TO_ACCOUNT_ID,
            TRANSACTIONS.AMOUNT
        ).values(
            LocalDateTime.now(),
            txnId,
            fromAccountId,
            toAccountId,
            amount
        ).execute();
    }
}
