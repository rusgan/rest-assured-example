package org.sora.steps;

import io.qameta.allure.Step;
import io.restassured.specification.RequestSpecification;
import org.sora.rest.information.dto.CountriesDTO;
import org.sora.rest.SoraFilter;
import org.sora.properties.Properties;

import static io.restassured.RestAssured.given;
import static org.sora.rest.information.InformationEndpoints.COUNTRY;
import static org.sora.rest.Specifications.initSpec;

public class InformationSteps {

    private RequestSpecification informationSpec = initSpec(Properties.INFORMATION_URL);


    @Step
    public CountriesDTO getCountries(){

        return given()
                .spec(informationSpec)
                .filter(SoraFilter.filter)
                .get(COUNTRY)
                .then()
                .statusCode(200).extract().as(CountriesDTO.class);
    }
}
