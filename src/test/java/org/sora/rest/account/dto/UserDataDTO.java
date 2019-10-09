package org.sora.rest.account.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jp.co.soramitsu.sora.sdk.did.model.dto.DID;
import lombok.Data;
import lombok.experimental.Accessors;
import org.sora.dataproviders.User;

@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDataDTO {

    public UserDataDTO(User user){

        country = user.getCountry();
        firstName = user.getFirstName();
        lastName = user.getLastName();
        phone = user.getPhone();
        status = user.getStatus();
        userId = user.getUserId();
    }

    @JsonProperty("country")
    private String country;
    @JsonProperty("firstName")
    private String firstName;
    @JsonProperty("lastName")
    private String lastName;
    @JsonProperty("phone")
    private String phone;
    @JsonProperty("status")
    private String status;
    @JsonProperty("userId")
    private DID userId;
}
