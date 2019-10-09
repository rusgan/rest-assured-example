package org.sora.rest.account.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import org.sora.dataproviders.User;

@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RegisterRequestDTO {

    public RegisterRequestDTO(User user){

        userDataDTO = new UserDataDTO(user);
    }

    public RegisterRequestDTO(User user, String invitationCode){

        userDataDTO = new UserDataDTO(user);
        this.invitationCode = invitationCode;
    }

    @JsonProperty("userData")
    private UserDataDTO userDataDTO;

    @JsonProperty("invitationCode")
    private String invitationCode;
}
