package com.safetynet.alerts.model;

import java.util.List;
import java.util.Map;

import lombok.Data;

@Data
public class AlertsDonnee {

	private List<Map<String, String>> persons;
	private List<Map<String, String>> firestations;

	private List<MedicalRecord> medicalrecords;

}
