package com.safetynet.alerts.model;

import java.util.List;

import lombok.Data;

@Data
public class AlertsData {

	private List<Person> persons;
	private List<Firestation> firestations;
	private List<MedicalRecord> medicalrecords;
}
