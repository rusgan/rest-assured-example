package org.sora.rest.wallet.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

public class BalanceDTO {

    @JsonProperty("assets")
    private ArrayList<String> assets;

    public BalanceDTO createListAddAsset(String asset) {
        assets = new ArrayList<String>();
        assets.add(asset);
        return this;
    }

    public BalanceDTO setQuery(byte[] query) {
        this.query = query;
        return this;
    }

    @JsonProperty("query")
    private byte[] query;
}
