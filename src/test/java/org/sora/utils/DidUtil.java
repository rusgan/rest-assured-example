package org.sora.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jp.co.soramitsu.sora.sdk.did.model.dto.DDO;
import jp.co.soramitsu.sora.sdk.did.model.dto.DID;
import jp.co.soramitsu.sora.sdk.did.model.dto.authentication.Ed25519Sha3Authentication;
import jp.co.soramitsu.sora.sdk.did.model.dto.publickey.Ed25519Sha3VerificationKey;
import jp.co.soramitsu.sora.sdk.json.JsonUtil;
import org.sora.dataproviders.User;
import org.spongycastle.util.encoders.Hex;

import java.security.KeyPair;
import java.util.Date;

public class DidUtil {

    public static DDO generateUserDDO(User user) {

        byte[] entropy = MnemonicUtil.getBytesFromMnemonic(user.getPassphrase());
        byte[] seed = CryptoUtil.generateScryptSeed(entropy, "SORA", "iroha keypair", "");
        KeyPair keys = CryptoUtil.generateKeys(seed);
        user.setKeyPair(keys);
        DID userDID = generateDID(Hex.toHexString(keys.getPublic().getEncoded()).substring(0, 20));
        user.setUserId(userDID)
                .setAccountId(userDID.toString().replace(':', '_') + "@sora");
        DDO userDDO = generateDDO(userDID, keys.getPublic().getEncoded());
        return CryptoUtil.signDDO(keys, userDDO);
    }

    public static DID generateDID(String identifier) {
        return DID.builder()
                .method("sora")
                .identifier(identifier)
                .build();
    }

    private static DDO generateDDO(DID owner, byte[] publicKey) {
        return DDO.builder()
                .id(owner)
                .authentication(new Ed25519Sha3Authentication(owner.withFragment("keys-1")))
                .created(new Date())
                .publicKey(new Ed25519Sha3VerificationKey(owner.withFragment("keys-1"), owner, publicKey))
                .build();
    }

    public static String ddoToJson(DDO ddo) {
        ObjectMapper mapper = JsonUtil.buildMapper();
        try {
            return mapper.writeValueAsString(ddo);
        } catch (JsonProcessingException e) {
            return "";
        }
    }
}
