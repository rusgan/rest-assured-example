package org.sora.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.KeyPair;
import java.security.SecureRandom;
import java.util.Date;
import jp.co.soramitsu.crypto.ed25519.Ed25519Sha3;
import jp.co.soramitsu.crypto.ed25519.EdDSAPrivateKey;
import jp.co.soramitsu.crypto.ed25519.EdDSAPublicKey;
import jp.co.soramitsu.sora.sdk.crypto.json.JSONEd25519Sha3SignatureSuite;
import jp.co.soramitsu.sora.sdk.did.model.dto.DDO;
import jp.co.soramitsu.sora.sdk.did.model.dto.Options;
import jp.co.soramitsu.sora.sdk.did.model.dto.PublicKey;
import jp.co.soramitsu.sora.sdk.did.model.type.SignatureTypeEnum;
import jp.co.soramitsu.sora.sdk.json.JsonUtil;
import org.spongycastle.crypto.generators.SCrypt;
import org.spongycastle.jcajce.provider.digest.SHA3;
import org.spongycastle.util.encoders.Hex;

import static java.nio.charset.StandardCharsets.UTF_8;

public class CryptoUtil {

    private static final Ed25519Sha3 ed25519Sha3 = new Ed25519Sha3();
    private static final JSONEd25519Sha3SignatureSuite suite = new JSONEd25519Sha3SignatureSuite();
    private static final ObjectMapper mapper = JsonUtil.buildMapper();
    private static final SecureRandom secureRandom = new SecureRandom();

    private static String CHARSET = "UTF-8";

    public static byte[] generateScryptSeed(byte[] entropy,
                                            String project, String purpose, String password) {
        try {
            String salt = new StringBuilder()
                    .append(project)
                    .append("|")
                    .append(purpose)
                    .append("|")
                    .append(password)
                    .toString();

            return SCrypt.generate(entropy, salt.getBytes(CHARSET), 16384, 8, 1, 32);
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    public static String SHA3(String P, String salt) {
        try {
            return Hex.toHexString(new SHA3.DigestSHA3(256).digest((salt + P).getBytes(CHARSET)));
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    public static KeyPair generateKeys(byte[] seed) {
        return ed25519Sha3.generateKeypair(seed);
    }

    public static DDO signDDO(KeyPair keyPair, DDO ddo) {
        Options options = Options.builder()
                .type(SignatureTypeEnum.Ed25519Sha3Signature)
                .nonce(Hex.toHexString(getSecureRandom(8)))
                .creator(ddo.getId().withFragment("keys-1"))
                .created(new Date())
                .build();
        try {
            ObjectNode singedDdo = suite
                    .sign(ddo, (EdDSAPrivateKey) keyPair.getPrivate(), options);
            return mapper
                    .readValue(singedDdo.toString(), DDO.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static DDO signDDO(KeyPair keyPair, DDO ddo, Options options) {
        try {
            return mapper
                    .readValue(suite.sign(ddo, (EdDSAPrivateKey) keyPair.getPrivate(), options).toString(),
                            DDO.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean verifySignedDDO(DDO ddo) {
        EdDSAPublicKey publicKey = (EdDSAPublicKey) Ed25519Sha3
                .publicKeyFromBytes(getProofKeyFromDdo(ddo));

        try {
            return suite.verify(ddo, publicKey);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static byte[] getProofKeyFromDdo(DDO ddo) {
        byte[] publicKeyByte = new byte[0];
        for (PublicKey publicKey : ddo.getPublicKey()) {
            if (publicKey.getId().equals(ddo.getProof().getOptions().getCreator())) {
                publicKeyByte = publicKey.getPublicKey();
            }
        }

        return publicKeyByte;
    }

    public static byte[] getSecureRandom(int length) {
        byte[] e = new byte[length];
        secureRandom.nextBytes(e);
        return e;
    }

    public static KeyPair textToKeys(String privKey, String pubKey) {
        return Ed25519Sha3.keyPairFromBytes(Hex.decode(privKey), Hex.decode(pubKey));
    }

    public static byte[] sign(String stringToSign, KeyPair keys){

        Ed25519Sha3 ed25519Sha3 = new Ed25519Sha3();
        return ed25519Sha3.rawSign(stringToSign.getBytes(UTF_8), keys);
    }
}
