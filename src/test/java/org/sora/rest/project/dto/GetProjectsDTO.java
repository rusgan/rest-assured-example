package org.sora.rest.project.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.sora.rest.StatusDTO;

@Data
public class GetProjectsDTO {

    @JsonProperty("status")
    StatusDTO status;

    @JsonProperty("projects")
    ProjectDTO[] projects;

    public ProjectDTO getProjectByName(String name){

        ProjectDTO project = null;
        for (ProjectDTO projectDTO : projects) {

            if (projectDTO.getName().equals(name)) {
                project = projectDTO;
                break;
            }
        }
        return project;
    }
}
