package com.myapp.tracyweather.model;

public class County {

	private int id;
	private String countyName;
	private String countyId;
	private int cityId;
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getCountyName() {
		return countyName;
	}
	
	public void setCountyName(String countyName) {
		this.countyName = countyName;
	}
	
	public String getCountyId() {
		return countyId;
	}
	
	public void setCountyId(String countyId) {
		this.countyId = countyId;
	}
	
	public int getCityId() {
		return cityId;
	}
	
	public void setCityId(int cityId) {
		this.cityId = cityId;
	}
	
}
