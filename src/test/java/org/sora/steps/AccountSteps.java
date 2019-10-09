package org.sora.steps;

import io.qameta.allure.Step;
import io.restassured.specification.RequestSpecification;
import jp.co.soramitsu.sora.sdk.did.model.dto.DID;
import lombok.val;
import org.sora.dataproviders.User;
import org.sora.dataproviders.db.Statements;
import org.sora.rest.GeneralResponseDTO;
import org.sora.rest.SoraFilter;
import org.sora.rest.account.dto.CreateUserRequestDTO;
import org.sora.rest.account.dto.CreateUserResponseDTO;
import org.sora.rest.account.dto.RegisterRequestDTO;
import org.sora.rest.account.dto.VerifyDTO;

import java.sql.Connection;

import static io.restassured.RestAssured.given;
import static org.sora.dataproviders.db.Connections.*;
import static org.sora.rest.Specifications.initSpec;
import static org.sora.rest.account.AccountEndPoints.*;
import static org.sora.properties.Properties.*;

public class AccountSteps extends BaseSteps {

    private static Connection accCon;
    private RequestSpecification accSpec = initSpec(ACCOUNT_URL);

    @Step()
    public CreateUserResponseDTO createUser(User user){

        val requestBody = new CreateUserRequestDTO().setPhone(user.getPhone());
        return given()
            .filter(SoraFilter.filter)
            .spec(accSpec)
            .body(requestBody)
            .when()
            .post(USER_CREATE)
            .then()
            .statusCode(200).extract().as(CreateUserResponseDTO.class);
    }

    @Step("Register new user")
    public GeneralResponseDTO registerUser(User user){

        val requestBody = new RegisterRequestDTO(user);
        return given()
                .filter(SoraFilter.filter)
                .spec(accSpec)
                .body(requestBody)
                .when()
                .post(USER_REGISTER)
                .then()
                .statusCode(200).extract().as(GeneralResponseDTO.class);
    }

    //TODO move to own
    @Step("Get SMS verification code")
    public int getVerificationCode(DID userId){

        return Statements.getVerificationCode(accCon, userId.toString());
    }

    @Step("Verify user via SMS")
    public GeneralResponseDTO verify(int smsCode){

        return given()
                .filter(SoraFilter.filter)
                .spec(accSpec)
                .body(new VerifyDTO().setCode(String.valueOf(smsCode)))
                .when()
                .post(SMSCODE_VERIFY)
                .then()
                .statusCode(200).extract().as(GeneralResponseDTO.class);
    }

    @Step("Get phone verify by {email}")
    public String getVerifCodeByEmail(String email){

        return Statements.getPhoneVerificationByEmail(accCon, email);
    }


//TODO move to own
    //Connections
    public static void createAccountConnection(){

        accCon = createCon(ACCOUNT_DB_USER, ACCOUNT_DB_PASS);
    }

    public static void closeAccountConnection(){

        close(accCon);
    }
}
