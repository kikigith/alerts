package com.safetynet.alerts.model.dto;

import java.util.List;

public class PersonsCoveredByStation {


    int stationID;
    List<PersonInfoDTO> personsCovered;
    int nbAdult;
    int nbEnfants;


    public PersonsCoveredByStation(int stationID) {
        this.stationID = stationID;

    }
    public PersonsCoveredByStation(){

    }

    public int getStationID() {
        return stationID;
    }

    public void setStationID(int stationID) {
        this.stationID = stationID;
    }

    public List<PersonInfoDTO> getPersonsCovered() {
        return personsCovered;
    }

    public void setPersonsCovered(List<PersonInfoDTO> personsCovered) {
        this.personsCovered = personsCovered;
    }

    public int getNbAdult() {
        return nbAdult;
    }

    public void setNbAdult(int nbAdult) {
        this.nbAdult = nbAdult;
    }

    public int getNbEnfants() {
        return nbEnfants;
    }

    public void setNbEnfants(int nbEnfants) {
        this.nbEnfants = nbEnfants;
    }

    @Override
    public String toString() {
        return "PersonsCoveredByStation{" +
                "stationID=" + stationID +
                ", personsCovered=" + personsCovered +
                ", nbAdult=" + nbAdult +
                ", nbEnfants=" + nbEnfants +
                '}';
    }
}
