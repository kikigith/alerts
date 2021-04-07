package com.safetynet.alerts.controller;

import java.io.IOException;
import java.net.URI;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.service.MedicalRecordService;

@RestController
public class MedicalRecordController {

	private final Logger logger = LoggerFactory.getLogger(FirestationController.class);

	@Autowired
	private MedicalRecordService medicalRecordService;

	/**
	 * 
	 */
	@GetMapping("medicalRecords")
	public List<MedicalRecord> getMedicalRecords() throws Exception {
		logger.info("GET /medicalrecords called");
		return medicalRecordService.findAll();
	}

	@PostMapping("medicalRecord")
	public ResponseEntity<MedicalRecord> saveMedicalRecord(@RequestBody MedicalRecord medicalRecord) {
		logger.info("Request = @RequestBody = {}", medicalRecord.toString());
		MedicalRecord persistedMedicalRecord = null;
		try {
			persistedMedicalRecord = medicalRecordService.saveMedicalRecord(medicalRecord);

		} catch (Exception e) {
			// TODO Auto-generated catch block

			e.printStackTrace();
		}
		logger.info("Response = @ResponseBody = {}", persistedMedicalRecord.toString());
		return ResponseEntity.created(URI.create(String.format("/medicalRecord"))).body(persistedMedicalRecord);

	}

	@PutMapping("medicalRecord")
	public ResponseEntity<MedicalRecord> updateMedicalRecord(@RequestParam("firstname") final String firstname,
			@RequestParam("lastname") final String lastname, @RequestBody MedicalRecord medicalRecord) {
		logger.info("Request = @RequestBody = {}", medicalRecord.toString());
		try {
			MedicalRecord mr = medicalRecordService.findMedicalRecord(firstname, lastname);
			String fname = medicalRecord.getFirstName();
			if (fname != null) {
				mr.setFirstName(fname);
			}
			String lname = medicalRecord.getLastName();
			if (lname != null) {
				mr.setLastName(lname);
			}
			Date birthdate = medicalRecord.getBirthdate();
			if (birthdate != null) {
				mr.setBirthdate(birthdate);
			}
			mr.setAllergies(medicalRecord.getAllergies());
			mr.setMedications(medicalRecord.getMedications());
			medicalRecordService.saveMedicalRecord(mr);
			logger.info("Response = @ResponseBody = {}", mr.toString());
			return ResponseEntity.ok().body(mr);
		} catch (Exception e) {
			e.printStackTrace();
			return null;

		}

	}

	@DeleteMapping("medicalRecord")
	public ResponseEntity<MedicalRecord> deleteMedicalRecord(@RequestParam("lastname") final String lastname,
			@RequestParam("firstname") final String firstname)
			throws JsonParseException, JsonMappingException, IOException {
		logger.info("Request Delete = @RequestBody = {},{}", lastname, firstname);
		medicalRecordService.deleteMedicalRecord(lastname, firstname);
		return new ResponseEntity<MedicalRecord>(HttpStatus.ACCEPTED);
	}


}
