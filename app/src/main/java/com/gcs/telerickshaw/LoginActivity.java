package com.gcs.telerickshaw;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.gcs.utils.IncomingSms;
import com.gcs.utils.ServiceHandler;
import com.gcs.utils.Urlconstant;
import com.gcs.utils.User;
import com.gcs.utils.UserDatabaseHandler;
import com.gcs.utils.UserEmailFetcher;

public class LoginActivity extends Activity implements OnClickListener {
	
	Button loginButton;
	ProgressBar pbar;
	EditText editTextusername,editTextpassword;
	String userpassword,username;
	TextView forgotpassword;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_login);
		//setTitle("Sign in");
		init();
		overridePendingTransition(R.anim.trans_top_in, R.anim.trans_top_out);
	}
	
	
	Typeface typeFace;
	private void init() {
		typeFace=Typeface.createFromAsset(getAssets(),"Roboto-Regular.ttf");
		editTextusername=(EditText)findViewById(R.id.editTextusername);
		editTextpassword=(EditText)findViewById(R.id.editTextpassword);
		forgotpassword=(TextView)findViewById(R.id.forgotpassword_text);
		forgotpassword.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				forgotpwdialog();
			}
		});
		loginButton=(Button)findViewById(R.id.button_signin);
		loginButton.setOnClickListener(this);
		 pbar=(ProgressBar)findViewById(R.id.progressBar1);
		
		editTextusername.setTypeface(typeFace);
		editTextusername.setText(UserEmailFetcher.getEmail(getApplicationContext()));
		editTextpassword.setTypeface(typeFace);
	}

	 // CHANGE PHONE NUMBER DIALOG
	 private void forgotpwdialog() {
	    	LayoutInflater li = LayoutInflater.from(LoginActivity.this);
			View promptsView = li.inflate(R.layout.forgotpassworddialog, null);
			TextView heading = (TextView) promptsView.findViewById(R.id.textView_heading);
			heading.setTypeface(typeFace);
			final EditText edittext_email = (EditText) promptsView.findViewById(R.id.edittext_forgotpw);
			edittext_email.setTypeface(typeFace);
			edittext_email.setText(UserEmailFetcher.getEmail(getApplicationContext()));
			Button confirm = (Button)promptsView.findViewById(R.id.button_confirm);
			confirm.setTypeface(typeFace);
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(LoginActivity.this);
			alertDialogBuilder.setView(promptsView);
			
			
			alertDialog1 = null;
			
			if(alertDialog1==null)
			alertDialog1 = alertDialogBuilder.create();
			if(alertDialog1.isShowing()) {
				alertDialog1.dismiss();
			}
			alertDialog1.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
			
			
			alertDialog1.show();
			
			YoYo.with(Techniques.SlideInDown).duration(250)
           .playOn(alertDialog1.getWindow().getDecorView());
			
			
	        
	        confirm.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					
					username=edittext_email.getText().toString();
					if(username.length()>0){
						alertDialog1.dismiss();
						new forgotpw().execute();
					}
					else{
						Toast.makeText(LoginActivity.this, "Please enter email", Toast.LENGTH_SHORT).show();
					}
					
				}
			});
	    }
	 
	@Override
	public void onClick(View v) {
		if(v.equals(loginButton)){
			userpassword=editTextpassword.getText().toString();
			username=editTextusername.getText().toString();
		}
		
		if(username.length()>0&&userpassword.length()>0){
			new Signin().execute();
		}
		else{
			Toast.makeText(LoginActivity.this, "Please enter all details", Toast.LENGTH_SHORT).show();
		}
	
	}
	
	
	String useremail,userphn;
	private class Signin extends AsyncTask<Void, Void, Void> {
	   	 int isSuccessful=0;
	   	UserDatabaseHandler udata=new UserDatabaseHandler(getApplicationContext());
	   	 @Override
	        protected void onPreExecute() {
	            super.onPreExecute();
	            pbar.setVisibility(ProgressBar.VISIBLE);
	        }
	 
	        @Override
	        protected Void doInBackground(Void... params) {
	        	ServiceHandler sh=new ServiceHandler();
	            String url=Urlconstant.url_login+username+"&pw="+userpassword+"&type=login";
	            System.out.println("my url"+url);
	            url=url.replace(" ", "%20");
	            
				String response=sh.makeServiceCall(url,getApplicationContext(),ServiceHandler.GET);
	            System.out.println("rEs="+response);
	        	if(response !=null)
				{
					JSONObject jsonobj;
					try
					{
						jsonobj =new JSONObject(response);
						//String status=jsonobj.getString("signup");
						
							JSONArray jsonarr =jsonobj.getJSONArray("login");
							 System.out.println(jsonarr.get(0).toString());
							if(jsonarr.get(0).toString().compareTo("usSuccess")==0){
								isSuccessful=0;
							}
							else if(jsonarr.get(0).toString().compareTo("Success")==0){
								isSuccessful=1;
								String firsname=jsonarr.get(1).toString();
								String email=jsonarr.get(2).toString();
								String phone=jsonarr.get(3).toString();
								User user=new User(firsname,firsname,phone,email,userpassword);
								
								
								udata.addBrand(user);
							}
							else if(jsonarr.get(3).toString().compareTo("Account Not Verified")==0){
								isSuccessful=2;
								String email=jsonarr.get(1).toString();
								String phone=jsonarr.get(2).toString();
								String firsname=jsonarr.get(0).toString();
								
								User user=new User(firsname,firsname,phone,email,userpassword);
								udata.addBrand(user);
								useremail=email;
								userphn=phone;
								
								//---add account verification
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
	         
	       	 donext(isSuccessful);
	        }

	    }
	
	private class GetOTP extends AsyncTask<Void, Void, Void> {
	   	 int isSuccessful;
	   	 @Override
	        protected void onPreExecute() {
	            super.onPreExecute();
	            pbar.setVisibility(ProgressBar.VISIBLE);
	        }
	 
	        @Override
	        protected Void doInBackground(Void... params) {
	        	ServiceHandler sh=new ServiceHandler();
	            String url=Urlconstant.url_login+useremail+"&phonenumber="+userphn+"&pw="+userpassword+"&type=getotagain";
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
						
							JSONArray jsonarr =jsonobj.getJSONArray("getotp");
							 System.out.println(jsonarr.get(0).toString());
							 
						
							 if(jsonarr.get(1).toString().compareTo("Success")==0){
								isSuccessful=1;
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
	       	
	        }

	    }
	
	
	
	private class forgotpw extends AsyncTask<Void, Void, Void> {
	   	 int isSuccessful=0;
	   	 @Override
	        protected void onPreExecute() {
	            super.onPreExecute();
	            pbar.setVisibility(ProgressBar.VISIBLE);
	        }
	 
	        @Override
	        protected Void doInBackground(Void... params) {
	        	ServiceHandler sh=new ServiceHandler();
	            String url=Urlconstant.url_forgotpw+username+"&type=forget";
	            System.out.println("my url"+url);
	            url=url.replace(" ", "%20");
	            
				String response=sh.makeServiceCall(url,getApplicationContext(),ServiceHandler.GET);
	            System.out.println("rEs="+response);
	        	if(response !=null)
				{
					JSONObject jsonobj;
					try
					{
						jsonobj =new JSONObject(response);
						//String status=jsonobj.getString("signup");
						
							JSONArray jsonarr =jsonobj.getJSONArray("forget");
							 System.out.println(jsonarr.get(0).toString());
							if(jsonarr.get(0).toString().compareTo("UnSuccess")==0){
								isSuccessful=0;
							}
							else if(jsonarr.get(0).toString().compareTo("Success")==0){
								isSuccessful=1;
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
	         
	       	 donext2(isSuccessful);
	        }

	    }


	public void donext(int isSuccessful) {
		if(isSuccessful==1){
			popupdialog("Welcome to Telerickshaw", "Book your first ride",isSuccessful);
		}
		else if(isSuccessful==0){
			popupdialog("Oops!", "It seems you have entered a wrong username or password",isSuccessful);
		}
		else if(isSuccessful==2){
			otpdialog();
			new GetOTP().execute();
				
		}
		
		
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
		    	LayoutInflater li = LayoutInflater.from(LoginActivity.this);
				View promptsView = li.inflate(R.layout.enterotpddialog, null);
				
				TextView heading = (TextView) promptsView.findViewById(R.id.textView_heading);
				heading.setTypeface(typeFace);
				heading.setText("Account not verified");
				edittext_email = (EditText) promptsView.findViewById(R.id.edittext_forgotpw);
				edittext_email.setTypeface(typeFace);
				
				Button confirm = (Button)promptsView.findViewById(R.id.button_confirm);
				confirm.setTypeface(typeFace);
				
				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(LoginActivity.this);
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
				
				
				alertDialog1.show();
				alertDialog1.setCancelable(false);
				alertDialog1.setCanceledOnTouchOutside(false);
				
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
		 
		 
		//---------------verify otp-------------------------
			private class verifyOTP extends AsyncTask<Void, Void, Void> {
			   	 int isSuccessful;
			   	 @Override
			        protected void onPreExecute() {
			            super.onPreExecute();
			            pbar.setVisibility(ProgressBar.VISIBLE);
			        }

			   	 
			   	 @Override
			        protected Void doInBackground(Void... params) {
			        	ServiceHandler sh=new ServiceHandler();
			            String url=Urlconstant.url_verifyotp+useremail+"&phonenumber="+userphn+"&otp_no="+otpstring+"&type=otpverify";
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
										isSuccessful=0;
									}
									else if(jsonarr.get(0).toString().compareTo("success")==0){
										isSuccessful=1;
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
			         donext(1);
			        }

			    }
//			
	
	public void donext2(int isSuccessful) {
		if(isSuccessful==1){
			popupdialog2("Forgot password request", "We have sent your password on your registred Email",isSuccessful);
		}
		else if(isSuccessful==0){
			popupdialog2("Oops!", "It seems you have entered a wrong Email",isSuccessful);
		}
		
		
		
	}
	
	AlertDialog alertDialog1 = null;
	private void popupdialog(String title,String message,final int isSuccessful) {
    	LayoutInflater li = LayoutInflater.from(LoginActivity.this);
		View promptsView = li.inflate(R.layout.custom_dialog, null);
		TextView titleview=(TextView)promptsView.findViewById(R.id.textView1);
		TextView descview=(TextView)promptsView.findViewById(R.id.textViewcompletedes);
		Button next=(Button)promptsView.findViewById(R.id.button_close);
		
		titleview.setText(title);
		descview.setText(message);
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(LoginActivity.this);
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
				if(isSuccessful==1){
					alertDialog1.dismiss();
					startActivity(new Intent(LoginActivity.this,ActionbarActivity.class));
					//User has successfully logged in, save this information
					 //We need an Editor object to make preference changes.
					SharedPreferences settings = getSharedPreferences(splashActivity.PREFS_NAME, 0); // 0 - for private mode
					SharedPreferences.Editor editor = settings.edit();
		
					//Set "hasLoggedIn" to true
					editor.putBoolean("hasLoggedIn", true);
					
					// Commit the edits!
					editor.commit();
					
					finish();
				}
				else{
					alertDialog1.dismiss();
				}
			}
		});
	
	}
	
	private void popupdialog2(String title,String message,final int isSuccessful) {
    	LayoutInflater li = LayoutInflater.from(LoginActivity.this);
		View promptsView = li.inflate(R.layout.custom_dialog, null);
		TextView titleview=(TextView)promptsView.findViewById(R.id.textView1);
		TextView descview=(TextView)promptsView.findViewById(R.id.textViewcompletedes);
		Button next=(Button)promptsView.findViewById(R.id.button_close);
		
		titleview.setText(title);
		descview.setText(message);
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(LoginActivity.this);
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
					
					alertDialog1.dismiss();
			}
		});
	
	}
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		overridePendingTransition(R.anim.trans_bottom_in, R.anim.trans_bottom_out);
	}
	
	

}
