package org.sora.dataproviders;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jp.co.soramitsu.sora.sdk.did.model.dto.DID;
import lombok.Data;
import lombok.experimental.Accessors;

import java.security.KeyPair;

@Data
@Accessors(chain = true)
public class User {

    private String firstName;
    private String lastName;
    private String phone;
    private String country;
    private String status;
    @JsonIgnore
    private KeyPair keyPair;
    private String passphrase; //aka mnemonic
    private DID userId;
    private String accountId;
}
