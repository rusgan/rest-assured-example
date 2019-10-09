package org.sora.rest.wallet.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.sora.rest.StatusDTO;

import java.util.Arrays;


@Data
public class AccountAssetsDTO {

    @JsonProperty("status")
    private StatusDTO stauts;

    @JsonProperty("assets")
    private AssetDTO[] assets;


    public AssetDTO getAsset(String id){

        return Arrays.stream(assets)
                .filter(asset -> asset.getId().equals(id))
                .findAny()
                .orElse(null);
    }
}
