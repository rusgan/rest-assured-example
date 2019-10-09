package org.sora.rest.wallet.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.sora.rest.StatusDTO;

@Data
public class AccountTransactionsDTO {

    @JsonProperty("status")
    StatusDTO status;

    @JsonProperty("transactions")
    TransactionDTO[] transactions;

    public TransactionDTO getTransactionByHash(String hash){

        TransactionDTO transaction = null;

        for (TransactionDTO transactionDTO : transactions) {

            if (transactionDTO.getTransactionId().equals(hash)) {

                transaction = transactionDTO;
                break;
            }
        }
        return transaction;
    }
}
