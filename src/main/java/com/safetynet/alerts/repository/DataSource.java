package com.safetynet.alerts.repository;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.alerts.model.AlertsData;
import com.safetynet.alerts.model.Firestation;
import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.model.Person;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.List;

public class DataSource {

    @Value("${json.file.location}")
    private Resource jsonFile;
    
    private AlertsData data;
    
    /**
     * jsonDataReader - Deserialize json root node from the json data repository
     * into AlertsData class object.
     *
     * @throws JsonMappingException when the json doesn't
     */
    @PostConstruct
    public void loadJsonData() throws JsonMappingException {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            data = objectMapper.readValue(jsonFile.getFile(), AlertsData.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Person> getPersons() {
        return data.getPersons();
    }

    public List<Firestation> getFirestations() {
        return data.getFirestations();
    }

    public List<MedicalRecord> getMedicalrecords() {
        return data.getMedicalrecords();
    }
}
