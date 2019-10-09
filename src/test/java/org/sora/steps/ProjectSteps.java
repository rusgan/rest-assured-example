package org.sora.steps;

import io.qameta.allure.Step;
import io.restassured.specification.RequestSpecification;
import org.sora.rest.Group;
import org.sora.rest.GeneralResponseDTO;
import org.sora.rest.project.dto.CreateProjectDTO;
import org.sora.rest.project.dto.GetProjectsDTO;
import org.sora.rest.project.dto.ProjectDTO;
import org.sora.rest.project.ProjectEndPoints;
import org.sora.rest.SoraFilter;
import org.sora.rest.project.dto.UserVotesDTO;
import org.sora.properties.Properties;

import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.sora.rest.Specifications.initSpec;
import static org.assertj.core.api.Assertions.*;
import static org.sora.utils.AssertUtil.assertPositiveStatus;

public class ProjectSteps extends BaseSteps {

    private static RequestSpecification projectSpec = initSpec(Properties.PROJECT_URL);

    @Step("Get projects")
    public  GetProjectsDTO getProjects(String group){

        return given()
                .filter(SoraFilter.filter)
                .spec(projectSpec)
                .when()
                .queryParam("offset", 0)
                .queryParam("count", 50)
                .get(ProjectEndPoints.PROJECT_GROUP, group)
                .then()
                .statusCode(200).extract().as(GetProjectsDTO.class);
    }

    @Step("Get project by name: {name}")
    public ProjectDTO getProject(String name){

        ProjectDTO response = given()
                .filter(SoraFilter.filter)
                .spec(projectSpec)
                .when()
                .queryParam("offset", 0)
                .queryParam("count", 50)
                .get(ProjectEndPoints.PROJECT_GROUP, Group.all.toString())
                .then()
                .statusCode(200).extract().as(GetProjectsDTO.class).getProjectByName(name);

        assertThat(response).as("No such project %s", name).isNotNull();

        return response;
    }

    @Step("Get votes")
    public UserVotesDTO getVotes(){

        return given()
                .filter(SoraFilter.filter)
                .spec(projectSpec)
                .get(ProjectEndPoints.USER_VOTES)
                .then()
                .statusCode(200).extract().as(UserVotesDTO.class);
    }

    @Step("Vote for {projectId} project, with {amount} of votes")
    public void vote(UUID projectId, int amount){

        given()
                .filter(SoraFilter.filter)
                .spec(projectSpec)
                .when()
                .queryParam("votes", amount)
                .post(ProjectEndPoints.PROJECT_PROJECT_ID_VOTE, projectId)
                .then()
                .statusCode(200);
    }

    @Step("Create project \"{name}\"")
    public void createProject(ProjectDTO project, String token) {

        GeneralResponseDTO response = given()
                .spec(projectSpec)
                .auth().oauth2(token)
                .when()
                .body(new CreateProjectDTO()
                        .setProjectDTO(project))
                .post(ProjectEndPoints.ADMIN_PROJECT)
                .then()
                .statusCode(200).extract().as(GeneralResponseDTO.class);

        assertPositiveStatus(response.getStatus());
    }
}
