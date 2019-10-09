package org.sora.rest.authorization.dto;


import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class LoginRequestDTO {

    private String username;
    private String password;
}
