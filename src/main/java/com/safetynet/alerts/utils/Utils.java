package com.safetynet.alerts.utils;

import java.time.LocalDate;
import java.time.Period;
import java.util.Date;

public class Utils {

	public static int calculateAge(Date bdate) {
		LocalDate today = LocalDate.now();
		LocalDate bDat = LocalDate.of(bdate.getYear(), bdate.getMonth(), bdate.getDay());
		Period period = Period.between(bDat, today);

		return period.getYears();

	}

}
