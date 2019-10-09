package org.sora.rest.wallet.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class SendTransactionDTO {

    byte[] transaction;
}
