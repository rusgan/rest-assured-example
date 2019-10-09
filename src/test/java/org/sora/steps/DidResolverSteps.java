package org.sora.steps;

import io.qameta.allure.Step;
import io.restassured.specification.RequestSpecification;
import jp.co.soramitsu.sora.sdk.did.model.dto.DDO;
import org.sora.rest.GeneralResponseDTO;
import org.sora.rest.did.DidEndpoints;
import org.sora.properties.Properties;

import static io.restassured.RestAssured.given;
import static org.sora.rest.Specifications.initSpec;
import static org.sora.utils.DidUtil.ddoToJson;

public class DidResolverSteps {

    private static RequestSpecification didSpec = initSpec(Properties.DID_URL);

    @Step("Register new DID-DDO pair in Identity System")
    public GeneralResponseDTO registerDid(DDO userDdo){

        return given()
                .spec(didSpec)
                .body(ddoToJson(userDdo))
                .when()
                .post(DidEndpoints.did)
                .then()
                .statusCode(200).extract().as(GeneralResponseDTO.class);
    }
}
