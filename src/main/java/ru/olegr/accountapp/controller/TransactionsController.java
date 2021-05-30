package ru.olegr.accountapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.olegr.accountapp.logic.api.service.TransactionsService;
import ru.olegr.accountapp.model.dto.Transaction;
import ru.olegr.accountapp.model.dto.TransactionResponse;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/transactions")
public class TransactionsController {

    private final TransactionsService transactionsService;

    @Autowired
    public TransactionsController(TransactionsService transactionsService) {
        this.transactionsService = transactionsService;
    }

    @PostMapping({"", "/"})
    public TransactionResponse process(@Valid @RequestBody Transaction transaction) {
        return transactionsService.process(transaction);
    }
}
