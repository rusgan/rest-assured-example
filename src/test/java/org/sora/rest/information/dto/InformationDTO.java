package org.sora.rest.information.dto;

import lombok.Data;

import java.util.Map;

@Data
class InformationDTO {

    private String sectionName;
    private Map<String, CountryDTO> topics;
}
