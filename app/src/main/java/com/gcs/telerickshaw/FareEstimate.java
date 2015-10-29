package com.gcs.telerickshaw;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.gcs.telerickshaw.MainActivity.TouchableWrapper;
import com.gcs.utils.DirectionsJSONParser;
import com.gcs.utils.GData;
import com.gcs.utils.ServiceHandler;
import com.gcs.utils.UserDatabaseHandler;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class FareEstimate extends SherlockFragmentActivity implements LocationListener,OnClickListener,GooglePlayServicesClient.ConnectionCallbacks,
GooglePlayServicesClient.OnConnectionFailedListener {
	
	Button button_ridenow;
	
	GoogleMap mGoogleMap;
	// A request to connect to Location Services
    private LocationRequest mLocationRequest;
    private LocationClient mLocationClient; 
	   // flag for GPS status
	   boolean isGPSEnabled = false;

	   // flag for network status
	   boolean isNetworkEnabled = false;

	   boolean canGetLocation = false;
	   
	   Location location; // location
//	   double latitude; // latitude
//	   double longitude; // longitude

	   public static String ShopLat;
	   public static String ShopPlaceId;
	   public static String ShopLong;

	   // Declaring a Location Manager
	   protected LocationManager locationManager;
	   public static LatLng originpoint,destinationpoint;
	   

	   // The minimum distance to change Updates in meters
	   private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters

	   // The minimum time between updates in milliseconds
	   private static final long MIN_TIME_BW_UPDATES = 1000 *60* 1; // 1 minute


	   // Stores the current instantiation of the location client in this object
	   //private LocationClient mLocationClient;
	   boolean mUpdatesRequested = false;
	   
	   TextView textviewDistanceval, farevalue, extracharges,time;
	   ProgressBar pbar;
	   
	// Milliseconds per second
	   public static final int MILLISECONDS_PER_SECOND = 1000;

	   // The update interval
	   public static final int UPDATE_INTERVAL_IN_SECONDS = 5;

	   // A fast interval ceiling
	   public static final int FAST_CEILING_IN_SECONDS = 1;

	   // Update interval in milliseconds
	   public static final long UPDATE_INTERVAL_IN_MILLISECONDS = MILLISECONDS_PER_SECOND
	           * UPDATE_INTERVAL_IN_SECONDS;

	   // A fast ceiling of update intervals, used when the app is visible
	   public static final long FAST_INTERVAL_CEILING_IN_MILLISECONDS = MILLISECONDS_PER_SECOND
	           * FAST_CEILING_IN_SECONDS;
	   // Stores the current instantiation of the location client in this object
	   //private LocationClient mLocationClient;

	   TouchableWrapper  mTouchView ;
	   FrameLayout googlemapframe;
	   private LatLng center;
	   private String distance,fare;
	 
	   

	 
	/////----------------------------GET RIDE DETAILS FROM SERVER-------------------------------------------
	   private class getRidedetails extends AsyncTask<Void, Void, Void> {
		   	 @Override
		        protected void onPreExecute() {
		            super.onPreExecute();
		        }
		 
		        @Override
		        protected Void doInBackground(Void... params) {
//		        	ServiceHandler sh=new ServiceHandler();
//		            String url=Urlconstant.url_booklater;
//		            url=url.replace(" ", "%20");
//					String response=sh.makeServiceCall(url,ServiceHandler.GET);
//		        	if(response !=null)
//					{
//						JSONObject jsonobj;
//						try
//						{
//							jsonobj =new JSONObject(response);
//								JSONArray jsonarr =jsonobj.getJSONArray("alldriver");
//								 System.out.println(jsonarr.length());
//								 for (int i = 0; i < jsonarr.length(); i++) {
//									 JSONObject arrobj= jsonarr.getJSONObject(i);
//									 String driverlat= arrobj.getString("driver_latitude");
//									 String driverlong= arrobj.getString("driver_logitude");
//									 String driverid= arrobj.getString("unique_id");
//								}
//							
//						}catch(JSONException e)
//						{
//							e.printStackTrace();
//						}
//					}
					return null;
		        }
		        
		        @Override
		        protected void onPostExecute(Void result) {
		       	 super.onPostExecute(null);
		      	 plotsourcedestinationMap();
		        }
		    }
	   

	   
		@Override
		public void  onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			 
			setContentView(R.layout.activity_fareestimate);
			overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
			
			 Typeface typeFace=Typeface.createFromAsset(this.getAssets(),"Rupee_Foradian.ttf");
			 Typeface typeFace2=Typeface.createFromAsset(getAssets(),"lato_regular.ttf");
			 pbar=(ProgressBar)findViewById(R.id.progressBar1);
			 pbar.setVisibility(View.VISIBLE);
			 
			 textviewDistanceval=(TextView)findViewById(R.id.textViewrideinkmvalue);
			 farevalue=(TextView)findViewById(R.id.extrarate1);
			 extracharges=(TextView)findViewById(R.id.extrarate2);
			 time=(TextView)findViewById(R.id.timevalue);
			 button_ridenow=(Button)findViewById(R.id.button_ridenow);
			 button_ridenow.setOnClickListener(this);
			 textviewDistanceval.setTypeface(typeFace);
			 farevalue.setTypeface(typeFace);
			 extracharges.setTypeface(typeFace);
			 time.setTypeface(typeFace);
			 button_ridenow.setTypeface(typeFace2);
			 
			 
			// Getting Google Play availability status
		        int status = GooglePlayServicesUtil
		                .isGooglePlayServicesAvailable(this.getBaseContext());

		        if (status != ConnectionResult.SUCCESS) { // Google Play Services are
		                                                    // not available

		            int requestCode = 10;
		            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, this,
		                    requestCode);
		            dialog.show();

		        } else { // Google Play Services are available
		            // Getting reference to the SupportMapFragment
		            // Create a new global location parameters object
		            mLocationRequest = LocationRequest.create();

		            /*
		             * Set the update interval
		             */
		            mLocationRequest.setInterval(GData.UPDATE_INTERVAL_IN_MILLISECONDS);

		            // Use high accuracy
		            mLocationRequest
		                    .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

		            // Set the interval ceiling to one minute
		            mLocationRequest
		                    .setFastestInterval(GData.FAST_INTERVAL_CEILING_IN_MILLISECONDS);

		            // Note that location updates are off until the user turns them on
		            mUpdatesRequested = false;

		            /*
		             * Create a new location client, using the enclosing class to handle
		             * callbacks.
		             */
		            mLocationClient = new LocationClient(this, this,this);
		            mLocationClient.connect();
		        }
	  }
		
		private void stupMap() {
	        try {
	            LatLng latLong;
	            // TODO Auto-generated method stub
	            SupportMapFragment supportMapFragment =
	  		          (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.googleMap);
	  		       mGoogleMap =supportMapFragment.getMap();
	            // Enabling MyLocation in Google Map
	               mGoogleMap.setMyLocationEnabled(true);
	            if (mLocationClient.getLastLocation() != null) {
	                latLong = new LatLng(mLocationClient.getLastLocation()
	                        .getLatitude(), mLocationClient.getLastLocation()
	                        .getLongitude());
	                ShopLat = mLocationClient.getLastLocation().getLatitude() + "";
	                ShopLong = mLocationClient.getLastLocation().getLongitude()
	                        + "";

	            } else {
	                latLong = new LatLng(12.9667, 77.5667);
	            }
	            CameraPosition cameraPosition = new CameraPosition.Builder()
	                    .target(latLong).zoom(15f).build();

	            mGoogleMap.setMyLocationEnabled(true);
//	            mGoogleMap.animateCamera(CameraUpdateFactory
//	                    .newCameraPosition(cameraPosition));
	            // Clears all the existing markers
	            mGoogleMap.clear();

	            
	            mGoogleMap.setOnCameraChangeListener(new OnCameraChangeListener() {

	                @Override
	                public void onCameraChange(CameraPosition arg0) {
	                	
	                    // TODO Auto-generated method stub
	                    center = mGoogleMap.getCameraPosition().target;
	                  

	                   
	                }
	            });

	           

	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        new getRidedetails().execute();
	    }
	
		

		//MarkerOptions options;
		@Override
		public void onLocationChanged(Location location) {
		}
		
		
		
	    //------Plot the source destination on map----------
	    public void plotsourcedestinationMap(){
	          String url = getDirectionsUrl(originpoint, destinationpoint);
	          DownloadTask downloadTask = new DownloadTask();
	          downloadTask.execute(url);
	          CameraPosition cameraPosition = new CameraPosition.Builder()
              .target(originpoint).zoom(14f).build();

		      mGoogleMap.animateCamera(CameraUpdateFactory
		              .newCameraPosition(cameraPosition));
	    }

	    
		 private String getDirectionsUrl(LatLng origin,LatLng dest){
			 
		        // Origin of route
		        String str_origin = "origin="+origin.latitude+","+origin.longitude;
		 
		        // Destination of route
		        String str_dest = "destination="+dest.latitude+","+dest.longitude;
		 
		        // Sensor enabled
		        String sensor = "sensor=false";
		 
		        // Building the parameters to the web service
		        String parameters = str_origin+"&"+str_dest+"&"+sensor;
		 
		        // Output format
		        String output = "json";
		 
		        // Building the url to the web service
		        String url = "https://maps.googleapis.com/maps/api/directions/"+output+"?"+parameters;
		 
		        return url;
		    }
		 


	@Override
	public void onClick(View v) {
		 if(v.equals(button_ridenow)){
			 askbookingdialog("Book Your Ride", "Do you want auto to pick you up?" 
						, 0);
		 }
		    
	}
	
	AlertDialog alertDialog1;
	private void askbookingdialog(String title,String message,final int isSuccessful) {
	   	LayoutInflater li = LayoutInflater.from(FareEstimate.this);
			View promptsView = li.inflate(R.layout.cutom_askforbook, null);
			TextView titleview=(TextView)promptsView.findViewById(R.id.textView1);
			TextView descview=(TextView)promptsView.findViewById(R.id.textViewcompletedes);
			Button cancelride=(Button)promptsView.findViewById(R.id.button_close);
			Button confirm=(Button)promptsView.findViewById(R.id.button_ok);
			
			titleview.setText(title);
			descview.setText(message);
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(FareEstimate.this);
			alertDialogBuilder.setView(promptsView);
			
			alertDialog1 = null;
			if(alertDialog1==null)
			alertDialog1 = alertDialogBuilder.create();
			if(alertDialog1.isShowing()) {
				alertDialog1.dismiss();
			}
			alertDialog1.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
			
			alertDialog1.show();
			
			
			cancelride.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
						alertDialog1.dismiss();
						
				}
			});
			
			confirm.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
						alertDialog1.dismiss();
						if(isSuccessful==0){
						  finish();
						}
				}
			});
		
		}
	
	

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}
	
	
	/** A method to download json data from url */
	private String downloadUrl(String strUrl) throws IOException{
	    String data = "";
	    InputStream iStream = null;
	    HttpURLConnection urlConnection = null;
	    try{
	        URL url = new URL(strUrl);

	        // Creating an http connection to communicate with url
	        urlConnection = (HttpURLConnection) url.openConnection();

	        // Connecting to url
	        urlConnection.connect();

	        // Reading data from url
	        iStream = urlConnection.getInputStream();

	        BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

	        StringBuffer sb = new StringBuffer();

	        String line = "";
	        while( ( line = br.readLine()) != null){
	            sb.append(line);
	        }

	        data = sb.toString();

	        br.close();

	    }catch(Exception e){
	        Log.d("Exception while downloading url", e.toString());
	    }finally{
	        iStream.close();
	        urlConnection.disconnect();
	    }
	    return data;
	}


			
		@Override
		public void onDestroy() {
		    super.onDestroy();
		   // getFragmentManager().beginTransaction().remove(this).commit();
		}
		
		
		
		
		// Fetches data from url passed
	    private class DownloadTask extends AsyncTask<String, Void, String>{
	 
	        // Downloading data in non-ui thread
	        @Override
	        protected String doInBackground(String... url) {
	 
	            // For storing data from web service
	            String data = "";
	 
	            try{
	                // Fetching the data from web service
	                data = downloadUrl(url[0]);
	            }catch(Exception e){
	                Log.d("Background Task",e.toString());
	            }
	            return data;
	        }
	 
	        // Executes in UI thread, after the execution of
	        // doInBackground()
	        @Override
	        protected void onPostExecute(String result) {
	            super.onPostExecute(result);
	 
	            DistanceParserTask parserTask = new DistanceParserTask();
	 
	            // Invokes the thread for parsing the JSON data
	            parserTask.execute(result);
	        }
	    }
	    
	    
	    /** A class to parse the Google Places in JSON format */
	    private class DistanceParserTask extends AsyncTask<String, Integer, List<List<HashMap<String,String>>> >{
	 
	        // Parsing the data in non-ui thread
	        @Override
	        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {
	 
	            JSONObject jObject;
	            List<List<HashMap<String, String>>> routes = null;
	 
	            try{
	                jObject = new JSONObject(jsonData[0]);
	                DirectionsJSONParser parser = new DirectionsJSONParser();
	 
	                // Starts parsing data
	                routes = parser.parse(jObject);
	            }catch(Exception e){
	                e.printStackTrace();
	            }
	            return routes;
	        }
	 
	        // Executes in UI thread, after the parsing process
	        @Override
	        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
	            ArrayList<LatLng> points = null;
	            PolylineOptions lineOptions = null;
	            MarkerOptions markerOptions = new MarkerOptions();
	            String distance = "";
	            String duration = "";
	 
	            if(result.size()<1){
	                Toast.makeText(FareEstimate.this, "No Points", Toast.LENGTH_SHORT).show();
	                return;
	            }
	 
	            // Traversing through all the routes
	            for(int i=0;i<result.size();i++){
	                points = new ArrayList<LatLng>();
	                lineOptions = new PolylineOptions();
	 
	                // Fetching i-th route
	                List<HashMap<String, String>> path = result.get(i);
	 
	                // Fetching all the points in i-th route
	                for(int j=0;j<path.size();j++){
	                    HashMap<String,String> point = path.get(j);
	 
	                    if(j==0){    // Get distance from the list
	                        distance = (String)point.get("distance");
	                        continue;
	                    }else if(j==1){ // Get duration from the list
	                        duration = (String)point.get("duration");
	                        continue;
	                    }
	 
	                    double lat = Double.parseDouble(point.get("lat"));
	                    double lng = Double.parseDouble(point.get("lng"));
	                    LatLng position = new LatLng(lat, lng);
	 
	                    points.add(position);
	                }
	 
	                // Adding all the points in the route to LineOptions
	                lineOptions.addAll(points);
	                lineOptions.width(2);
	                lineOptions.color(Color.RED);
	            }
	            System.out.println("Distance:"+distance + ", Duration:"+duration);
	            textviewDistanceval.setText(distance);
	            time.setText(duration);
	            String d[]=distance.split("km");
	            System.out.println("disssss="+d[0]);
	         //
	            float fare=fare(d[0]);
	            farevalue.setText(fare+"");
	          
	            if(pbar!=null){
	            	pbar.setVisibility(View.INVISIBLE);
	            }
	            // Drawing polyline in the Google Map for the i-th route
	            // Drawing polyline in the Google Map for the i-th route
	            mGoogleMap.addPolyline(lineOptions);
	        }
	    }
	    
	    
	    
		@Override
		public void onConnectionFailed(ConnectionResult arg0) {
			// TODO Auto-generated method stub
			
		}

		 @Override
		    public void onConnected(Bundle arg0) {
		        // TODO Auto-generated method stub
			 System.out.println("fsf");
			 stupMap();

		    }
		@Override
		public void onDisconnected() {
			// TODO Auto-generated method stub
			
		}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);
	}
	
	
	  public float fare(String dis){
		  try{
		   float Beforetwokm=15;
		   float Aftertwokm=12;
		   float distancecovered=Float.parseFloat(dis);
		   float calculated_fare;
		   float peakhour=1.3f;
		   if(checkforPeakhours()){
			   peakhour=1.3f;
			   extracharges.setText(peakhour+"");
		   }else{
			   peakhour=1f;
			   extracharges.setText("0");
		   }
		   if(distancecovered<2){
			   calculated_fare= Beforetwokm*distancecovered*peakhour;
			   calculated_fare=40;
		   }
		   else{
			   calculated_fare= Aftertwokm*distancecovered*peakhour;
		   }
		   
		   
		   return calculated_fare;
		  }
		  catch(Exception e){
			  Toast.makeText(getApplicationContext(), "Unable to calculate fare", Toast.LENGTH_SHORT).show();
			  return 0;
		  }
	   }
	  
	  //check for peak hours
	   int hour,min;
	   String AM_PM;
	   private boolean checkforPeakhours() {
		 boolean isinpeak=false;
		 Calendar c = Calendar.getInstance();
	     hour = c.get(Calendar.HOUR_OF_DAY);
	     min = c.get(Calendar.MINUTE);
	     int ds = c.get(Calendar.AM_PM);
	     if(ds==0)
	     AM_PM="am";
	     else
	     AM_PM="pm";

	  //   Toast.makeText(getActivity(), ""+hour+":"+min+AM_PM, Toast.LENGTH_SHORT).show();
	     if((hour==22&&AM_PM.matches("pm")) || (hour<7&&AM_PM.matches("am")))
	     {
	    	 isinpeak=true;
	         //Toast.makeText(getActivity(), "Time is between the range", Toast.LENGTH_SHORT).show();
	     }
	     else{
	    	 isinpeak=false;
	        // Toast.makeText(getActivity(), "Time is not between the range", Toast.LENGTH_SHORT).show();
	    	 
	     }

		return isinpeak;
	   }
	   
	   
	   
	

}
