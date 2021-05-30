package ru.olegr.accountapp.logic.impl.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import ru.olegr.accountapp.logic.api.dao.AccountsDao;
import ru.olegr.accountapp.logic.api.dao.TransactionsDao;
import ru.olegr.accountapp.model.dto.Transaction;
import ru.olegr.accountapp.model.dto.TransactionResponse;
import ru.olegr.accountapp.logic.api.service.TransactionsService;
import ru.olegr.accountapp.model.entity.Account;
import ru.olegr.accountapp.model.exception.AccountNotFoundException;
import ru.olegr.accountapp.model.exception.InsufficientBalanceException;
import ru.olegr.accountapp.model.exception.SameAccountException;

import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;

@Service
public class TransactionsServiceImpl implements TransactionsService {

    private final AccountsDao accountsDao;
    private final TransactionsDao transactionsDao;

    @Autowired
    public TransactionsServiceImpl(AccountsDao accountsDao, TransactionsDao transactionsDao) {
        this.accountsDao = accountsDao;
        this.transactionsDao = transactionsDao;
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public TransactionResponse process(Transaction transaction) {
        String fromNumber = transaction.getFromAccountNumber();
        String toNumber = transaction.getToAccountNumber();
        checkSameAccount(fromNumber, toNumber);
        BigDecimal amount = transaction.getAmount();

        Set<Account> accounts = accountsDao.getAccountsByNumbersForUpdate(
            Set.of(transaction.getFromAccountNumber(), transaction.getToAccountNumber())
        );

        String txnId = processInternal(accounts, fromNumber, toNumber, amount);
        return new TransactionResponse(txnId);
    }

    private void checkSameAccount(String fromNumber, String toNumber) {
        if (fromNumber.equals(toNumber)) {
            throw new SameAccountException();
        }
    }

    private String processInternal(Set<Account> accounts, String fromNumber, String toNumber, BigDecimal amount) {
        Account fromAccount = getAccountFromSet(accounts, fromNumber);
        if (fromAccount.getBalance().compareTo(amount) < 0) {
            throw new InsufficientBalanceException(fromAccount.getNumber());
        }

        Account toAccount = getAccountFromSet(accounts, toNumber);

        accountsDao.setBalance(fromAccount.getId(), fromAccount.getBalance().subtract(amount));
        accountsDao.setBalance(toAccount.getId(), toAccount.getBalance().add(amount));

        String txnId = generateTransactionId();
        transactionsDao.saveTransaction(txnId, fromAccount.getId(), toAccount.getId(), amount);
        return txnId;
    }

    private Account getAccountFromSet(Set<Account> accounts, String accountNumber) {
        return accounts.stream()
            .filter(a -> accountNumber.equals(a.getNumber()))
            .findFirst()
            .orElseThrow(() -> new AccountNotFoundException(accountNumber));
    }

    private String generateTransactionId() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
}
