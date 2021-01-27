package com.safetynet.alerts.model;

import lombok.Data;

@Data
public class Person {

	private String lastName;

	private String firstName;

	private String address;
	private String city;
	private String zip;
	private String phone;
	private String email;

	public Person(String fname, String lname, String address, String city, String zip, String phone, String email) {
		this.setLastName(lname);
		this.setFirstName(fname);
		this.setAddress(address);
		this.setCity(city);
		this.setZip(zip);
		this.setPhone(phone);
		this.setEmail(email);
	}

}
