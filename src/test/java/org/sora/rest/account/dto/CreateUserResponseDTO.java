package org.sora.rest.account.dto;

import lombok.Data;
import org.sora.rest.StatusDTO;

@Data
public class CreateUserResponseDTO {

    StatusDTO status;
    long blockingTime;
}
