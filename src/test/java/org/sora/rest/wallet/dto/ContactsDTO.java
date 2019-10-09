package org.sora.rest.wallet.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.sora.rest.StatusDTO;

@Data
public class ContactsDTO {

    @JsonProperty("results")
    ContactDTO[] results;

    @JsonProperty("status")
    StatusDTO status;
}
