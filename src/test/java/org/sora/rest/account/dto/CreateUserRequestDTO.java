package org.sora.rest.account.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CreateUserRequestDTO {

    String phone;
}