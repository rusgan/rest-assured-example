package org.sora.steps;

import io.restassured.specification.RequestSpecification;
import org.sora.rest.activityfeed.dto.ActivityFeedDTO;
import org.sora.rest.CommonEndPoints;
import org.sora.rest.SoraFilter;
import org.sora.properties.Properties;

import static io.restassured.RestAssured.given;
import static org.sora.rest.Specifications.initSpec;

public class ActivitySteps extends BaseSteps {

    private static RequestSpecification activitySpec = initSpec(Properties.ACTIVITY_URL);

    public ActivityFeedDTO getFeed(){

        return given()
                .filter(SoraFilter.filter)
                .spec(activitySpec)
                .when()
                .queryParam("offset", 0)
                .queryParam("count", 50)
                .get(CommonEndPoints.FEED)
                .then()
                .statusCode(200).extract().as(ActivityFeedDTO.class);
    }
}
