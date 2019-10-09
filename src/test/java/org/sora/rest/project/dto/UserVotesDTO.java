package org.sora.rest.project.dto;

import lombok.Data;
import org.sora.rest.StatusDTO;

@Data
public class UserVotesDTO {

    StatusDTO status;
    private float votes;
    private float lastReceivedVotes;
}
