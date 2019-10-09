package org.sora.rest.information.dto;

import lombok.Data;
import org.sora.rest.StatusDTO;

import java.util.Map;

@Data
public class CountriesDTO {

    private StatusDTO status;
    private InformationDTO information;

    public Map<String, CountryDTO> getCountries(){

        return information.getTopics();
    }
}
