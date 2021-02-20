package com.safetynet.alerts.model;

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

	public Person(String fname, String lname) {
		this.setFirstName(fname);
		this.setLastName(lname);
	}

	public Person() {
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((firstName == null) ? 0 : firstName.hashCode());
		result = prime * result + ((lastName == null) ? 0 : lastName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Person other = (Person) obj;
		if (firstName == null) {
			if (other.firstName != null)
				return false;
		} else if (!firstName.equals(other.firstName))
			return false;
		if (lastName == null) {
			if (other.lastName != null)
				return false;
		} else if (!lastName.equals(other.lastName))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Person [lastName=" + lastName + ", firstName=" + firstName + ", address=" + address + ", city=" + city
				+ ", zip=" + zip + ", phone=" + phone + ", email=" + email + "]";
	}

}
