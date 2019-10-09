package org.sora.steps;

import io.qameta.allure.Step;
import io.restassured.specification.RequestSpecification;
import org.sora.rest.authorization.dto.LoginRequestDTO;
import org.sora.rest.authorization.dto.LoginResponseDTO;
import org.sora.rest.CommonEndPoints;
import org.sora.properties.Properties;

import static io.restassured.RestAssured.given;
import static org.sora.rest.Specifications.initSpec;

public class AuthSteps {

    @Step("Get token for {username} using {password}")
    public static String getToken(String username, String password){

        RequestSpecification authSpec = initSpec(Properties.AUTH_URL);

        LoginResponseDTO responseLogin = given()
                .spec(authSpec)
                .body(new LoginRequestDTO()
                        .setUsername(username)
                        .setPassword(password))
                .when()
                .post(CommonEndPoints.LOGIN)
                .then()
                .statusCode(200)
                .extract().as(LoginResponseDTO.class);
        return responseLogin.getToken();
    }
}
