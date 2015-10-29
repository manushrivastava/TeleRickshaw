package com.gcs.telerickshaw;


import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.gcs.utils.ServiceHandler;
import com.gcs.utils.Urlconstant;
import com.gcs.utils.User;
import com.gcs.utils.UserDatabaseHandler;
import com.google.android.gms.common.api.GoogleApiClient;
import com.loadindicators.adrianlesniak.library.LoaderType;
import com.loadindicators.adrianlesniak.library.LoaderView;

public class splashActivity extends Activity
{
	private static long Timeout=5000;
	public static int Measuredwidth;
	public static int Measuredheight;
	public static String emailid;
	//Give your SharedPreferences file a name and save it to a static variable
	public static final String PREFS_NAME = "MyPrefsFile";
	public static ArrayList<User> userlist=new ArrayList<User>();
	private GoogleApiClient googleApiClient;
	Button call;
	ProgressBar pbar;
	LoaderView loader;
	//CircleProgress mprogress;
	//LoaderView lv;
	String deviceName,deviceMan;
	ImageView bg;
	TextView retry;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
	super.onCreate(savedInstanceState);
	this.requestWindowFeature(Window.FEATURE_NO_TITLE);
	this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
			WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.spalsh);
		 deviceName = android.os.Build.MODEL;
		 deviceMan = android.os.Build.MANUFACTURER;
		 System.out.println("deviceMan="+deviceMan);
		//PanningView panningview = (PanningView)findViewById(R.id.panningView);
		//pbar=(ProgressBar)findViewById(R.id.progressBar1);
		call=(Button)findViewById(R.id.button_call);
		call.setVisibility(View.INVISIBLE);
		Typeface typeFace=Typeface.createFromAsset(getAssets(),"Roboto-Regular.ttf");
		call.setTypeface(typeFace);
		loader = (LoaderView)  findViewById(R.id.imageLoader);
		bg = (ImageView)  findViewById(R.id.panningView);
		
		bg.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(loader.getVisibility()==View.GONE){
					loader.setVisibility(View.VISIBLE);
					retry.setVisibility(View.GONE);
					new Signin().execute();
				}
			}
		});
		
		retry = (TextView)  findViewById(R.id.textViewRetry);
		retry.setVisibility(View.GONE);
		loader.setLoader(LoaderType.MINI_BALLS);
		call.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String contactno="9098098098";
				Intent callIntent = new Intent(Intent.ACTION_CALL);
				callIntent.setData(Uri.parse("tel:"+contactno));
				startActivity(callIntent);
			}
		});
	//	lv=(LoaderView)findViewById(R.id.imageLoader);
		
//		mprogress=(CircleProgress)findViewById(R.id.progress);
//		mprogress.setRadius(1f);
//		 mprogress.startAnim();
//		mprogress.setDuration();
//		mprogress.setInterpolator();
		//panningview.startPanning();
		Timer timer=new Timer();
		ServiceHandler sh =new ServiceHandler();
		boolean isonline=sh.isOnline(getApplicationContext());
		
		if(isonline){
		timer.schedule (new  TimerTask() {
			
			@Override
			public void run()
			{
				
				SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
				//Get "hasLoggedIn" value. If the value doesn't exist yet false is returned
				boolean hasLoggedIn = settings.getBoolean("hasLoggedIn", false);

				if(hasLoggedIn)
				{
					this.cancel();
					start();
				}
				else{
					 // getting GPS status
					
			       ContentResolver contentResolver = getBaseContext().getContentResolver();  
			       boolean isGPSEnabled = Settings.Secure.isLocationProviderEnabled(  
			         contentResolver, LocationManager.GPS_PROVIDER);  
			       
			      
			       if(isGPSEnabled){
			    	   Intent intent=new Intent(splashActivity.this,WelcomeActivity.class);
						startActivity(intent);
						finish();
						}
			       else
			    	   runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							showSettingsAlert();
						}
					});
					
				}
				
			    
			}
		},Timeout);
		}
		else{
			Toast.makeText(getApplicationContext(), "Please check your Internet connection", Toast.LENGTH_LONG).show();
			loader.setVisibility(View.GONE);
			retry.setVisibility(View.VISIBLE);
			call.setVisibility(View.VISIBLE);
		}
		
	}
	
	
	protected void start() {
		 // getting GPS status
		
	       ContentResolver contentResolver = getBaseContext().getContentResolver();  
	       boolean isGPSEnabled = Settings.Secure.isLocationProviderEnabled(  
	         contentResolver, LocationManager.GPS_PROVIDER);  
	       
     
       if(isGPSEnabled)
		new Signin().execute();
       else
    	   runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				showSettingsAlert();
			}
		});
	}
	
//	GoogleApiClient googleApiClient;
//	public void gpson(){
//		if (googleApiClient == null) {
//	        googleApiClient = new GoogleApiClient.Builder(getActivity())
//	                .addApi(LocationServices.API)
//	                .addConnectionCallbacks(this)
//	                .addOnConnectionFailedListener(this).build();
//	            googleApiClient.connect();
//
//	            LocationRequest locationRequest = LocationRequest.create();
//	            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
//	            locationRequest.setInterval(30 * 1000);
//	            locationRequest.setFastestInterval(5 * 1000);
//	            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
//	                    .addLocationRequest(locationRequest);
//
//	            //**************************
//	            builder.setAlwaysShow(true); //this is the key ingredient
//	            //**************************
//
//	            PendingResult<LocationSettingsResult> result =
//	                    LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
//	            result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
//	                @Override
//	                public void onResult(LocationSettingsResult result) {
//	                    final Status status = result.getStatus();
//	                    final LocationSettingsStates state = result.getLocationSettingsStates();
//	                    switch (status.getStatusCode()) {
//	                        case LocationSettingsStatusCodes.SUCCESS:
//	                            // All location settings are satisfied. The client can initialize location
//	                            // requests here.
//	                            break;
//	                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
//	                            // Location settings are not satisfied. But could be fixed by showing the user
//	                            // a dialog.
//	                            try {
//	                                // Show the dialog by calling startResolutionForResult(),
//	                                // and check the result in onActivityResult().
//	                                status.startResolutionForResult(
//	                                        getActivity(), 1000);
//	                            } catch (IntentSender.SendIntentException e) {
//	                                // Ignore the error.
//	                            }
//	                            break;
//	                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
//	                            // Location settings are not satisfied. However, we have no way to fix the
//	                            // settings so we won't show the dialog.
//	                            break;
//	                    }
//	                }
//	            });             }
//	}
	/**
     * Function to show settings alert dialog
     * */
    public void showSettingsAlert(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(splashActivity.this);
      
        // Setting Dialog Title
        alertDialog.setTitle("GPS Permission");
  
        // Setting Dialog Message
        alertDialog.setMessage("Telerickshaw needs GPS to be switched on. Do you want to continue?");
  
        // Setting Icon to Dialog
        //alertDialog.setIcon(R.drawable.delete);
  
        // On pressing Settings button
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
            	finish();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
             //   turnGpsOn(getApplicationContext());
            }
        });
  
        // on pressing cancel button
        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            dialog.cancel();
            finish();
            }
        });
  
        // Showing Alert Message
        alertDialog.show();
    }


	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		finish();
	}
	
	
	
	
	private class Signin extends AsyncTask<Void, Void, Void> {
	   	 int isSuccessful=0;String uu;
	     UserDatabaseHandler udata=new UserDatabaseHandler(getApplicationContext());
	   	
	   	 @Override
	        protected void onPreExecute() {
	            super.onPreExecute();
	            //pbar.setVisibility(ProgressBar.VISIBLE);
	           
	            userlist=udata.getAllBrand();
	        }
	 
	        @Override
	        protected Void doInBackground(Void... params) {
	        	ServiceHandler sh=new ServiceHandler();
	            String url=Urlconstant.url_login+userlist.get(0).getUseremail()+"&pw="+userlist.get(0).getUserpassword()+"&type=login";
	            url=url.replace(" ", "%20");
	            System.out.println("login"+url);
	            uu=url;
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
							
							}
							else if(jsonarr.get(0).toString().compareTo("Account Not Verified")==0){
								isSuccessful=2;
							}
						
						
					}catch(JSONException e)
					{
						e.printStackTrace();
						System.out.println(e.getMessage());
					}
				}
	        	else{
	        		isSuccessful=-1;
	        	}
				return null;
	        }
	        
	        @Override
	        protected void onPostExecute(Void result) {
	       	 super.onPostExecute(null);
	        // pbar.setVisibility(ProgressBar.INVISIBLE);
	         donext(isSuccessful,uu);
	        }

	    }


	public void donext(int isSuccessful,String uu) {
		if(isSuccessful==1){
			startActivity(new Intent(splashActivity.this,ActionbarActivity.class));
			finish();
			//popupdialog("Welcome to Telerickshaw", "Book your first ride",isSuccessful);
		}
		else if(isSuccessful==0){
			startActivity(new Intent(splashActivity.this,ActionbarActivity.class));
			finish();
			//popupdialog("Oops!", "You have entered wrong username or password"+uu,isSuccessful);
		}
		
		else if(isSuccessful==2){
			popupdialog("Not Verified!", "Please verify your account by clicking the link on your registered email"+uu,isSuccessful);
		}
		else if(isSuccessful==-1){
			Toast.makeText(getApplicationContext(), "Please check your Internet connection", Toast.LENGTH_LONG).show();
			loader.setVisibility(View.GONE);
			retry.setVisibility(View.VISIBLE);
			call.setVisibility(View.VISIBLE);
	
		}
		
	}
	
	AlertDialog alertDialog1 = null;
	
	private void popupdialog(String title,String message,final int isSuccessful) {
    	LayoutInflater li = LayoutInflater.from(splashActivity.this);
		View promptsView = li.inflate(R.layout.custom_dialog, null);
		TextView titleview=(TextView)promptsView.findViewById(R.id.textView1);
		TextView descview=(TextView)promptsView.findViewById(R.id.textViewcompletedes);
		Button next=(Button)promptsView.findViewById(R.id.button_close);
		if(isSuccessful==0){
			next.setText("Try again");
		}
		else if(isSuccessful==1){
			next.setText("Book Ride");
		}
		else if(isSuccessful==2){
			next.setText("Ok");
		}
		
		titleview.setText(title);
		descview.setText(message);
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(splashActivity.this);
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
	
	
	
	private void turnGpsOn (Context context) {
	    try {
	    	Intent intent=new Intent("android.location.GPS_ENABLED_CHANGE");
	    	intent.putExtra("enabled", true);
	    	sendBroadcast(intent);
	    	start();
	    } catch(Exception e) {
	    	 System.out.println("in catch");
	    }
	    
	}


	private void turnGpsOff (Context context) {
	    
	    try {
	    	Intent intent = new Intent("android.location.GPS_ENABLED_CHANGE");
	    	intent.putExtra("enabled", false);
	    	sendBroadcast(intent);
	    } catch(Exception e) {}
	}
	}



