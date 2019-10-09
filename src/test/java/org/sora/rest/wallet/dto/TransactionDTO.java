package org.sora.rest.wallet.dto;

import lombok.Data;

@Data
public class TransactionDTO {

    private String amount;
    private String assetId;
    private String details;
    private boolean incoming;
    private String peerId;
    private String peerName;
    private String status;
    private long timestamp;
    private String transactionId;
    private String reason;
}
