package com.safetynet.alerts.service;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.alerts.model.AlertsData;

public abstract class AbstractService {

	@Value("${json.file.location}")
	Resource jsonFile;

	public AlertsData jsonDataReader() throws JsonMappingException {
		ObjectMapper objectMapper = new ObjectMapper();
		AlertsData aData = null;
		try {
			aData = objectMapper.readValue(jsonFile.getFile(), AlertsData.class);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return aData;
	}

}
