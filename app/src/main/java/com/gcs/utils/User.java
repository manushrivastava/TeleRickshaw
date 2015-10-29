package com.gcs.utils;

public class User {
	
	public  String userfirstname,usernumber,useremail,userpassword,userlastname;
	int id_auto;
	
	
	public User(int id,String userfirstname,String userlastname,String phonenumber,String email, String password) {
		this.userfirstname=userfirstname;
		this.userlastname=userlastname;
		this.userpassword=password;
		this.usernumber=phonenumber;
		this.useremail=email;
		this.id_auto=id;
	}

	
	public User(String userfirstname,String userlastname,String phonenumber,String email, String password) {
		this.userfirstname=userfirstname;
		this.userlastname=userlastname;
		this.userpassword=password;
		this.usernumber=phonenumber;
		this.useremail=email;
	}


	public User() {
		// TODO Auto-generated constructor stub
	}

	public String getUserfirstname() {
		return userfirstname;
	}

	public void setUserfirstname(String userfirstname) {
		this.userfirstname = userfirstname;
	}

	public String getUsernumber() {
		return usernumber;
	}

	public void setUsernumber(String usernumber) {
		this.usernumber = usernumber;
	}

	public String getUseremail() {
		return useremail;
	}

	public void setUseremail(String useremail) {
		this.useremail = useremail;
	}

	public String getUserpassword() {
		return userpassword;
	}

	public void setUserpassword(String userpassword) {
		this.userpassword = userpassword;
	}

	public String getUserlastname() {
		return userlastname;
	}

	public void setUserlastname(String userlastname) {
		this.userlastname = userlastname;
	}


	public int getId_auto() {
		return id_auto;
	}


	public void setId_auto(int id_auto) {
		this.id_auto = id_auto;
	}

	
	
	

}
