package org.sora.rest.project.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.experimental.Accessors;
import org.sora.rest.ProjectStatus;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProjectDTO {

    private UUID id;
    private String name;
    private String description;
    private String detailedDescription;
    private String walletAccountId;
    private String email;
    private BigDecimal fundingTarget;
    private BigDecimal fundingCurrent;
    private Instant fundingDeadline;
    private String projectLink;
    private String imageLink;
    private Instant statusUpdateTime;
    private ProjectStatus status;
    private int votedFriendsCount;
    private BigDecimal votes;
    private boolean favorite;
    private boolean unwatched;
    private int favoriteCount;
}
