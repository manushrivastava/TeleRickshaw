package com.gcs.telerickshaw;

public class Locations  {
	
	String latitue,longitude;

	

	public Locations(String lati, String longi) {
		setLatitue(lati);
		setLongitude(longi);
	}

	public String getLatitue() {
		return latitue;
	}

	public void setLatitue(String latitue) {
		this.latitue = latitue;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	
	

}
