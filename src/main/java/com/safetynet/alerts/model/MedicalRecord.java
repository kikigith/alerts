package com.safetynet.alerts.model;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class MedicalRecord {

	private String firstName;

	private String lastName;

	@JsonFormat(pattern = "dd/MM/yyyy")
	private Date birthdate;

	private List<String> medications;

	private List<String> allergies;

}
