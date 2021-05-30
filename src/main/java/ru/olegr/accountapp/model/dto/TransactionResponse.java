package ru.olegr.accountapp.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Ответ на запрос на перевод со счёта на счёт
 */
@ApiModel("Response for successfull transaction")
public class TransactionResponse implements DtoModel {

    /**
     * Идентификтаор транзакции
     */
    @ApiModelProperty(value = "Transaction ID", example = "cd4b4c77713247f5a783961f6ea53e3b")
    private final String transactionId;

    public TransactionResponse(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getTransactionId() {
        return transactionId;
    }
}
