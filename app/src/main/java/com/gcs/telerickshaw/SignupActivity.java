package com.gcs.telerickshaw;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.gcs.utils.IncomingSms;
import com.gcs.utils.ServiceHandler;
import com.gcs.utils.Urlconstant;
import com.gcs.utils.UserEmailFetcher;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class SignupActivity extends Activity implements OnClickListener {

	Button signupButton;
	EditText editTextfirstname,editTextlastname,editTextusername,editTextphonenumber,editTextpassword;
	String userfirstname,userlastname,useremail,userpassword,usernumber;
	ProgressBar pbar;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_signup);
		//setTitle("Create account");
		init();
		overridePendingTransition(R.anim.trans_top_in, R.anim.trans_top_out);
	}
	
	private void init() {
		Typeface typeFace=Typeface.createFromAsset(getAssets(),"Roboto-Regular.ttf");
		editTextfirstname=(EditText)findViewById(R.id.editTextfirstname);
		editTextlastname=(EditText)findViewById(R.id.editTextlastname);
		editTextpassword=(EditText)findViewById(R.id.editTextpassword);
		editTextphonenumber=(EditText)findViewById(R.id.editTextphonenumber);
		editTextusername=(EditText)findViewById(R.id.editTextusername);
		pbar=(ProgressBar)findViewById(R.id.progressBar1);
		signupButton=(Button)findViewById(R.id.button_signin);
		signupButton.setOnClickListener(this);
		
		editTextfirstname.setTypeface(typeFace);
		editTextlastname.setTypeface(typeFace);
		editTextpassword.setTypeface(typeFace);
		editTextphonenumber.setTypeface(typeFace);
		editTextusername.setTypeface(typeFace);
		
		editTextusername.setText(UserEmailFetcher.getEmail(getApplicationContext()));
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		overridePendingTransition(R.anim.trans_bottom_in, R.anim.trans_bottom_out);
	}
	

	@Override
	public void onClick(View v) {
		if(v.equals(signupButton)){
			
			
			userfirstname=editTextfirstname.getText().toString();
			userlastname=editTextlastname.getText().toString();
			usernumber=editTextphonenumber.getText().toString();
			userpassword=editTextpassword.getText().toString();
			useremail=editTextusername.getText().toString();
			
			if(userfirstname.length()>0&&userlastname.length()>0&&usernumber.length()>0&&
					userpassword.length()>0&&useremail.length()>0){
				new Signup().execute();
			}
			else{
				Toast.makeText(SignupActivity.this, "Please enter all details", Toast.LENGTH_SHORT).show();
			}
			
			
		}
				
	}
	
	
	AlertDialog alertDialog1 = null;
	private void popupdialog(String title,String message,final boolean isSuccessful) {
    	LayoutInflater li = LayoutInflater.from(SignupActivity.this);
		View promptsView = li.inflate(R.layout.custom_dialog, null);
		TextView titleview=(TextView)promptsView.findViewById(R.id.textView1);
		TextView descview=(TextView)promptsView.findViewById(R.id.textViewcompletedes);
		Button next=(Button)promptsView.findViewById(R.id.button_close);
		
		titleview.setText(title);
		descview.setText(message);
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(SignupActivity.this);
		alertDialogBuilder.setView(promptsView);
		alertDialog1 = null;
		
		if(alertDialog1==null)
		alertDialog1 = alertDialogBuilder.create();
		if(alertDialog1.isShowing()) {
			alertDialog1.dismiss();
		}
		alertDialog1.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
		
		alertDialog1.show();
		
		
		next.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(isSuccessful){
					startActivity(new Intent(SignupActivity.this,LoginActivity.class));
					alertDialog1.dismiss();
					finish();
				}
				else{
					
					alertDialog1.dismiss();
				}
			}
		});
	
	}
	
	
	private void popupalreadyexistdialog(String title,String message,final boolean isSuccessful) {
    	LayoutInflater li = LayoutInflater.from(SignupActivity.this);
		View promptsView = li.inflate(R.layout.custom_dialog, null);
		TextView titleview=(TextView)promptsView.findViewById(R.id.textView1);
		TextView descview=(TextView)promptsView.findViewById(R.id.textViewcompletedes);
		Button next=(Button)promptsView.findViewById(R.id.button_close);
		
		titleview.setText(title);
		descview.setText(message);
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(SignupActivity.this);
		alertDialogBuilder.setView(promptsView);
		alertDialog1 = null;
		
		if(alertDialog1==null)
		alertDialog1 = alertDialogBuilder.create();
		if(alertDialog1.isShowing()) {
			alertDialog1.dismiss();
		}
		alertDialog1.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
		
		alertDialog1.show();
		
		
		next.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(isSuccessful){
					startActivity(new Intent(SignupActivity.this,LoginActivity.class));
					alertDialog1.dismiss();
					finish();
				}
				else{
					startActivity(new Intent(SignupActivity.this,LoginActivity.class));
					alertDialog1.dismiss();
					finish();
				}
			}
		});
	
	}
	
	
	private final Handler handler = new Handler();
	
	  //-----------------------------------------GET OTP----------
    private Runnable checkOTp = new Runnable() {
        public void run() {
                handler.postDelayed(this, 5000); // 60 seconds here you can give your time
                if(IncomingSms.otp!=null)
                edittext_email.setText(IncomingSms.otp);
               // Toast.makeText(getApplicationContext(), "Hi location update"+latitude+","+longitude, Toast.LENGTH_SHORT).show();        
                
        }
    };    
    
       
         EditText edittext_email;
         String otpstring="";
		 private void otpdialog() {
			 Typeface typeFace=Typeface.createFromAsset(getAssets(),"Roboto-Regular.ttf");
		    	LayoutInflater li = LayoutInflater.from(SignupActivity.this);
				View promptsView = li.inflate(R.layout.enterotpddialog, null);
				
				TextView heading = (TextView) promptsView.findViewById(R.id.textView_heading);
				heading.setTypeface(typeFace);
				
				edittext_email = (EditText) promptsView.findViewById(R.id.edittext_forgotpw);
				edittext_email.setTypeface(typeFace);
				
				Button confirm = (Button)promptsView.findViewById(R.id.button_confirm);
				confirm.setTypeface(typeFace);
				
				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(SignupActivity.this);
				alertDialogBuilder.setView(promptsView);
				
				handler.removeCallbacks(checkOTp);
		        handler.postDelayed(checkOTp, 5000); // 1 second
				
				alertDialog1 = null;
				
				if(alertDialog1==null)
				alertDialog1 = alertDialogBuilder.create();
				if(alertDialog1.isShowing()) {
					alertDialog1.dismiss();
				}
				alertDialog1.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
				
				alertDialog1.setCancelable(false);
				alertDialog1.setCanceledOnTouchOutside(false);
				alertDialog1.show();
				
				YoYo.with(Techniques.SlideInDown).duration(250)
	           .playOn(alertDialog1.getWindow().getDecorView());
				
				
		        
		        confirm.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {

						
						otpstring=edittext_email.getText().toString();
						if(otpstring.length()>0){
							new verifyOTP().execute();
							alertDialog1.dismiss();
						}
						
					}
				});
		    }
		 
	private class Signup extends AsyncTask<Void, Void, Void> {
	   	 boolean isSuccessful=false;
	   	 @Override
	        protected void onPreExecute() {
	            super.onPreExecute();
	            pbar.setVisibility(ProgressBar.VISIBLE);
	        }
	 
	        @Override
	        protected Void doInBackground(Void... params) {
	        	ServiceHandler sh=new ServiceHandler();
	            String url=Urlconstant.url_signup+userfirstname+"&email="+useremail+"&phonenumber="+usernumber+"&pw="+userpassword+"&type=signup";
	            url=url.replace(" ", "%20");
	            
				String response=sh.makeServiceCall(url,getApplicationContext(),ServiceHandler.GET);
	            System.out.println(url+"rEs="+response);
	        	if(response !=null)
				{
					JSONObject jsonobj;
					try
					{
						jsonobj =new JSONObject(response);
						//String status=jsonobj.getString("signup");
						
							JSONArray jsonarr =jsonobj.getJSONArray("signup");
							 System.out.println(jsonarr.get(0).toString());
							 
							if(jsonarr.get(0).toString().compareTo("Already Exists")==0){
								isSuccessful=false;
							}
							else if(jsonarr.get(0).toString().compareTo("Success")==0){
								isSuccessful=true;
							}
						
						
					}catch(JSONException e)
					{
						e.printStackTrace();
					}
				}
				return null;
	        }
	        
	        @Override
	        protected void onPostExecute(Void result) {
	       	 super.onPostExecute(null);
	         pbar.setVisibility(ProgressBar.INVISIBLE);
	         System.out.println("success");
	       	 donext(isSuccessful);
	        }

	    }
	
	
	


	public void donext(boolean isSuccessful) {
		if(isSuccessful){
			System.out.println("welcome");
			otpdialog();
			//popupdialog("Welcome to Telerickshaw", "Please verify your account by clicking the link on your mail",isSuccessful);
		}
		else{
			System.out.println("not welcome");
			popupalreadyexistdialog("Already Registered", "You are already registered with Telerickshaw. Please login to continue.",isSuccessful);
		}
		
		
	}
	
	//---------------verify otp-------------------------
	
	 
	private class verifyOTP extends AsyncTask<Void, Void, Void> {
	   	 boolean isSuccessful=false;
	   	 @Override
	        protected void onPreExecute() {
	            super.onPreExecute();
	            pbar.setVisibility(ProgressBar.VISIBLE);
	        }
	 
	        @Override
	        protected Void doInBackground(Void... params) {
	        	ServiceHandler sh=new ServiceHandler();
	            String url=Urlconstant.url_verifyotp+useremail+"&phonenumber="+usernumber+"&otp_no="+otpstring+"&type=otpverify";
	            url=url.replace(" ", "%20");
	            
				String response=sh.makeServiceCall(url,getApplicationContext(),ServiceHandler.GET);
	            System.out.println(url+"rEs="+response);
	        	if(response !=null)
				{
					JSONObject jsonobj;
					try
					{
						jsonobj =new JSONObject(response);
						//String status=jsonobj.getString("signup");
						
							JSONArray jsonarr =jsonobj.getJSONArray("opt");
							 System.out.println(jsonarr.get(0).toString());
							if(jsonarr.get(0).toString().compareTo("UnSuccess")==0){
								isSuccessful=false;
							}
							else if(jsonarr.get(0).toString().compareTo("Success")==0){
								isSuccessful=true;
							}
						
						
					}catch(JSONException e)
					{
						e.printStackTrace();
					}
				}
				return null;
	        }
	        
	        @Override
	        protected void onPostExecute(Void result) {
	       	 super.onPostExecute(null);
	         pbar.setVisibility(ProgressBar.INVISIBLE);
	         System.out.println("success");
	       	 donext2(isSuccessful);
	        }

	    }
	
	  
	  public void donext2(boolean isSuccessful) {
		if(isSuccessful){
			popupdialog("Congratulations", "Your account has been verfied.Please login and start booking",isSuccessful);
		}
		else{
			popupdialog("Wrong OTP", "You have entered a wrong OTP code",isSuccessful);
		}
	}
	
	
}
