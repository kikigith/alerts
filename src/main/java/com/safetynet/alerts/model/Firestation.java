package com.safetynet.alerts.model;

public class Firestation {

	private Integer station;
	private String address;

	public Firestation() {

	}

	public Firestation(Integer station, String address) {
		super();
		this.station = station;
		this.address = address;
	}

	public Firestation(Integer station) {
		super();
		this.station = station;
	}

	public Integer getStation() {
		return station;
	}

	public void setStation(Integer station) {
		this.station = station;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((station == null) ? 0 : station.hashCode());
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
		Firestation other = (Firestation) obj;
		if (station == null) {
			if (other.station != null)
				return false;
		} else if (!station.equals(other.station))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Firestation [station=" + station + ", address=" + address + "]";
	}

}
