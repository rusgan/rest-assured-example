package org.sora.rest.activityfeed.dto;

import jp.co.soramitsu.sora.sdk.did.model.dto.DID;
import lombok.Data;
import org.sora.rest.StatusDTO;

import java.util.Map;
import java.util.UUID;

@Data
public class ActivityFeedDTO {

    private StatusDTO status;
    private Map<DID, UserDictDTO> usersDict;
    private Map<UUID, ProjectDictDTO> projectsDict;
    private ActivityDTO[] activity;
}