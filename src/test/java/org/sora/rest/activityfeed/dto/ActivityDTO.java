package org.sora.rest.activityfeed.dto;

import jp.co.soramitsu.sora.sdk.did.model.dto.DID;
import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Data
class ActivityDTO {

    private Date issuedAt;
    private String issuedBy;
    private String type;
    private UUID projectId;
    private String name;
    private String description;
    private String detailedDescription;
    private String projectLink;
    private String imageLink;
    private long fundingTarget;
    private Date fundingDeadline;
    private String email;
    private String walletAccountId;
    private DID userId;
    private long invitations;
    private double votingRights;
    private int rank;
    private int totalRank;
    private DID sender;
    private DID receiver;
    private String message;
    private double amount;
    private double reward;
}
