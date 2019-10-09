package org.sora.rest.wallet.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ContactDTO {

    @JsonProperty("accountId")
    private String accountId;
    @JsonProperty("firstName")
    private String firstName;
    @JsonProperty("lastName")
    private String lastName;
}
