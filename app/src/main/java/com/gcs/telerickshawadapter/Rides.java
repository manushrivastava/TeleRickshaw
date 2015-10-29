package com.gcs.telerickshawadapter;

public class Rides {
	
	String bookingid,driverid,drivername,drivercontact,driverautonumber,driverlat,driverlong,status;
	int id_auto;

	public Rides(String bookingid, String drivername, String driverid,String driverlat,
			String driverlong,String driverautonum,String drivercont,String status) {
		this.bookingid=bookingid;
		this.drivername=drivername;
		this.driverid=driverid;
		this.driverlat=driverlat;
		this.driverlong=driverlong;
		this.driverautonumber=driverautonum;
		this.drivercontact=drivercont;
		this.status=status;
	}

	public Rides(int id,String bookingid, String drivername, String driverid,String driverlat,
			String driverlong,String driverautonum,String drivercont,String driverstatus) {
		this.id_auto=id;
		this.bookingid=bookingid;
		this.drivername=drivername;
		this.driverid=driverid;
		this.driverlat=driverlat;
		this.driverlong=driverlong;
		this.driverautonumber=driverautonum;
		this.drivercontact=drivercont;
		this.status=status;
	}

	public Rides() {
		
	}


	public int getId_auto() {
		return id_auto;
	}



	public void setId_auto(int id_auto) {
		this.id_auto = id_auto;
	}



	public String getBookingid() {
		return bookingid;
	}


	public void setBookingid(String bookingid) {
		this.bookingid = bookingid;
	}


	public String getDriverid() {
		return driverid;
	}


	public void setDriverid(String driverid) {
		this.driverid = driverid;
	}


	public String getDrivername() {
		return drivername;
	}


	public void setDrivername(String drivername) {
		this.drivername = drivername;
	}


	public String getDrivercontact() {
		return drivercontact;
	}


	public void setDrivercontact(String drivercontact) {
		this.drivercontact = drivercontact;
	}


	public String getDriverautonumber() {
		return driverautonumber;
	}


	public void setDriverautonumber(String driverautonumber) {
		this.driverautonumber = driverautonumber;
	}


	public String getDriverlat() {
		return driverlat;
	}


	public void setDriverlat(String driverlat) {
		this.driverlat = driverlat;
	}


	public String getDriverlong() {
		return driverlong;
	}


	public void setDriverlong(String driverlong) {
		this.driverlong = driverlong;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	


}
