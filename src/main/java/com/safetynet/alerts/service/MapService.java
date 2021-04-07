package com.safetynet.alerts.service;

import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.model.dto.PersonInfoDTO;
import com.safetynet.alerts.repository.DataRepository;

import com.safetynet.alerts.utils.Utils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MapService {

    @Autowired
    private DataRepository dataRepository;

    @Autowired
    private PersonService personService;

    @Autowired
    private MedicalRecordService medicalRecordService;

    //@Autowired
    private ModelMapper modelMapper;


    public PersonInfoDTO convertMedicalRecordToPersonInfo(MedicalRecord mr){
        PersonInfoDTO personInfo=null;
        try {
            Person pers = personService.findPerson(mr.getLastName(), mr.getFirstName());
            personInfo = new PersonInfoDTO(mr.getFirstName(), mr.getLastName(),
                    Utils.calculateAge(mr.getBirthdate()), pers.getEmail(), pers.getPhone(), pers.getAddress(),
                    mr.getAllergies(), mr.getMedications());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return personInfo;
    }



}
