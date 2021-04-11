package com.safetynet.alerts.model.dto;

import com.safetynet.alerts.model.Person;

import java.util.List;

public class StationCoverageDTO {
    private String address;
    private List<Person> personsCovered;

    public StationCoverageDTO(){}

    public StationCoverageDTO(String address, List<Person> personsCovered) {
        this.address = address;
        this.personsCovered = personsCovered;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<Person> getPersonsCovered() {
        return personsCovered;
    }

    public void setPersonsCovered(List<Person> personsCovered) {
        this.personsCovered = personsCovered;
    }
}
