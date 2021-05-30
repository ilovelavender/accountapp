package ru.olegr.accountapp.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.olegr.accountapp.logic.api.service.TransactionsService;
import ru.olegr.accountapp.model.dto.Transaction;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;

import static org.jooq.tools.StringUtils.isBlank;

/**
 * Сервис, который осуществляет переводы по расписанию, на основании конфигурационного файла,
 * путь для которого формируется следующим обрзазом:
 *
 * [ПАПКА_ПОЛЬЗОВАТЕЛЯ]/{transactions.file}, где {transactions.file} - значение соответствюущей проперти из
 * application.properties. Пользователь берётся тот, из под которого запущено приложение.
 *
 * После процессинга всех транзакций рядом с файлом конфигурации сохраняется отчёт
 */
@Component
public class TransactionsSchedule {
    private static final Logger LOGGER = LoggerFactory.getLogger(TransactionsSchedule.class);

    private final Environment environment;
    private final TransactionsService transactionsService;
    private final Validator validator;

    @Autowired
    public TransactionsSchedule(Environment environment, TransactionsService transactionsService,
                                @Qualifier("accountAppValidator") Validator validator) {
        this.environment = environment;
        this.transactionsService = transactionsService;
        this.validator = validator;
    }

    @Scheduled(cron = "${transactions.cron}")
    public void scheduleTransactionsProcessing() {
        String filePathPart = environment.getProperty("transactions.file");
        Path filePath = Path.of(System.getProperty("user.home"), filePathPart);
        Path reportPath = Path.of(filePath.getParent().toAbsolutePath().toString(),
                "transactions" + (System.currentTimeMillis() / 1000L));
        try {
            if (!Files.exists(filePath)) {
                throw new RuntimeException("File not found at path - " + filePath);
            }

            List<String> lines = Files.readAllLines(filePath);
            StringBuilder report = new StringBuilder();
            for (int i = 0; i < lines.size(); i++) {
                processLine(i, lines.get(i), report);
            }
            LOGGER.info("Finished processing, saving report");
            Files.writeString(reportPath, report.toString());
            LOGGER.info("Report saved at path - " + reportPath.toString());
        } catch (IOException | RuntimeException exe) {
            LOGGER.error("Got error when trying to process transactions for schedule", exe);
        }
    }

    private void processLine(int ix, String line, StringBuilder report) {
        try {
            if (isBlank(line)) {
                throw new RuntimeException("Found blank line at index - " + ix);
            }

            Transaction transaction = parseTransaction(line);
            validateTransaction(transaction);

            LOGGER.info("Start processing transaction for line at index " + ix + " - " + line);
            String txnId = transactionsService.process(transaction).getTransactionId();
            LOGGER.info("Successfully processed transaction for line at index " + ix + ". Txn ID - " + txnId);
            report.append(line).append(",SUCCESS,").append(txnId).append(System.lineSeparator());
        } catch (RuntimeException exe) {
            LOGGER.error("Got error when processing row at index " + ix + " - " + line, exe);
            report.append(line).append(",ERROR,").append(exe.getMessage()).append(System.lineSeparator());
        }
    }

    private Transaction parseTransaction(String line) {
        String[] transactionParams = line.split(",", -1);
        if (transactionParams.length < 3) {
            throw new RuntimeException("Line must contain 3 comma-separated transaction params" +
                    " - from account number, to account number and amount");
        }

        return new Transaction(
                transactionParams[0].strip(),
                transactionParams[1].strip(),
                new BigDecimal(transactionParams[2])
        );
    }

    private void validateTransaction(Transaction transaction) {
        Set<ConstraintViolation<Transaction>> constraintViolationSet = validator.validate(transaction);
        if (!constraintViolationSet.isEmpty()) {
            StringBuilder logSb = new StringBuilder("Constraint violations for transaction object:");
            constraintViolationSet.forEach(c ->
                logSb.append(c.getPropertyPath().toString())
                    .append(" - ")
                    .append(c.getMessage())
                    .append(System.lineSeparator()));
            LOGGER.error(logSb.toString());
            throw new RuntimeException("Found " + constraintViolationSet.size()
                    + " validation errors for transaction");
        }
    }
}
