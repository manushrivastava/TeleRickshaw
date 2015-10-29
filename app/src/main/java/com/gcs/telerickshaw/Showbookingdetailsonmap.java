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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.gcs.utils.DirectionsJSONParser;
import com.gcs.utils.GData;
import com.gcs.utils.ServiceHandler;
import com.gcs.utils.Urlconstant;
import com.gcs.utils.User;
import com.gcs.utils.UserDatabaseHandler;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class Showbookingdetailsonmap extends SherlockFragmentActivity  implements LocationListener,OnClickListener,GooglePlayServicesClient.ConnectionCallbacks,
GooglePlayServicesClient.OnConnectionFailedListener {
	
	// A request to connect to Location Services
    private LocationRequest mLocationRequest;
    private LocationClient mLocationClient;
    
	GoogleMap mGoogleMap;
	Button button_startjourney;
    ProgressBar pbar;
	AlertDialog alertDialog2 = null;
	AlertDialog alertDialog3 = null;
	public static ArrayList<User> userlist=new ArrayList<User>();
	
	 public static String ShopLat;
	 public static String ShopPlaceId;
	 public static String ShopLong;
	    
   int BOOKNOW=0;
   int BOOKLATER=1;
   
   int JOURENYSTARTED_FLAG=0;
   
   int booknoworlater=0;
 

   //View v = null;
   //ViewGroup previousContainer;
   
   // flag for GPS status
   boolean isGPSEnabled = false;

   // flag for network status
   boolean isNetworkEnabled = false;

   boolean canGetLocation = false;
   
   Location location; // location
   double latitude; // latitude
   double longitude; // longitude

   // The minimum distance to change Updates in meters
   private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters

   // The minimum time between updates in milliseconds
   private static final long MIN_TIME_BW_UPDATES = 1000 * 6 * 1; // 1 minute

   // Declaring a Location Manager
   protected LocationManager locationManager;
   LatLng originpoint,destinationpoint;
   private LatLng center;
   
   
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
   boolean mUpdatesRequested = false;
   
   
   boolean Peak_on=false;
   double Totalfare=0;
   double journeyStartlat,journeystartlong,journeyendlat,journeyendlong;
   
//   public Location getLocation() {
//       try {
//           locationManager = (LocationManager) getActivity()
//                   .getSystemService(getActivity().LOCATION_SERVICE);
//
//           // getting GPS status
//           isGPSEnabled = locationManager
//                   .isProviderEnabled(LocationManager.GPS_PROVIDER);
//          
//           
//           System.out.println("GPS on="+isGPSEnabled);
//           if (!isGPSEnabled && !isNetworkEnabled) {
//               // no network provider is enabled
//           } else {
//               this.canGetLocation = true;
//  
//               // if GPS Enabled get lat/long using GPS Services
//               if (isGPSEnabled) {
//                   if (location == null) {
//                       locationManager.requestLocationUpdates(
//                               LocationManager.GPS_PROVIDER,
//                               MIN_TIME_BW_UPDATES,
//                               MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
//                       Log.d("GPS Enabled", "GPS Enabled");
//                       if (locationManager != null) {
//                           location = locationManager
//                                   .getLastKnownLocation(LocationManager.GPS_PROVIDER);
//                           if (location != null) {
//                               latitude = location.getLatitude();
//                               longitude = location.getLongitude();
//                               System.out.println("lat="+latitude+"long"+longitude);
//                           }
//                       }
//                   }
//               }
//           }
//
//       } catch (Exception e) {
//           e.printStackTrace();
//           System.out.println("EXCEPTION");
//       }
//
////       try{
////    	   ServiceHandler sh=new ServiceHandler();
////           if(sh.isOnline(getActivity()))
////           	new getRidedetails().execute();
////           }
////           catch(Exception e){
////           	e.printStackTrace();
////        }
//           
//       if (location != null) {
//           onLocationChanged(location);
//        }
//       return location;
//   }
//   
   
   
/////----------------------------GET RIDE DETAILS FROM SERVER-------------------------------------------
   private class getRidedetails extends AsyncTask<String, Void, String> {
		String status,drivername,driverno,driverlat,driverlong,driverrickshawnum;
	     
        @Override
        protected void onPreExecute() {
        }

        @Override
        protected String doInBackground(String... params) {
        	ServiceHandler sh=new ServiceHandler();
            String url=Urlconstant.url_getstatusofbooking+MainActivity.bookin_ID+"&type=userbookingstauts";
            url=url.replace(" ", "%20");
            
			String response=sh.makeServiceCall(url,getApplicationContext(),ServiceHandler.GET);
			
        	if(response !=null)
			{
				JSONObject jsonobj;
				try
				{
					jsonobj =new JSONObject(response);
					//String status=jsonobj.getString("signup");
						JSONArray jsonarr =jsonobj.getJSONArray("bookingstauts");
						
						 for (int i = 0; i < jsonarr.length(); i++) {
							 JSONObject arrobj= jsonarr.getJSONObject(i);
							  status= arrobj.getString("status");
							 
							 if(status.compareTo("Accepted")==0){
								  drivername= arrobj.getString("d_name");
								  driverno= arrobj.getString("d_mobileno");
								  driverlat= arrobj.getString("d_latitude");
								  driverlong= arrobj.getString("d_logitude");
								  driverrickshawnum= arrobj.getString("d_rickhano");
								 
								 System.out.println("Dname="+drivername);
								 System.out.println("Dno="+driverno);
								 System.out.println("Dauto="+driverrickshawnum);
								 System.out.println("Dlat="+driverlat);
								 System.out.println("Dlong="+driverlong);
							 }
							 else{
								 return null;
							 }
						}
							
					
				}catch(JSONException e)
				{
					e.printStackTrace();
				}
			}
			return null;

        }

        @Override
        protected void onPostExecute(String result) {
        	plotsourcedestinationMap(driverlat,driverlong);
           
        }

      
   }
   
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		 UserDatabaseHandler udata=new UserDatabaseHandler(this.getApplicationContext());
		 userlist=udata.getAllBrand();
		 setContentView(R.layout.rideon);
		 Typeface typeFace=Typeface.createFromAsset(getAssets(),"Rupee_Foradian.ttf");
		// button_fare=(Button)v.findViewById(R.id.button_getfare);
		 pbar=(ProgressBar)findViewById(R.id.progressBar1);
		 
		 
			// Getting Google Play availability status
	        int status = GooglePlayServicesUtil
	                .isGooglePlayServicesAvailable(getBaseContext());

	        if (status != ConnectionResult.SUCCESS) { // Google Play Services are
	                                                    // not available

	            int requestCode = 10;
	            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, this,
	                    requestCode);
	            dialog.show();

	        } else { // Google Play Services are available
//	        	mTouchView = new TouchableWrapper(getActivity());
//	        	
//	        	 mTouchView.addView(v);
	        
	   		    
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
	
	boolean polylinesdrawn=false;;
	
	private void stupMap() {
        try {
            LatLng latLong;

            SupportMapFragment supportMapFragment =
  		          (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.googleMap);
  		        mGoogleMap =supportMapFragment.getMap();

  		        mGoogleMap.setMyLocationEnabled(true);
  		        
  		        
           
            if (mLocationClient.getLastLocation() != null) {
                latLong = new LatLng(mLocationClient.getLastLocation()
                        .getLatitude(), mLocationClient.getLastLocation()
                        .getLongitude());
//                ShopLat = mLocationClient.getLastLocation().getLatitude() + "";
//                ShopLong = mLocationClient.getLastLocation().getLongitude()
//                        + "";
        //        System.out.println(ShopLong+"lastlocation"+ShopLat);
               // Location loc=getLocation();
                //center=new LatLng(latitude, longitude);

            } else {
                latLong = new LatLng(12.9667, 77.5667);
            }
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(latLong).zoom(15f).build();

            mGoogleMap.setMyLocationEnabled(true);
            mGoogleMap.animateCamera(CameraUpdateFactory
                    .newCameraPosition(cameraPosition));
            // Clears all the existing markers
           //  mGoogleMap.clear();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

	
//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        MapFragment f = (MapFragment) getActivity().getFragmentManager()
//        .findFragmentById(R.id.googleMap);
//			if (f != null) 
//			getActivity().getFragmentManager().beginTransaction().remove(f).commit();
//    }
	
	
	@Override
	public void onLocationChanged(Location location) {
		System.out.println("UPDATING BY onLocationChanged");
		try{
	     latitude=location.getLatitude();
	     longitude=location.getLongitude();
		 center=new LatLng(latitude, longitude);
		 }
		catch(Exception e){
			
		}
		 if(!polylinesdrawn){
             try{
            	   ServiceHandler sh=new ServiceHandler();
                   if(sh.isOnline(getApplicationContext()))
                   	new getRidedetails().execute();
                   }
                   catch(Exception e){
                   	e.printStackTrace();
                }
          }
             
	}
	
	
	
    //------Plot the source destination on map----------
    public void plotsourcedestinationMap(String driverlat, String driverlong){
    	if(center==null){
    		stupMap();
    	}else{
    	  double sourcelatitude=center.latitude;
    	  double sourcelongitude=center.longitude;
    	  double destinationlatitude=Double.parseDouble(driverlat);
    	  double destinationlongitude=Double.parseDouble(driverlong);
    	  
    	  originpoint=new LatLng(sourcelatitude, sourcelongitude);
	      destinationpoint=new LatLng(destinationlatitude, destinationlongitude);
	      
	        MarkerOptions markerOptions=new MarkerOptions();
	        markerOptions.icon(BitmapDescriptorFactory
                    .fromResource(R.drawable.autosmall));
	        
	      
//	        mGoogleMap.clear();
//	        if(originpoint!=null){
	        	mGoogleMap.addMarker(markerOptions.position(destinationpoint));
	//        	mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(destinationpoint));
	 //       	mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(15));
//	        }
	     
          String url = getDirectionsUrl(originpoint, destinationpoint);
          DownloadTask downloadTask = new DownloadTask();
          downloadTask.execute(url);
    	}
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
            markerOptions.icon(BitmapDescriptorFactory
                    .fromResource(R.drawable.autosmall));
            String distance = "";
            String duration = "";
            if(result!=null){
            if(result.size()<1){
                Toast.makeText(Showbookingdetailsonmap.this, "No Points", Toast.LENGTH_SHORT).show();
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
           // tvDistanceDuration.setText("Distance:"+distance + ", Duration:"+duration);
 
            // Drawing polyline in the Google Map for the i-th route
            mGoogleMap.addPolyline(lineOptions);
            polylinesdrawn=true;
            }
            else{
            	System.out.println("null");
            }
        }
    }
    
    
    private final Handler handler = new Handler();
	 // The minimum time between updates in milliseconds
   private static final long DISTANCE_CALCULATION_UPDATES = 1000 *6* 1; // 1 minute
   
   private void startcalculatingfare() {
	   
	   plat=center.latitude;
       plon=center.longitude;
       
       journeyStartlat=center.latitude;
       journeystartlong=center.longitude;
       
       if(!Peak_on)
        Peak_on=checkforPeakhours();
       
       handler.removeCallbacks(locationDistanceupdate);
       handler.postDelayed(locationDistanceupdate, DISTANCE_CALCULATION_UPDATES); // 1 second
       counter= new MyCount(30000,1000);
       counter.start();
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

     Toast.makeText(Showbookingdetailsonmap.this, ""+hour+":"+min+AM_PM, Toast.LENGTH_SHORT).show();
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
   
   

//-----------------------------------------GET AND UPDATE LOCATION ON SERVER----------
   private Runnable locationDistanceupdate = new Runnable() {
       public void run() {
               handler.postDelayed(this, DISTANCE_CALCULATION_UPDATES); // 6 seconds here you can give your time
               clat=center.latitude;
               clon=center.longitude;
          //     System.out.println("cla="+clat+","+"clong="+clon);
            //   System.out.println("plat="+plat+","+"plong="+plon);
               
               if(clat!=plat || clon!=plon){
                   dis+=getDistance(plat,plon,clat,clon);
                   
                   System.out.println("Changes locaaa"+dis);
                   plat=clat;
                   plon=clon;
                   fare();
               }
              // Toast.makeText(getApplicationContext(), "Hi location update"+latitude+","+longitude, Toast.LENGTH_SHORT).show();        
               
       }
   };   
   
   
  
   
   public double fare(){
	   float Beforetwokm=15;
	   float Aftertwokm=12;
	   float distancecovered=(float)dis;
	   double calculated_fare;
	   float peakhour=1.3f;
	   
	   if(Peak_on){
		   peakhour=1.3f;
	   }else{
		   peakhour=1f;
	   }
	   
	   if(distancecovered<2){
		  
		   calculated_fare= Beforetwokm*distancecovered*peakhour;
		   
	   }
	   else{
		   calculated_fare= Aftertwokm*distancecovered*peakhour;
	   }
	   
	   Totalfare=calculated_fare;
	   return Totalfare;
	   
   }
   
   
   
   

   
    static double n=0;
    Long s1,r1;
    double plat,plon,clat,clon,dis;
    MyCount counter;
    Thread t1;
    boolean bool=true;
    public class MyCount extends CountDownTimer{
        public MyCount(long millisInFuture, long countDownInterval) {
        super(millisInFuture, countDownInterval);
        }
        @Override
        public void onFinish() {
            counter= new MyCount(30000,1000);
	         counter.start();
	         n=n+1;
        }
        @Override
        public void onTick(long millisUntilFinished) {
            s1=millisUntilFinished;
            r1=(30000-s1)/1000;
            System.out.println("updated");
//            textview_distanceval.setText(String.format("%.2f", dis));
//            textview_fareval.setText(String.format("%.2f", Totalfare));
//            

        }
        }
    
    
   
    
    public double getDistance(double lat1, double lon1, double lat2, double lon2) {
//        double latA = Math.toRadians(lat1);
//        double lonA = Math.toRadians(lon1);
//        double latB = Math.toRadians(lat2);
//        double lonB = Math.toRadians(lon2);
//        double cosAng = (Math.cos(latA) * Math.cos(latB) * Math.cos(lonB-lonA)) +
//                        (Math.sin(latA) * Math.sin(latB));
//        double ang = Math.acos(cosAng);
//        double dist = ang *6371;
//        
//        return dist/1000;
        
    	Location location1 = new Location("");
        location1.setLatitude(lat1);
        location1.setLongitude(lon1);

        Location location2 = new Location("");
        location2.setLatitude(lat2);
        location2.setLongitude(lon2);

        float distanceInMeters = location1.distanceTo(location2)/1000;
        return distanceInMeters;
    }
    
   
	@Override
	public void onConnectionFailed(ConnectionResult arg0) {
		// TODO Auto-generated method stub
		
	}

	 @Override
	    public void onConnected(Bundle arg0) {
	        // TODO Auto-generated method stub
	        mLocationClient.requestLocationUpdates(mLocationRequest, this);
	        newhandler.removeCallbacks(updatemap);
	        newhandler.postDelayed(updatemap, MIN_TIME_FOR_CHECK); // 1 second		
			
	        stupMap();

	    }
	 
		private final Handler newhandler = new Handler();
		int MAXTIME=0;
		 // The minimum time between updates in milliseconds
	    private static final long MIN_TIME_FOR_CHECK = 1000 *20* 1; // 1/2 minute
	    
	    private Runnable updatemap = new Runnable() {
	        public void run() {
	        	System.out.println("map update");
	        	polylinesdrawn=false;
	   		    mGoogleMap.clear();  
	   		 newhandler.postDelayed(updatemap, MIN_TIME_FOR_CHECK); // 1 second		
				    
	        }
	    };    
	@Override
	public void onDisconnected() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}

}
