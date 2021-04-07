package com.safetynet.alerts.model.dto;

import java.util.List;

public class PersonCoveredDTO {
	String nom;
	String prenom;
	String phone;
	int age;
	String address;

	List<PersonInfoDTO> siblingPersons;

	public PersonCoveredDTO(String nom, String prenom, String phone, int age, String address) {
		this.nom = nom;
		this.prenom = prenom;
		this.phone = phone;
		this.age = age;
		this.address = address;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getPrenom() {
		return prenom;
	}

	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public List<PersonInfoDTO> getSiblingPersons() {
		return siblingPersons;
	}

	public void setSiblingPersons(List<PersonInfoDTO> siblingPersons) {
		this.siblingPersons = siblingPersons;
	}
}
