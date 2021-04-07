package com.safetynet.alerts.model.dto;

import java.util.List;

public class ChildrenCoveredDTO {
    List<PersonInfoDTO> enfants;
    List<PersonInfoDTO> others;

    public ChildrenCoveredDTO() {
    }

    public List<PersonInfoDTO> getEnfants() {
        return enfants;
    }

    public void setEnfants(List<PersonInfoDTO> enfants) {
        this.enfants = enfants;
    }

    public List<PersonInfoDTO> getOthers() {
        return others;
    }

    public void setOthers(List<PersonInfoDTO> others) {
        this.others = others;
    }
}
