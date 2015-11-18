package com.gcs.telerickshaw;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnDragListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.actionbarsherlock.app.SherlockFragment;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.gcs.telerickshawadapter.Rides;
import com.gcs.utils.BookingDatabaseHandler;
import com.gcs.utils.DirectionsJSONParser;
import com.gcs.utils.GData;
import com.gcs.utils.PlaceJSONParser;
import com.gcs.utils.ServiceHandler;
import com.gcs.utils.Urlconstant;
import com.gcs.utils.User;
import com.gcs.utils.UserDatabaseHandler;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.common.api.GoogleApiClient;

public class MainActivity extends SherlockFragment implements LocationListener,OnClickListener ,OnDragListener, GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener
{
	
	GoogleMap mGoogleMap;
	// A request to connect to Location Services
    private LocationRequest mLocationRequest;
    private GoogleApiClient client=null;

//    private LocationClient mLocationClient;
    String timetopick="";
    boolean suggestionselected=false;

    TextView estimatedtimetopic;
	Button button_fare,button_ridenow,button_ridelater;
	//AutoCompleteTextView //atvPlacesfrom,
	AutoCompleteTextView atvPlacesto;
    PlacesTask placesTask;
	ParserTask parserTask;
	String from,to,date,time;
    ProgressBar pbar;
	AlertDialog alertDialog2 = null;
	AlertDialog alertDialog3 = null;
	public static ArrayList<User> userlist=new ArrayList<User>();
	
	//private TextView markerText;
    private LatLng center;
    private LatLng myoriginallocation;
    private LinearLayout markerLayout;
	    
   int BOOKNOW=0;
   int BOOKLATER=1;
   int booknoworlater=0;
 
   int UPDATE_TIME=20000;
   boolean handlerStarted=false;

   View v = null;
   ViewGroup previousContainer;
   
   // flag for GPS status
   boolean isGPSEnabled = false;

   // flag for network status
   boolean isNetworkEnabled = false;

   boolean canGetLocation = false;

   


   // Declaring a Location Manager
   protected LocationManager locationManager;
   
   TextView textview_time;
   private Geocoder geocoder;
   private List<Address> addresses;
   private TextView Address;
   
   
   
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

   TouchableWrapper  mTouchView ;
   FrameLayout googlemapframe;
   
  
   Typeface typeFacelatolight;
   
	ArrayList<RickshawDetails> rikshawlist=new ArrayList<RickshawDetails>();
 
   
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {


		if (mTouchView != null && this.previousContainer == container) {
	        ViewGroup parent = (ViewGroup) mTouchView.getParent();
	        if (parent != null) {
	            parent.removeView(mTouchView);
	        }
	    } else {
		 
		 UserDatabaseHandler udata=new UserDatabaseHandler(getActivity().getApplicationContext());
		 userlist=udata.getAllBrand();
		 v = inflater.inflate(R.layout.home, container, false);
		 

		 Typeface typeFace=Typeface.createFromAsset(getActivity().getAssets(),"lato_regular.ttf");
		 typeFacelatolight=Typeface.createFromAsset(getActivity().getAssets(),"lato_light.ttf");
		// button_fare=(Button)v.findViewById(R.id.button_getfare);
		 button_ridelater=(Button)v.findViewById(R.id.button_ridelater);
		 pbar=(ProgressBar)v.findViewById(R.id.progressBar1);
		 button_ridenow=(Button)v.findViewById(R.id.button_ridenow);
		 textview_time=(TextView)v.findViewById(R.id.textViewtime);
		
		 textview_time.setTypeface(typeFacelatolight);
		 button_ridelater.setTypeface(typeFace);
		 button_ridenow.setTypeface(typeFace);
		// markerText = (TextView) v.findViewById(R.id.locationMarkertext);
		 Address = (TextView) v.findViewById(R.id.adressText);
		 Address.setOnClickListener(this);
			
		 Address.setTypeface(typeFacelatolight);
		 Address.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if(event.getAction()==MotionEvent.ACTION_DOWN){
				Intent intent=new Intent(getActivity(),SearchaddressActivity.class);
				startActivityForResult( intent, 100 );
				}
				return false;
			}
		});
	     markerLayout = (LinearLayout) v.findViewById(R.id.locationMarker);
	     googlemapframe = (FrameLayout) v.findViewById(R.id.googleMapfragment);
	   //  markerText.setTypeface(typeFace);
		 button_ridenow.setOnClickListener(this);
		 button_ridelater.setOnClickListener(this);
			 
		 this.previousContainer = container;
		
	
	        int status = GooglePlayServicesUtil
	                .isGooglePlayServicesAvailable(getActivity().getBaseContext());

	        if (status != ConnectionResult.SUCCESS) { // Google Play Services are
	                                                    // not available

	            int requestCode = 10;
	            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, getActivity(),
	                    requestCode);
	            dialog.show();

	        } else { // Google Play Services are available
	        	mTouchView = new TouchableWrapper(getActivity());
        	
	        	 mTouchView.addView(v);
	      
	            // Getting reference to the SupportMapFragment
	            // Create a new global location parameters object
	            mLocationRequest = LocationRequest.create();
                client = new GoogleApiClient.Builder(new Activity()).addApi(LocationServices.API).addConnectionCallbacks(this).addOnConnectionFailedListener(this).build();
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
	            client.connect();
	        }
	    }
		return mTouchView;
	}
	
	@Override
	public View getView() {
	    return v;
	}
	
	private void stupMap() {
        try {
            LatLng latLong;
            // TODO Auto-generated method stub
            SupportMapFragment supportMapFragment =
  		          (SupportMapFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.googleMap);
  		       mGoogleMap =supportMapFragment.getMap();
            // Enabling MyLocation in Google Map
               mGoogleMap.setMyLocationEnabled(true);
            if (LocationServices.FusedLocationApi.getLastLocation(client)!= null) {
                latLong = new LatLng(LocationServices.FusedLocationApi.getLastLocation(client)
                        .getLatitude(), LocationServices.FusedLocationApi.getLastLocation(client)
                        .getLongitude());

            } else {
                latLong = new LatLng(22.719569, 77.5667);
            }
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(latLong).zoom(15f).build();

            mGoogleMap.setMyLocationEnabled(true);
            mGoogleMap.animateCamera(CameraUpdateFactory
                    .newCameraPosition(cameraPosition));
            // Clears all the existing markers
            mGoogleMap.clear();

            
            mGoogleMap.setOnCameraChangeListener(new OnCameraChangeListener() {

                @Override
                public void onCameraChange(CameraPosition arg0) {
                	
                    // TODO Auto-generated method stub
                    center = mGoogleMap.getCameraPosition().target;
                     //system.out.println("arg0"+arg0);
                   // markerText.setText(" Set your Location ");
                    //mGoogleMap.clear();
                    markerLayout.setVisibility(View.VISIBLE);
                    try {
                    	 if (!mMapIsDragging) {
                    		// mGoogleMap.clear();
                    		 //TO UPDATE RICKSHAW LOCATIONS
                    		 if(!handlerStarted)
                             UI_HANDLER.postDelayed(UI_UPDTAE_RUNNABLE, UPDATE_TIME);
                    		 System.out.println("DRAGEING");
                    		 Thread.sleep(50);
                    		 new GetLocationAsync(center.latitude, center.longitude)
                             .execute();
                    		 
                    		
                         }
                       
                       
                    } catch (Exception e) {
                    }
                }
            });

            markerLayout.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub

                    try {

                        LatLng latLng1 = new LatLng(center.latitude,
                                center.longitude);
                       
                        Marker m = mGoogleMap.addMarker(new MarkerOptions()
                                .position(latLng1)
                                .title(" Set your Location ")
                                .snippet("")
                                .icon(BitmapDescriptorFactory
                                        .fromResource(R.drawable.mappointer)));
                        m.setDraggable(true);

                        markerLayout.setVisibility(View.GONE);
                    } catch (Exception e) {
                    }

                }
            });

         
//            try{
//          	   ServiceHandler sh=new ServiceHandler();
//                 if(sh.isOnline(getActivity())){
//                 	new getRikshaws().execute();
//                 }
//                 }
//                 catch(Exception e){
//                 	e.printStackTrace();
//              }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


	 Handler UI_HANDLER = new Handler();
	   
	 Runnable UI_UPDTAE_RUNNABLE = new Runnable() {

		    @Override
		    public void run() {
		    	try{
		    		Thread.sleep(50);
		    		System.out.println("inside update");
		          	   ServiceHandler sh=new ServiceHandler();
		                 if(sh.isOnline(getActivity())){
		                 	new getRikshaws().execute();
		                 }
		                 }
		                 catch(Exception e){
		                 	e.printStackTrace();
		              }
		    	handlerStarted=true;
		        UI_HANDLER.postDelayed(UI_UPDTAE_RUNNABLE, UPDATE_TIME);
		    }
		};
   
  
    @Override
    public void onConnectionFailed(ConnectionResult arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onConnected(Bundle a) {
        // TODO Auto-generated method stub
        LocationServices.FusedLocationApi.requestLocationUpdates(client, mLocationRequest, this);


        stupMap();

    }
    @Override
    public void onConnectionSuspended(int i) {
    }




    private class GetLocationAsync extends AsyncTask<String, Void, String> {

        // boolean duplicateResponse;
        double x, y;
        StringBuilder str;

        public GetLocationAsync(double latitude, double longitude) {
            // TODO Auto-generated constructor stub

            x = latitude;
            y = longitude;
        }

        @Override
        protected void onPreExecute() {
            Address.setText("finding location ");
        }

        @Override
        protected String doInBackground(String... params) {

            try {
                geocoder = new Geocoder(getActivity(), Locale.ENGLISH);
                addresses = geocoder.getFromLocation(x, y, 1);
                str = new StringBuilder();
                if (geocoder.isPresent()&&!addresses.isEmpty()) {
                    Address returnAddress = addresses.get(0);

                    String localityString = returnAddress.getLocality();
                    String city = returnAddress.getCountryName();
                    String region_code = returnAddress.getCountryCode();
                    String zipcode = returnAddress.getPostalCode();

                    str.append(localityString + "");
                    str.append(city + "" + region_code + "");
                    str.append(zipcode + "");

                } else {
                }
            } catch (IOException e) {
                Log.e("tag", e.getMessage());
            }
            return null;

        }

        @Override
        protected void onPostExecute(String result) {
            try {
            	if(!addresses.isEmpty())
                Address.setText(addresses.get(0).getAddressLine(0)
                        + addresses.get(0).getAddressLine(1) + " ");
            	//plotLocationsonMap(MainActivity.this.rikshawlist);
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getActivity(), "Network Problem", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onProgressUpdate(Void... values) {

        }
    }

    

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        MapFragment f = (MapFragment) getActivity().getFragmentManager()
        .findFragmentById(R.id.googleMap);
			if (f != null) 
			getActivity().getFragmentManager().beginTransaction().remove(f).commit();
    }
	
    int ingetrickshaw=0;
    int inplotmap=0;
	

	
/////----------------------------UPDATE LOCATION IN BACKGROUND ON SERVER-------------------------------------------
    private class getRikshaws extends AsyncTask<Void, Void, Void> {
    	 int issucessful=0;
	   	 @Override
	        protected void onPreExecute() {
	            super.onPreExecute();
	            System.out.println("");
	            ingetrickshaw=ingetrickshaw+1;
	         //   pbar.setVisibility(ProgressBar.VISIBLE);
	         //   textview_time.setText("Searching AutoRickshaws..");
	            rikshawlist.clear();
	        }
	 
	        @Override
	        protected Void doInBackground(Void... params) {
	        	ServiceHandler sh=new ServiceHandler();
	            String url=Urlconstant.url_getrickshaws;
	            url=url.replace(" ", "%20");
	            
				String response=sh.makeServiceCall(url,getActivity(),ServiceHandler.GET);
				issucessful=1;
				
				//System.out.println("rEs="+response);
	        	if(response !=null)
				{
	        		issucessful=1;
					JSONObject jsonobj;
					try
					{
						  rikshawlist.clear();
						jsonobj =new JSONObject(response);
						//String status=jsonobj.getString("signup");
							JSONArray jsonarr =jsonobj.getJSONArray("alldriver");
							 System.out.println("Getting all rickshaws="+jsonarr.length());
							 for (int i = 0; i < jsonarr.length(); i++) {
								 JSONObject arrobj= jsonarr.getJSONObject(i);
								 String driverlat= arrobj.getString("driver_latitude");
								 String driverlong= arrobj.getString("driver_logitude");
								 String driverid= arrobj.getString("unique_id");
								 
								 RickshawDetails rd=new RickshawDetails(driverlat, driverlong, driverid);
								 rikshawlist.add(rd);
							}
								
						
					}catch(JSONException e)
					{
						e.printStackTrace();
					}
				}else{
					issucessful=0;
				}
				return null;
	        }
	        
	        
	        @Override
	        protected void onPostExecute(Void result) {
	       	 super.onPostExecute(null);
	       	 if(!rikshawlist.isEmpty()&&issucessful==1){
	       		 System.out.println("No of rickshaws="+rikshawlist.size());
	         	 plotLocationsonMap();
	       	 }
	       	 else{
	       		textview_time.setText("Auto rickshaw not available nearby you");
	       	 }
	        }
	    }
    
    
    //------Plot the location of all rickshaws on map----------
    public void plotLocationsonMap(){
    	int countofcloserickshaws = 0;
    	mGoogleMap.clear();
    	Double distance[][]=new Double[rikshawlist.size()][3];
    	inplotmap=inplotmap+1;
    	System.out.println("rickshaw valo="+rikshawlist.size());
    	System.out.println("ingetrricskahw="+ingetrickshaw);
    	System.out.println("inplot="+inplotmap);
      for (int i = 0; i < rikshawlist.size(); i++) {
    	  try{
    	  double rlatitude=Double.parseDouble(rikshawlist.get(i).getLatitude());
    	  double rlongitude=Double.parseDouble(rikshawlist.get(i).getLongitude());
    	  //double rlatitude=Double.parseDouble("22.802507453598082");
    	  //double rlongitude=Double.parseDouble("75.85749423131347");
    	 // //system.out.println(i+"Auto"+rlatitude);
    	//  //system.out.println(i+"Auto"+rlongitude);
    	  
    	drawMarker( new LatLng(rlatitude,rlongitude));
    	Location locationA = new Location("A");
  	    locationA.setLatitude(center.latitude);
  	    locationA.setLongitude(center.longitude);
  	    Location locationB = new Location("B");
  	    locationB.setLatitude(rlatitude);
  	    locationB.setLongitude(rlongitude);
  	    
  	    distance[i][0]= (double) locationA.distanceTo(locationB);
  	    distance[i][1]= rlatitude;
  	    distance[i][2]= rlongitude;
  	    
  	    if(distance[i][0]<3000){
  	    	countofcloserickshaws=countofcloserickshaws+1;
  	    }
    	  }
    	  catch(Exception e){
    		  System.out.println("Exception in array");
    	  }
    
      }
      if(countofcloserickshaws>0){
        //textview_time.setText(countofcloserickshaws+"autorickshaw found in your area");
      }
      //find closest auto
        if(distance.length>0){
        	try{
	    	double small = distance[0][0];
	    	int index = 0;
	    	for (int i = 0; i < distance.length; i++)
	    	    if (distance[i][0] < small)
	    	    {
	    	        small = distance[i][0];
	    	        index = i;
	    	    }
	    	
	    	if(small<=10000){
	    		//system.out.println(distance[index][0]+"index"+index);
	    		destinationpoint=new LatLng(distance[index][1], distance[index][2]);
	    		findDistances(rikshawlist);
	    	}
	      	else
	      	  textview_time.setText("Auto rickshaw not available nearby you");
        	}
        	catch(Exception e){
        		System.out.println("Nearest rickshaw find me exception");
        	}
        	
        }
     
    }

    LatLng destinationpoint,originpoint;
	private void drawMarker(LatLng point){
		
        // Creating an instance of MarkerOptions
        MarkerOptions markerOptions = new MarkerOptions();
 
        // Setting latitude and longitude for the marker
        markerOptions.position(point);
        markerOptions.icon(BitmapDescriptorFactory
                .fromResource(R.drawable.autosmall));
        // Adding marker on the Google Map
        mGoogleMap.addMarker(markerOptions);
    }
	
	
	 private void findDistances(ArrayList<RickshawDetails> rikshawlist) {
		 originpoint=new LatLng(center.latitude, center.longitude);
		//	for (int i = 0; i < rikshawlist.size(); i++) {
				// Getting URL to the Google Directions API
                String url = getDirectionsUrl(originpoint, destinationpoint);
                DownloadTask downloadTask = new DownloadTask();
                // Start downloading json data from Google Directions API
                downloadTask.execute(url);
			//}
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
		// TODO Auto-generated method stub
		try{
		System.out.println("center.lat="+center.latitude+"="+myoriginallocation.latitude);
		System.out.println("center.long="+center.longitude+"="+myoriginallocation.longitude);
		
	    Location locationA = new Location("A");
	    locationA.setLatitude(center.latitude);
	    locationA.setLongitude(center.longitude);
	    Location locationB = new Location("B");
	    locationB.setLatitude(myoriginallocation.latitude);
	    locationB.setLongitude(myoriginallocation.longitude);
	    
	    double distance=locationA.distanceTo(locationB);
	    System.out.println("distace="+distance);
	    
		if(v.equals(button_ridenow)){
			
			if(distance<2)
			    askbookingdialog("Book Your Ride", "Do you want auto to pick you up?" 
					, 0);
			else
				askbookingdialog("Book Your Ride", "The pickup location you have set is different from your current location. " +
						"Do you want an auto at this location?", 0);
		}
		// TODO Auto-generated method stub
		else if(v.equals(button_ridelater)){
			if(distance<2)
			       askbookingdialog("Book Your Ride", "Do you want auto to pick you up?", 1);
				else
					askbookingdialog("Book Your Ride", "The pickup location you have set is different from your current location. " +
							"Do you want an auto at this location?", 1);
			
		}
		
		// TODO Auto-generated method stub
		else if(v.equals(addresses)){
		//	startActivity(new Intent(getActivity(),SearchaddressActivity.class));
		}
		}
		catch(Exception e){
			popupdialog("Network problem", "Trying to locate your location", 0);
		}
	}
	
	
	
	
	 Button datetimepicker;
	private void ridelaterDialog() {
		booknoworlater=BOOKLATER;
		from="";
		to="";
		LayoutInflater li = LayoutInflater.from(getActivity());
		final View promptsView=li.inflate(R.layout.popup_ridelater, null);
		//AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
	 //   atvPlacesfrom = (AutoCompleteTextView) promptsView.findViewById(R.id.atv_placesfrom);
       // atvPlacesfrom.setThreshold(1);
        Button confirm = (Button) promptsView.findViewById(R.id.button_confirm);
        ImageButton cancle = (ImageButton) promptsView.findViewById(R.id.imageButton_cancel);
        TextView fromtext = (TextView) promptsView.findViewById(R.id.textView_to);
        TextView totext = (TextView) promptsView.findViewById(R.id.textView_timedate);
        Typeface typeFace=Typeface.createFromAsset(getActivity().getAssets(),"fontawesome.ttf");
		 
        totext.setTypeface(typeFace);
        fromtext.setTypeface(typeFace);
        confirm.setTypeface(typeFace);
        
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity(),R.style.DialogSlideAnim);
		alertDialogBuilder.setView(promptsView);
		alertDialog3=null;
		if(alertDialog3==null)
		alertDialog3 = alertDialogBuilder.create();
		if(alertDialog3.isShowing()) {
			alertDialog3.dismiss();
		}
		alertDialog3.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
		
		alertDialog3.show();
		YoYo.with(Techniques.SlideInDown).duration(250)
        .playOn(alertDialog3.getWindow().getDecorView());
        
		
		datetimepicker=(Button)promptsView.findViewById(R.id.buttontimesetter);
        datetimepicker.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				datechooserDialog();
				
			}
		});
 
//        atvPlacesfrom.addTextChangedListener(new TextWatcher() {
// 
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                
//                if(placesTask!=null)
//                {
//                        if(placesTask.getStatus() == AsyncTask.Status.PENDING ||
//                        		placesTask.getStatus() == AsyncTask.Status.RUNNING ||
//                        		placesTask.getStatus() == AsyncTask.Status.FINISHED)
//                        {
//                                Log.i("--placesDownloadTask--","progress_status : "+placesTask.getStatus());
//                                placesTask.cancel(true);
//                        }
//                }
//            }
 
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count,
//            int after) {
//                // TODO Auto-generated method stub
//            }
// 
//            @Override
//            public void afterTextChanged(Editable s) {
//                // TODO Auto-generated method stub
//                placesTask = new PlacesTask();
//
//                // Getting url to the Google Places Autocomplete api
//                String url = s.toString();
//
//                // Start downloading Google Places
//                // This causes to execute doInBackground() of DownloadTask class
//                placesTask.execute(url);
//            }
//        });
//        
        atvPlacesto = (AutoCompleteTextView) promptsView.findViewById(R.id.atv_placesto);
        atvPlacesto.setThreshold(1);
 
        atvPlacesto.addTextChangedListener(new TextWatcher() {
 
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                suggestionselected=false;
                if(placesTask!=null)
                {
                        if(placesTask.getStatus() == AsyncTask.Status.PENDING ||
                        		placesTask.getStatus() == AsyncTask.Status.RUNNING ||
                        		placesTask.getStatus() == AsyncTask.Status.FINISHED)
                        {
                                Log.i("--placesDownloadTask--","progress_status : "+placesTask.getStatus());
                                placesTask.cancel(true);
                        }
                }
            }
 
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
            int after) {
                // TODO Auto-generated method stub
            }
 
            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
                placesTask = new PlacesTask();

                // Getting url to the Google Places Autocomplete api
                String url = s.toString();

                // Start downloading Google Places
                // This causes to execute doInBackground() of DownloadTask class
                placesTask.execute(url);
            }
        });
//		final Dialog dialog = new Dialog(getActivity());
//		dialog.setContentView(promptsView);
//		//dialog.setTitle("Shopping List Name");
//		dialog.setCancelable(true); 
//	    dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
//		dialog.show();
        
        cancle.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				alertDialog3.dismiss();
				
			}
		});
        
        
        confirm.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				 //from= atvPlacesfrom.getText().toString();
				from=center.latitude+","+center.longitude;
				if(suggestionselected){
				 to= atvPlacesto.getText().toString();
				 
				 if(date!=null&&time!=null&&from!=null){
				 if(from.length()>0&&date.length()>0&&time.length()>0){
					 new Bookride().execute();
					 alertDialog3.dismiss();
					}
					else{
						Toast.makeText(getActivity(), "Please enter time and date of ride", Toast.LENGTH_SHORT).show();
					}
				 }
				 else{
						Toast.makeText(getActivity(), "Please enter time and date of ride", Toast.LENGTH_SHORT).show();
					}
				}	
				
				else{
					 Toast.makeText(getActivity(), "Please enter a correct drop location", Toast.LENGTH_SHORT).show();
					}
				
			}
		});
        
//        Animation bottomUp = AnimationUtils.loadAnimation(getActivity().getApplicationContext(),
//	            R.anim.bottomup);
//        promptsView.startAnimation(bottomUp);
//        promptsView.setVisibility(View.VISIBLE);
        
  	}

	
	
	protected void datechooserDialog() {
		LayoutInflater li = LayoutInflater.from(getActivity());
		final View promptsView=li.inflate(R.layout.datetimedialog, null);
		final Dialog dialog = new Dialog(getActivity());
         Button done=(Button)promptsView.findViewById(R.id.button_done);
         Button cancel=(Button)promptsView.findViewById(R.id.button_cancel);
         final DatePicker datepicker=(DatePicker)promptsView.findViewById(R.id.datePicker1);
         final TimePicker timepicker=(TimePicker)promptsView.findViewById(R.id.timePicker1);
        
         done.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				datetimepicker.setText((datepicker.getMonth() + 1) +"/"+datepicker.getDayOfMonth()
		                        +"/"+datepicker.getYear()+"\n"+timepicker.getCurrentHour()+":"
		                        +timepicker.getCurrentMinute());
				 date=datepicker.getDayOfMonth() +"-"+(datepicker.getMonth() + 1) +"-"
                 +datepicker.getYear();
				 time=timepicker.getCurrentHour()+":"+timepicker.getCurrentMinute();
				dialog.dismiss();
				
			}
		});
         
         cancel.setOnClickListener(new OnClickListener() {
 			
 			@Override
 			public void onClick(View v) {
 				dialog.dismiss();
 			}
 		});
		
		dialog.setContentView(promptsView);
		//dialog.setTitle("Shopping List Name");
		dialog.setCancelable(true); 
		dialog.show();
		
		
	}

	
	private void ridenowDialog() {
		booknoworlater=BOOKNOW;
		from="";
		to="";
		LayoutInflater li = LayoutInflater.from(getActivity());
		final View promptsView=li.inflate(R.layout.popup_ridenow, null);
		//AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
		// atvPlacesfrom = (AutoCompleteTextView) promptsView.findViewById(R.id.atv_placesfrom);
		Button confirm = (Button) promptsView.findViewById(R.id.button_confirm);
		Button fareestimate = (Button) promptsView.findViewById(R.id.button_fareestimate);
		ImageButton cancle = (ImageButton) promptsView.findViewById(R.id.imageButton_cancel);
		TextView fromtext = (TextView) promptsView.findViewById(R.id.textView_to);
        TextView totext = (TextView) promptsView.findViewById(R.id.textView_from);
        Typeface typeFace=Typeface.createFromAsset(getActivity().getAssets(),"fontawesome.ttf");
		 
        totext.setTypeface(typeFace);
        fromtext.setTypeface(typeFace);
        confirm.setTypeface(typeFace);
        
        //atvPlacesfrom.setThreshold(1);
        
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity(),R.style.DialogSlideAnim);
		alertDialogBuilder.setView(promptsView);
		alertDialog2 = null;
		if(alertDialog2==null)
		alertDialog2 = alertDialogBuilder.create();
		if(alertDialog2.isShowing()) {
			alertDialog2.dismiss();
		}
		alertDialog2.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
		
		alertDialog2.show();
		
		YoYo.with(Techniques.SlideInDown).duration(250)
        .playOn(alertDialog2.getWindow().getDecorView());
        
 
//        atvPlacesfrom.addTextChangedListener(new TextWatcher() {
// 
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                
//                if(placesTask!=null)
//                {
//                        if(placesTask.getStatus() == AsyncTask.Status.PENDING ||
//                        		placesTask.getStatus() == AsyncTask.Status.RUNNING ||
//                        		placesTask.getStatus() == AsyncTask.Status.FINISHED)
//                        {
//                                Log.i("--placesDownloadTask--","progress_status : "+placesTask.getStatus());
//                                placesTask.cancel(true);
//                        }
//                }
//            }
// 
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count,
//            int after) {
//                // TODO Auto-generated method 
//            }
// 
//            @Override
//            public void afterTextChanged(Editable s) {
//                // TODO Auto-generated method stub
//                placesTask = new PlacesTask();
//
//                // Getting url to the Google Places Autocomplete api
//                String url = s.toString();
//
//                // Start downloading Google Places
//                // This causes to execute doInBackground() of DownloadTask class
//                placesTask.execute(url);
//            }
//        });
        
        atvPlacesto = (AutoCompleteTextView) promptsView.findViewById(R.id.atv_placesto);
        estimatedtimetopic=(TextView)promptsView.findViewById(R.id.textView_from);
        estimatedtimetopic.setText("Pick up time from now :"+timetopick);
        atvPlacesto.setThreshold(1);
 
        atvPlacesto.addTextChangedListener(new TextWatcher() {
 
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            	suggestionselected=false;
                if(placesTask!=null)
                {
                        if(placesTask.getStatus() == AsyncTask.Status.PENDING ||
                        		placesTask.getStatus() == AsyncTask.Status.RUNNING ||
                        		placesTask.getStatus() == AsyncTask.Status.FINISHED)
                        {
                                Log.i("--placesDownloadTask--","progress_status : "+placesTask.getStatus());
                                placesTask.cancel(true);
                        }
                }
            }
 
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
            int after) {
                // TODO Auto-generated method stub
            }
 
            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
                placesTask = new PlacesTask();

                // Getting url to the Google Places Autocomplete api
                String url = s.toString();

                // Start downloading Google Places
                // This causes to execute doInBackground() of DownloadTask class
                placesTask.execute(url);
            }
        });
        
        
        cancle.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				alertDialog2.dismiss();
				
			}
		});
        
        confirm.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// from= atvPlacesfrom.getText().toString();
				try{
				from=center.latitude+","+center.longitude;
				 to= atvPlacesto.getText().toString();
				 
				if(to.length()>0){
					if(suggestionselected){
					 if(from.length()>0){
						 new Bookride().execute();
						 alertDialog2.dismiss();
				     }
				   }
				   else{
					 Toast.makeText(getActivity(), "Unable to find destination. Please try again", Toast.LENGTH_SHORT).show();
					}
				}
				
				else{
					 if(from.length()>0){
						new Bookride().execute();
					    alertDialog2.dismiss();
					  
					 }
					
					}
				}
				
				catch(Exception e){
					Toast.makeText(getActivity(), "Try Again", Toast.LENGTH_SHORT).show();
				}
				 
				
					
						
				
			}
		});
        
        fareestimate.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				from=center.latitude+","+center.longitude;
				if(suggestionselected){
				  to= atvPlacesto.getText().toString();
				  if(to.length()>0){
						FareEstimate.originpoint=center;
						FareEstimate.destinationpoint=getLocationFromAddress(to);
						 alertDialog2.dismiss();
						 Intent intent =new Intent(getActivity(),FareEstimate.class);
						 startActivityForResult( intent, 200 );
					 }
					else{
						
					    Toast.makeText(getActivity(), "Please enter your destination", Toast.LENGTH_SHORT).show();
				  }
				}
				else{
				 Toast.makeText(getActivity(), "Unable to find destination. Please try again", Toast.LENGTH_SHORT).show();
				}
				
				
			}
		});
        
//        Animation bottomUp = AnimationUtils.loadAnimation(getActivity().getApplicationContext(),
//	            R.anim.bottomup);
//        alertDialog2.getWindow().getDecorView().startAnimation(bottomUp);
//        promptsView.setVisibility(View.VISIBLE);
//		final Dialog dialog = new Dialog(getActivity());
//		dialog.setContentView(promptsView);
//		//dialog.setTitle("Shopping List Name");
//		dialog.setCancelable(true); 
//		dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
//		dialog.show();
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

//Fetches all places from GooglePlaces AutoComplete Web Service
private class PlacesTask extends AsyncTask<String, Void, String>{

    @Override
    protected String doInBackground(String... place) {
        // For storing data from web service
        String data = "";

        // Obtain browser key from https://code.google.com/apis/console
        String key = "key=AIzaSyDEKDOFzRYF29e4Px5lLZLEyH1v-dOBPPc";

        String input="";

        try {
            input = "input=" + URLEncoder.encode(place[0], "utf-8");
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }

        // place type to be searched
        String types = "types=geocode";

        // Sensor enabled
        String sensor = "sensor=false";

        // Building the parameters to the web service
        String parameters = input+"&"+types+"&"+sensor+"&"+key;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/place/autocomplete/"+output+"?"+parameters;
        //system.out.println("url="+url);

        try{
            // Fetching the data from we service
            data = downloadUrl(url);
        }catch(Exception e){
            Log.d("Background Task",e.toString());
        }
        return data;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);

        // Creating ParserTask
        parserTask = new ParserTask();

        // Starting Parsing the JSON string returned by Web Service
        parserTask.execute(result);
    }
	}
	/** A class to parse the Google Places in JSON format */
	private class ParserTask extends AsyncTask<String, Integer, List<HashMap<String,String>>>{
	
	    JSONObject jObject;
	
	    @Override
	    protected List<HashMap<String, String>> doInBackground(String... jsonData) {
	
	        List<HashMap<String, String>> places = null;
	
	        PlaceJSONParser placeJsonParser = new PlaceJSONParser();
	
	        try{
	            jObject = new JSONObject(jsonData[0]);
	
	            // Getting the parsed data as a List construct
	            places = placeJsonParser.parse(jObject);
	
	        }catch(Exception e){
	            Log.d("Exception",e.toString());
	        }
	        return places;
	    }
	
	    @Override
	    protected void onPostExecute(List<HashMap<String, String>> result) {
	
	        String[] from = new String[] { "description"};
	        int[] to = new int[] { android.R.id.text1 };
	        if(result!=null){
	        for (int i = 0; i < result.size(); i++) {
	        	 //system.out.println("Place ="+result.get(i).get("description"));
			}
	        // Creating a SimpleAdapter for the AutoCompleteTextView
	        SimpleAdapter adapter = new SimpleAdapter(getActivity().getBaseContext(), result,android.R.layout.simple_list_item_2, from, to);
	        // Setting the adapter
	      //  atvPlacesfrom.setAdapter(adapter);
	        atvPlacesto.setAdapter(adapter);
	        atvPlacesto.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					suggestionselected=true;
					
				}
			});
	       
	        adapter.notifyDataSetChanged();
	        }
	    }
	}
		
	@Override
	public void onDestroy() {
	    super.onDestroy();
	   // getFragmentManager().beginTransaction().remove(this).commit();
	}
	
	
	String bookingid;
	
	private class Bookride extends AsyncTask<Void, Void, Void> {
	   	 int isSuccessful=0;
	   	 
	   	 @Override
	        protected void onPreExecute() {
	            super.onPreExecute();
	            pbar.setVisibility(ProgressBar.VISIBLE);
	        }
	 
	        @Override
	        protected Void doInBackground(Void... params) {
	        	ServiceHandler sh=new ServiceHandler();
	            String url=null;
	            if(booknoworlater==BOOKNOW){
		            url=Urlconstant.url_booknow+userlist.get(0).getUserfirstname()+"&phonenumber="+userlist.get(0).getUsernumber()+"&from_distance="+from
		            +"&to_distance="+to+"&type=bookride";
	            }
	            else{
	            	url=Urlconstant.url_booknow+userlist.get(0).getUserfirstname()+"&phonenumber="+userlist.get(0).getUsernumber()+"&from_distance="+from
		            +"&to_distance="+to+"&date1="+date+"&time1="+time+"&type=bookridelater";
	            }
	            
	            url=url.replace(" ", "%20");
	            System.out.println("url="+url);
				String response=sh.makeServiceCall(url,getActivity(),ServiceHandler.GET);
	            System.out.println("rEs="+response);
	        	if(response !=null)
				{
					JSONObject jsonobj;
					try
					{
						jsonobj =new JSONObject(response);
						//String status=jsonobj.getString("signup");
						
							JSONArray jsonarr =jsonobj.getJSONArray("bookride");
							 //system.out.println(jsonarr.get(0).toString());
							if(jsonarr.get(1).toString().compareTo("usSuccess")==0){
								isSuccessful=1;
							}
							else if(jsonarr.get(1).toString().compareTo("Success")==0){
								isSuccessful=1;
								bookingid=jsonarr.get(0).toString();
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
	       	if(isSuccessful==1)
	        	bookingdialog("Finding Drivers","Please wait while we are booking your ride",isSuccessful);
	       	else
	       		donext(isSuccessful);
	        }

	    }


	public void donext(int isSuccessful) {
		if(isSuccessful==1){
			popupdialog("Thank You", "Your ride details has been registered with us. You ride will be confirmed in few minutes.",isSuccessful);
		}
		else if(isSuccessful==0){
			popupdialog("Oops!", "Something went wrong. Please try again",isSuccessful);
		}
		
	}
	
	AlertDialog alertDialog1 = null;
	private void popupdialog(String title,String message,final int isSuccessful) {
   	LayoutInflater li = LayoutInflater.from(getActivity());
		View promptsView = li.inflate(R.layout.custom_dialog, null);
		TextView titleview=(TextView)promptsView.findViewById(R.id.textView1);
		TextView descview=(TextView)promptsView.findViewById(R.id.textViewcompletedes);
		Button next=(Button)promptsView.findViewById(R.id.button_close);
		
		titleview.setText(title);
		descview.setText(message);
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
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
				}
				
				else{
					alertDialog1.dismiss();
				}
			}
		});
	
	}
	
	private void bookingdialog(String title,String message,final int isSuccessful) {
   	LayoutInflater li = LayoutInflater.from(getActivity());
		View promptsView = li.inflate(R.layout.custom_dialogforbooking, null);
		TextView titleview=(TextView)promptsView.findViewById(R.id.textView1);
		final TextView textprogress=(TextView)promptsView.findViewById(R.id.textViewprogress);
		TextView descview=(TextView)promptsView.findViewById(R.id.textViewcompletedes);
		Button cancelride=(Button)promptsView.findViewById(R.id.button_close);
		final ProgressBar circleprogress=(ProgressBar)promptsView.findViewById(R.id.progressBar1);
		titleview.setText(title);
		descview.setText(message);
//		getActivity().runOnUiThread(new Runnable() {
//			
//			@Override
//			public void run() {
//				// TODO Auto-generated method stub
//				dosomething(circleprogress,textprogress);
//				
//			}
//		});
		
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
		alertDialogBuilder.setView(promptsView);
		
		alertDialog1 = null;
		if(alertDialog1==null)
		alertDialog1 = alertDialogBuilder.create();
		if(alertDialog1.isShowing()) {
			alertDialog1.dismiss();
		}
		alertDialog1.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
		
		alertDialog1.show();
		
		 MAXTIME=0;
		 handler.removeCallbacks(checkifanyoneaccepted);
         handler.postDelayed(checkifanyoneaccepted, MIN_TIME_FOR_CHECK); // 1 second		
		
		cancelride.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
					alertDialog1.dismiss();
					 MAXTIME=0;
					 handler.removeCallbacks(checkifanyoneaccepted);
				    //callcanel ride service
					 new CancelRide().execute();
			}
		});
	
	}
	
	int mProgressStatus;
	Handler mHandler =new Handler();
	public void dosomething(final ProgressBar circleprogress, final TextView textprogress) {
            
		  new Thread(new Runnable() {
		        public void run() {
		        	
		            while (mProgressStatus < 100) {
		            	if(MAXTIME>0){
		    		        final int presentage=(mProgressStatus*100)/60;
		            	
		                mProgressStatus += 1;
		                // Update the progress bar
		                mHandler.post(new Runnable() {
		                    public void run() {
		                    	circleprogress.setProgress(presentage);
		                    	textprogress.setText(""+presentage+"%");

		                    }
		                });
		                try {
		                    Thread.sleep(50);

		                } catch (InterruptedException e) {
		                    e.printStackTrace();
		                }
		            }
		        }
		        }
		    }).start();
		  
		  }

	private void askbookingdialog(String title,String message,final int isSuccessful) {
	   	LayoutInflater li = LayoutInflater.from(getActivity());
			View promptsView = li.inflate(R.layout.cutom_askforbook, null);
			TextView titleview=(TextView)promptsView.findViewById(R.id.textView1);
			TextView descview=(TextView)promptsView.findViewById(R.id.textViewcompletedes);
			Button cancelride=(Button)promptsView.findViewById(R.id.button_close);
			Button confirm=(Button)promptsView.findViewById(R.id.button_ok);
			
			titleview.setText(title);
			descview.setText(message);
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
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
							ridenowDialog();
						}
						else{
							ridelaterDialog();
						}
				}
			});
		
		}
	
	
	
	
	
	private final Handler handler = new Handler();
	int MAXTIME=0;
	 // The minimum time between updates in milliseconds
    private static final long MIN_TIME_FOR_CHECK = 1000 *10* 1; // 1/2 minute
 
    
	 //-----------------------------------------CHECK IF ANY DRIVER ACCEPTED----------
    private Runnable checkifanyoneaccepted = new Runnable() {
        public void run() {
        	if(!isconfirmed&&!iscancelled){
        		System.out.println("CURRENT PROGRESS="+MAXTIME);
                if(MAXTIME<60){
                	//Toast.makeText(getActivity(), MAXTIME+" Kisi ne kiya?", Toast.LENGTH_SHORT).show(); 
                	 handler.postDelayed(this, MIN_TIME_FOR_CHECK); // 10 seconds here you can give your time
                	 MAXTIME=MAXTIME+10;
                	
                	 //call webservice for checking booking status.
                	 new Checkforconfirm().execute();
                }
                else{
                	handler.removeCallbacks(checkifanyoneaccepted);
                	if(alertDialog1!=null)
                		alertDialog1.dismiss();
                	MAXTIME=0;
                	//popupdialog("No Rickshaw Drivers available", "Your booking cannot be done due to inavalibility of drivers.", 1);
                	nodriverfounddialog();
                	//Toast.makeText(getActivity(), MAXTIME+"kisine nai kiya accept?", Toast.LENGTH_SHORT).show(); 
                }
                
          }
        }
    };    

    
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
            
            if(result!=null){
            if(result.size()<1){
                Toast.makeText(getActivity(), "No Points", Toast.LENGTH_SHORT).show();
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
            //system.out.println("Distance:"+distance + ", Duration:"+duration);
            
            timetopick=duration;
          	  textview_time.setText("Nearest Rickshaw is "+duration+" away");
          
           // tvDistanceDuration.setText("Distance:"+distance + ", Duration:"+duration);
 
            // Drawing polyline in the Google Map for the i-th route
            //mGoogleMap.addPolyline(lineOptions);
            }
        }
    }



    boolean mMapIsDragging;
    //// ---------------------------- Detect touch-----------------------------
    public class TouchableWrapper extends FrameLayout {
       
        
        public TouchableWrapper(Context context) {
    		super(context);
    		
    	}
        
        @Override
        public boolean dispatchTouchEvent(MotionEvent ev) {
        	 mMapIsDragging=false;
            switch (ev.getAction()) {
                case MotionEvent.ACTION_MOVE:
                    mMapIsDragging = true;
                break;
               
            }
           //system.out.println("map drag="+mMapIsDragging);

            return super.dispatchTouchEvent(ev);

        }
      
       
	

    }

    
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
    	// TODO Auto-generated method stub
    	super.onActivityResult(requestCode, resultCode, data);
    	System.out.println("requestCode="+requestCode);
    	System.out.println("response="+resultCode);
    	if(requestCode==100){
    	if(SearchaddressActivity.Selectedlocation!=null){
    	 CameraPosition cameraPosition = new CameraPosition.Builder()
         .target(SearchaddressActivity.Selectedlocation).zoom(15f).build();
		 mGoogleMap.setMyLocationEnabled(true);
		 mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
		 
		 new GetLocationAsync(SearchaddressActivity.Selectedlocation.latitude, SearchaddressActivity.Selectedlocation.longitude)
         .execute();
    	}
    	}
    	else if(requestCode==200){
    		 new Bookride().execute();
    	}
    }


	@Override
	public boolean onDrag(View v, DragEvent event) {
		// TODO Auto-generated method stub
		//system.out.println("ss");
		return false;
	}
	
	
	public LatLng getLocationFromAddress(String strAddress) {
        System.out.println("adrresssss==="+strAddress);
	    Geocoder coder = new Geocoder(getActivity());
	    List<Address> address;
	    LatLng p1 = null;

	    try {
	        address = coder.getFromLocationName(strAddress, 5);
	        if (address == null) {
	            return null;
	        }
	        Address location = address.get(0);
	        location.getLatitude();
	        location.getLongitude();

	        p1 = new LatLng(location.getLatitude(), location.getLongitude() );

	    } catch (Exception ex) {

	        ex.printStackTrace();
	    }

	    return p1;
	}

	@Override
	public void onLocationChanged(Location arg0) {
		//System.out.println("Location "+arg0.getLatitude()+"+"+arg0.getLongitude());
		myoriginallocation=new LatLng(arg0.getLatitude(), arg0.getLongitude());
		
	}

    public static String bookin_ID;
	//--------------------------CANCEL RIDE-----------------------------------------------------------------------------
	private class CancelRide extends AsyncTask<String, Void, String> {
        int issuccessfull=0;
        
        @Override
        protected void onPreExecute() {
        	iscancelled=true;
        	pbar.setVisibility(ProgressBar.VISIBLE);
        }

        @Override
        protected String doInBackground(String... params) {

        	ServiceHandler sh=new ServiceHandler();
            String url=null;
	            url=Urlconstant.url_cancleride+bookingid+"&type=bookingcenceled";
            url=url.replace(" ", "%20");
            System.out.println("url="+url);
		
            String response=sh.makeServiceCall(url,getActivity(),ServiceHandler.GET);
            System.out.println("rEs="+response);
        	if(response !=null)
			{
				JSONObject jsonobj;
				try
				{
					jsonobj =new JSONObject(response);
					//String status=jsonobj.getString("signup");
					
						JSONArray jsonarr =jsonobj.getJSONArray("bookingcencel");
						 //system.out.println(jsonarr.get(0).toString());
						if(jsonarr.get(0).toString().compareTo("unsuccess")==0){
							issuccessfull=0;
						}
						else if(jsonarr.get(0).toString().compareTo("success")==0){
							issuccessfull=1;
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
        	pbar.setVisibility(View.INVISIBLE);
        	if(issuccessfull==1)
        		popupdialog("Ride Cancelled", "Your currently booked ride has been cancelled", issuccessfull);
        	else
        		popupdialog("Ride Cancelled Failed", "Your currently booked ride cannot be cancelled", issuccessfull);
        }

      
    }

	
	//--------------------------Check if ride accepted -----------------------------------------------------------------------------
		private class Checkforconfirm extends AsyncTask<String, Void, String> {
			String status,drivername,driverno,driverId,driverlat,driverlong,driverrickshawnum;
	     
	        @Override
	        protected void onPreExecute() {
	        }

	        @Override
	        protected String doInBackground(String... params) {
	        	ServiceHandler sh=new ServiceHandler();
	            String url=Urlconstant.url_getstatusofbooking+bookingid+"&type=userbookingstauts";
	            url=url.replace(" ", "%20");
	            
				String response=sh.makeServiceCall(url,getActivity(),ServiceHandler.GET);
				
				
//	            //system.out.println("rEs="+response);
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
									  driverId= arrobj.getString("d_driverid");
									  driverrickshawnum= arrobj.getString("d_rickhano");
									 
									 System.out.println("Dname="+drivername);
									 System.out.println("Dno="+driverno);
									 System.out.println("DId="+driverId);
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
	        	if(status.compareTo("Accepted")==0){
	        		if(alertDialog1!=null)
                		alertDialog1.dismiss();
	        		
	        		bookin_ID=bookingid;
	        		bookingconfirmed(drivername,driverno,driverlat,driverlong,driverrickshawnum,driverId);
	        	}
	           
	        }

	      
	    }
	    
	
		
		 Vibrator myVib ;
		 boolean isconfirmed=false,iscancelled=false;
		 boolean cancleEnable=true;;
		private void bookingconfirmed(final String drivername, final String driverno, final String driverlat, final String driverlong, final String driverrickshawnum, String driverId) {
			 MAXTIME=0;
			 isconfirmed=true;
			 handler.removeCallbacks(checkifanyoneaccepted);
			 cancleEnable=true;
			 System.out.println("driverlat="+driverlat);
			 System.out.println("driverlong="+driverlong);
	         //handler.postDelayed(checkifanyoneaccepted, MIN_TIME_FOR_CHECK); // 1 second	
		   	    LayoutInflater li = LayoutInflater.from(getActivity());
				View promptsView = li.inflate(R.layout.driverdetails_dialog, null);
				TextView titleview=(TextView)promptsView.findViewById(R.id.textView1);
				TextView textdrivername=(TextView)promptsView.findViewById(R.id.textViewdrivername);
				//TextView timetopick=(TextView)promptsView.findViewById(R.id.textViewtimetoreach);
				TextView autonumber=(TextView)promptsView.findViewById(R.id.textViewautonumber);
				final Button cancelride=(Button)promptsView.findViewById(R.id.button_close);
				ImageButton closebutton=(ImageButton)promptsView.findViewById(R.id.imagebutton_cancel);
				
				//countdown for cancel limit
				new CountDownTimer(120000, 1000) {//CountDownTimer(edittext1.getText()+edittext2.getText()) also parse it to long
					 public void onTick(final long millisUntilFinished) {
						 getActivity().runOnUiThread(new Runnable() {
							
							@Override
							public void run() {
								// TODO Auto-generated method stub
								 cancelride.setText("Cancel in " + millisUntilFinished / 1000);
							}
						});
						
					 }
					 public void onFinish() {
						 cancelride.setText("Cancel");
						 cancleEnable=false;
						 
					 }
				 }
				   .start();
					
				Button showonmap=(Button)promptsView.findViewById(R.id.buttonshowonmap);
				Button calldriver=(Button)promptsView.findViewById(R.id.button_ok);
				titleview.setTypeface(typeFacelatolight);
				textdrivername.setTypeface(typeFacelatolight);
				autonumber.setTypeface(typeFacelatolight);
				cancelride.setTypeface(typeFacelatolight);
				calldriver.setTypeface(typeFacelatolight);
				textdrivername.setText(drivername+"Did="+driverId);
				autonumber.setText(driverrickshawnum);
				
				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity(),R.style.DialogSlideAnim);
				alertDialogBuilder.setView(promptsView);
				
				alertDialog1 = null;
				if(alertDialog1==null)
				alertDialog1 = alertDialogBuilder.create();
				if(alertDialog1.isShowing()) {
					alertDialog1.dismiss();
				}
				alertDialog1.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
				
				alertDialog1.show();
		         
				 myVib = (Vibrator) getActivity().getSystemService(getActivity().VIBRATOR_SERVICE);
		         long[] pattern = {100};
		         myVib.vibrate(100);
		         
		         createplayerPiano("notifiy.mp3");
		         
		         closebutton.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						alertDialog1.dismiss();
						BookingDatabaseHandler bhd=new BookingDatabaseHandler(getActivity());
						Rides ride=new Rides(driverno, drivername, driverno, driverlat, driverlong, driverrickshawnum, driverno, "Accepted");
						bhd.addBrand(ride);
						popupdialog("Booking Saved", "Booking details saved in My Rides", 1);
					}
				});
				
				cancelride.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
							alertDialog1.dismiss();
						    //callcanel ride service
							if(cancleEnable)
							 new CancelRide().execute();
							else
							 popupdialog("Cancel time over", "You cannot cancel this ride. " +
							 		"Either call driver to cancel the ride.", 1);
					}
				});
			
				calldriver.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						String contactno=driverno;
						Intent callIntent = new Intent(Intent.ACTION_CALL);
						callIntent.setData(Uri.parse("tel:"+contactno));
						startActivity(callIntent);
					}
				});
				
				showonmap.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
							alertDialog1.dismiss();
						     startActivity(new Intent(getActivity(),Showbookingdetailsonmap.class));
//						LatLng latlong=new LatLng(Double.parseDouble(driverlat), Double.parseDouble(driverlat));
//						 CameraPosition cameraPosition = new CameraPosition.Builder()
//			                    .target(latlong).zoom(15f).build();
//			            mGoogleMap.setMyLocationEnabled(true);
//			            mGoogleMap.animateCamera(CameraUpdateFactory
//			                    .newCameraPosition(cameraPosition));
					}
				});
			
			
			}
		
		MediaPlayer mediapianoPlayer;
		private void createplayerPiano(String id) {
			
			// TODO Auto-generated method stub
	    	 mediapianoPlayer = null;
	    	if(mediapianoPlayer ==null){
	        
	    		mediapianoPlayer = new MediaPlayer();}
	    	else{
	    		
	    		mediapianoPlayer.reset();
	    	}
	    	
	    	AssetFileDescriptor afd;
			try {
				afd = getActivity().getAssets().openFd(id);

				mediapianoPlayer.setDataSource(afd.getFileDescriptor(),afd.getStartOffset(),afd.getLength());
				mediapianoPlayer.setOnPreparedListener(new OnPreparedListener() { 
			        @Override
			        public void onPrepared(MediaPlayer mp) {
			        	
			            mp.start();
			        }
			    });
				mediapianoPlayer.prepareAsync(); 
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			
	    	
			mediapianoPlayer.setLooping(false);
		mediapianoPlayer.setOnCompletionListener(new OnCompletionListener() {
				
				public void onCompletion(MediaPlayer mp) {
					// TODO Auto-generated method stub
					mp.release();
					
				}
			});
	
		}
			
			
		

		
		private void nodriverfounddialog() {
		   	LayoutInflater li = LayoutInflater.from(getActivity());
				View promptsView = li.inflate(R.layout.nodriverfounddialog, null);
				TextView titleview=(TextView)promptsView.findViewById(R.id.textView1);
				TextView descview=(TextView)promptsView.findViewById(R.id.textViewcompletedes);
				Button cancelride=(Button)promptsView.findViewById(R.id.button_close);
				Button confirm=(Button)promptsView.findViewById(R.id.button_ok);
				
				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
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
							new CancelRide().execute();
							
					}
				});
				
				confirm.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
							alertDialog1.dismiss();
							String contactno="9098098098";
							Intent callIntent = new Intent(Intent.ACTION_CALL);
							callIntent.setData(Uri.parse("tel:"+contactno));
							startActivity(callIntent);
					}
				});
			
			}

    
    

}