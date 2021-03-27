package com.safetynet.alerts.controller;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.Map;

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
import com.safetynet.alerts.model.Firestation;
import com.safetynet.alerts.model.dto.PersonInfoDTO;
import com.safetynet.alerts.service.FirestationService;

@RestController
public class FirestationController {

	@Autowired
	private FirestationService firestationService;

	private final Logger logger = LoggerFactory.getLogger(FirestationController.class);

	/**
	 * 
	 */
	@GetMapping("/firestations")
	public List<Firestation> getFirestations() throws Exception {
		logger.info("GET /firestations called");
		return firestationService.findAll();

	}

	@PostMapping("/firestation")
	public ResponseEntity<Firestation> saveFirestation(@RequestBody Firestation firestation) {
		logger.info("Request = @RequestBody = {}", firestation.toString());
		Firestation persistedFirestation = null;
		try {
			persistedFirestation = firestationService.saveFirestation(firestation);

		} catch (Exception e) {
			// TODO Auto-generated catch block

			e.printStackTrace();
		}
		logger.info("Response = @ResponseBody = {}", persistedFirestation.toString());
		return ResponseEntity.created(URI.create(String.format("/firestation"))).body(persistedFirestation);

	}

	@PutMapping("/firestation")
	public ResponseEntity<Firestation> updateFirestation(@RequestParam Integer stationId,
			@RequestBody Firestation firestation) {
		logger.info("Request = @RequestBody = {}", firestation.toString());
		try {
			Firestation frs = firestationService.findFirestation(firestation.getStation());
			String address = frs.getAddress();
			if (address != null) {
				frs.setAddress(firestation.getAddress());
			}
			Integer station = frs.getStation();
			if (station != null) {
				frs.setStation(firestation.getStation());
			}

			firestationService.updateFirestation(frs);
			logger.info("Response = @ResponseBody = {}", frs.toString());
			return ResponseEntity.ok().body(frs);

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@DeleteMapping("/firestation")
	public ResponseEntity<HttpStatus> deleteFirestation(@RequestParam Integer stationId)
			throws JsonParseException, JsonMappingException, IOException {
		firestationService.deleteFirestation(stationId);
		return new ResponseEntity<HttpStatus>(HttpStatus.ACCEPTED);
	}

	/**
	 * 
	 * http://localhost:8080/phoneAlert?firestation=<firestation_number>
	 * 
	 * @return List<Person>
	 * @throws JsonMappingException
	 */
	@GetMapping("/phoneAlert")
	public ResponseEntity<Map<String, List<String>>> getPhoneAlert(@RequestParam("firestation") Integer station_number)
			throws JsonMappingException {

		Map<String, List<String>> residents = firestationService.getResidentPhone(station_number);
		return new ResponseEntity<Map<String, List<String>>>(residents, HttpStatus.OK);
	}

	/**
	 * 
	 * http://localhost:8080/firestation?stationNumber=<station_number>
	 */
	@GetMapping("/firestation")
	public ResponseEntity<List<PersonInfoDTO>> getPersonDeserved(@RequestParam("stationNumber") Integer stationNumber)
			throws JsonMappingException {

		List<PersonInfoDTO> personsDeserved = firestationService.getPersonsCoveredByStation(stationNumber);
		return new ResponseEntity<List<PersonInfoDTO>>(personsDeserved, HttpStatus.OK);
	}

}
