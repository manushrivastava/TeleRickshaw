package com.gcs.utils;

public class SearchHistory {
	
	public  String address;
	int id_auto;
	
	public SearchHistory(String add) {
		address=add;
	}
	
	public SearchHistory(int id,String add) {
		address=add;
		id_auto=id;
	}
	
	public SearchHistory() {
		// TODO Auto-generated constructor stub
	}
	
	
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public int getId_auto() {
		return id_auto;
	}
	public void setId_auto(int id_auto) {
		this.id_auto = id_auto;
	}
	
	
	

}
