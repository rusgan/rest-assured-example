package org.sora.rest;

import io.restassured.filter.Filter;
import io.restassured.filter.FilterContext;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;
import io.restassured.specification.FilterableResponseSpecification;
import jp.co.soramitsu.sora.sdk.did.model.dto.DID;
import org.sora.utils.DidUtil;
import org.spongycastle.util.encoders.Base64;
import org.spongycastle.util.encoders.Hex;

import java.security.KeyPair;
import java.util.Date;

import static org.sora.utils.CryptoUtil.sign;


public class SoraFilter implements Filter {

    public static SoraFilter filter;
    private KeyPair kp;

    private SoraFilter(KeyPair kp){

        this.kp = kp;
    }

    public Response filter(FilterableRequestSpecification requestSpec, FilterableResponseSpecification responseSpec, FilterContext ctx) {

        String method = requestSpec.getMethod().toUpperCase();
        String uri = requestSpec.getURI();
        String body = requestSpec.getBody();
        DID owner = DidUtil.generateDID(Hex.toHexString(kp.getPublic().getEncoded()).substring(0, 20));
        DID key = owner.withFragment("keys-1");

        String timestamp = String.valueOf(new Date().getTime());
        String stringToSign;

        if (body != null) {
            stringToSign = method + uri + body + timestamp + owner + key;
        }
        else {
            stringToSign = method + uri + timestamp + owner + key;
        }

        byte[] signature = sign(stringToSign, kp);

        requestSpec
                .header("SORA-AUTH-ID", owner.toString())
                .header("SORA-AUTH-PUBLIC-KEY", key.toString())
                .header("SORA-AUTH-TIMESTAMP", timestamp)
                .header("SORA-AUTH-SIGNATURE", Base64.toBase64String(signature));
        return ctx.next(requestSpec, responseSpec);
    }

    public static void initFilter(KeyPair kp){

        SoraFilter.filter = new SoraFilter(kp);
    }
}
