package com.safetynet.alerts.controller;

import java.io.IOException;
import java.net.URI;
import java.util.List;

import com.safetynet.alerts.exception.FirestationNotFoundException;
import com.safetynet.alerts.model.dto.ChildrenCoveredDTO;
import com.safetynet.alerts.model.dto.PersonsCoveredByStation;
import com.safetynet.alerts.model.dto.StationCoverageDTO;
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
			if (station != null ) {
				frs.setStation(firestation.getStation());
			}

			firestationService.updateFirestation(frs);
			logger.info("Response = @ResponseBody = {}", frs.toString());
			return ResponseEntity.ok().body(frs);

		}catch(FirestationNotFoundException fne){
			return null;
		}
		catch (Exception e) {
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
	 * @return List<String>
	 * @throws JsonMappingException
	 */
	@GetMapping("/phoneAlert")
	public ResponseEntity<List<String>> getPhoneAlert(@RequestParam("firestation") Integer station_number)
			throws JsonMappingException {
		logger.info("Request: retrieve phone number for residents covered by station {} ", station_number);
		List<String> residents = firestationService.getResidentPhone(station_number);

		logger.info("Response: phone number for residents covered by station {} : {}", station_number, residents);
		return new ResponseEntity<List<String>>(residents, HttpStatus.OK);
	}

	/**
	 * 
	 * http://localhost:8080/firestation?stationNumber=<station_number>
	 */
	@GetMapping("/firestation")
	public ResponseEntity<PersonsCoveredByStation> getPersonDeserved(@RequestParam("stationNumber") Integer stationNumber)
			throws JsonMappingException {
		logger.info("Request: retrieve Persons covered by station {}", stationNumber);
		PersonsCoveredByStation personsDeserved = firestationService.getPersonsCoveredByStation(stationNumber);

		logger.info("Response : persons covered by station {}: {}", stationNumber,personsDeserved.toString());
		return new ResponseEntity<PersonsCoveredByStation>(personsDeserved, HttpStatus.OK);
	}

	/**
	 *
	 * @param address
	 * @return
	 * @throws JsonMappingException
	 *  http://localhost:8080/childAlert?address=<address>
	 */
	@GetMapping("/childAlert")
	public ResponseEntity<ChildrenCoveredDTO> getChildrenCoveredByStation(@RequestParam("address") String address)
			throws JsonMappingException {
		logger.info("Request: retrieve children covered at address {}", address);
		ChildrenCoveredDTO childrenDeserved = firestationService.getChildrenCovered(address);

		logger.info("Response : persons covered at address {} : {}",address, childrenDeserved);
		return new ResponseEntity<ChildrenCoveredDTO>(childrenDeserved, HttpStatus.OK);
	}


	/**
	 *
	 * @param address
	 * @return
	 * @throws JsonMappingException
	 *
	 * http://localhost:8080/fire?address=<address>
	 */
	@GetMapping("/fire")
	public ResponseEntity<PersonsCoveredByStation> getPersonsCoveredAtAddress(@RequestParam("address") String address) throws JsonMappingException{

		logger.info("Request: retrieve persons covered at address: {}", address);
		PersonsCoveredByStation personsCovered = firestationService.getStationAndPersonsCoveredAtAddress(address);
		logger.info("Response : persons covered at address: {}", personsCovered);
		return new ResponseEntity<PersonsCoveredByStation>(personsCovered, HttpStatus.OK);
	}


	/**
	 * retrieveStationsCoverage - Display the coverage of a list of stations
	 * @param stations
	 * @return
	 * @throws JsonMappingException
	 */
	@GetMapping("/flood/stations")
	public ResponseEntity<List<StationCoverageDTO>> retrieveStationsCoverage(@RequestParam("stations") List<Integer> stations) throws JsonMappingException{
		logger.info("Request: retrieve stations coverage: {}", stations);
		List<StationCoverageDTO> stationsCoverage=firestationService.getStationsCoverage(stations);
		logger.info("Response : stations coverage: {}", stationsCoverage);
		return new ResponseEntity<List<StationCoverageDTO>>(stationsCoverage, HttpStatus.OK);
	}
}
