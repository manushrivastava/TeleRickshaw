package com.gcs.telerickshaw;

public class RickshawDetails {
	
	String latitude,longitude;
	String driverid;
	
	public RickshawDetails(String latitude,String longitude,String driverid) {
		this.latitude=latitude;
		this.longitude=longitude;
		this.driverid=driverid;
	}
	
	public String getLatitude() {
		return latitude;
	}
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	public String getLongitude() {
		return longitude;
	}
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	public String getDriverid() {
		return driverid;
	}
	public void setDriverid(String driverid) {
		this.driverid = driverid;
	}
	
	
	
	

}
