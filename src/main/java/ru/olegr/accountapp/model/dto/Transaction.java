package ru.olegr.accountapp.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import ru.olegr.accountapp.model.validation.annotations.MaxScale;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

import static ru.olegr.accountapp.model.validation.ValidationConstants.ACCOUNT_NUMBER_LENGTH;
import static ru.olegr.accountapp.model.validation.ValidationConstants.MAX_AMOUNT_SCALE;

/**
 * Канальная модель запроса перевода со счёта на счёт
 */
@ApiModel("Transfer between two accounts")
public class Transaction implements DtoModel {

    /**
     * Номер счёта списания
     */
    @ApiModelProperty(value = "Number of sending account", example = "40814444444444444444")
    @NotBlank(message = "From account number should not be blank")
    @Pattern(regexp = "\\d+", message = "From account number must contain only digits")
    @Size(
        min = ACCOUNT_NUMBER_LENGTH,
        max = ACCOUNT_NUMBER_LENGTH,
        message = "From account number length must be " + ACCOUNT_NUMBER_LENGTH)
    private final String fromAccountNumber;

    /**
     * Номер счёта зачисления
     */
    @ApiModelProperty(value = "Number of receiving account", example = "40813333333333333333")
    @NotBlank(message = "To account number should not be blank")
    @Pattern(regexp = "\\d+", message = "To account number must contain only digits")
    @Size(
        min = ACCOUNT_NUMBER_LENGTH,
        max = ACCOUNT_NUMBER_LENGTH,
        message = "To account number length must be " + ACCOUNT_NUMBER_LENGTH)
    private final String toAccountNumber;

    /**
     * Сумма транзакции
     */
    @ApiModelProperty(value = "Amount to send", example = "1.11")
    @NotNull
    @PositiveOrZero
    @MaxScale(MAX_AMOUNT_SCALE)
    private final BigDecimal amount;

    public Transaction(String fromAccountNumber, String toAccountNumber, BigDecimal amount) {
        this.fromAccountNumber = fromAccountNumber;
        this.toAccountNumber = toAccountNumber;
        this.amount = amount;
    }

    public String getFromAccountNumber() {
        return fromAccountNumber;
    }

    public String getToAccountNumber() {
        return toAccountNumber;
    }

    public BigDecimal getAmount() {
        return amount;
    }
}
