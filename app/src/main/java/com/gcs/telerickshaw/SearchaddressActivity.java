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

import org.json.JSONObject;

import android.app.ActionBar;
import android.app.Activity;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.gcs.telerickshawadapter.SearchHistoryAdapter;
import com.gcs.utils.PlaceJSONParser;
import com.gcs.utils.SearchHistory;
import com.gcs.utils.SearchhistoryDatabase;
import com.google.android.gms.maps.model.LatLng;

public class SearchaddressActivity extends Activity implements OnClickListener,OnItemClickListener{
	
	Button clearhistory;
	AutoCompleteTextView addresssearch;
	PlacesTask placesTask;
	ParserTask parserTask;
	Typeface tf;
	TextView typeaddress;
	ListView addresslist;
	ArrayList<SearchHistory> addresshistory=new ArrayList<SearchHistory>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	//	this.requestWindowFeature(Window.FEATURE_NO_TITLE);
//		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayShowTitleEnabled(true);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		setContentView(R.layout.activity_search);
		setTitle("Search");
		init();
		overridePendingTransition(R.anim.trans_top_in, R.anim.trans_top_out);
	}
	
	
	
	private void init() 
	{
		try{
			SearchhistoryDatabase  sd=new SearchhistoryDatabase(getApplicationContext());
		
		    addresslist = (ListView)findViewById(R.id.listView_history);
		    clearhistory = (Button)findViewById(R.id.button_clearhistory);
		    addresshistory=sd.getAllBrand();
		    System.out.println("k");
		}
		catch (Exception e) {
			System.out.println("123");
			// TODO: handle exception
		}
		System.out.println("addresshistory.size()="+addresshistory.size());
		    if(addresshistory.size()>0){
		    	SearchHistoryAdapter sh=new SearchHistoryAdapter(this,addresshistory);
		    	 addresslist.setAdapter(sh);
		    }
		   
		    addresslist.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					LatLng location=getLocationFromAddress(addresshistory.get(position).getAddress());
					Selectedlocation=location;
					finish();
				}
			});
		    addresssearch = (AutoCompleteTextView)findViewById(R.id.searchplaces);
	        addresssearch.setThreshold(1);
	        Typeface tf=Typeface.createFromAsset(getAssets(),"fontawesome.ttf");
	        typeaddress=(TextView)findViewById(R.id.textView1);
	        typeaddress.setTypeface(tf);
	        addresssearch.setTypeface(tf);
	        clearhistory.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					//SearchhistoryDatabase  sd=new SearchhistoryDatabase(getApplicationContext());
					//sd.dropTable(sd);					
				}
			});
	        addresssearch.addTextChangedListener(new TextWatcher() {
	 
	            @Override
	            public void onTextChanged(CharSequence s, int start, int before, int count) {
	                
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
	}



	@Override
	public void onBackPressed() {
		super.onBackPressed();
		overridePendingTransition(R.anim.trans_bottom_in, R.anim.trans_bottom_out);
	}



	@Override
	public void onClick(View v) {
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
	        System.out.println("url="+url);

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
	    protected void onPostExecute(final List<HashMap<String, String>> result) {
	
	        String[] from = new String[] { "description"};
	        int[] to = new int[] { android.R.id.text1 };
	        if(result!=null){
	        for (int i = 0; i < result.size(); i++) {
	        	 System.out.println("Place ="+result.get(i).get("description"));
			}
	        // Creating a SimpleAdapter for the AutoCompleteTextView
	        SimpleAdapter adapter = new SimpleAdapter(getBaseContext(), result,android.R.layout.simple_list_item_2, from, to);
	       // SearchHistoryAdapter adapter = new SearchHistoryAdapter(getApplicationContext(),result);
		      
	        // Setting the adapter
	      //  atvPlacesfrom.setAdapter(adapter);
	        addresssearch.setAdapter(adapter);
	        adapter.notifyDataSetChanged();
	        
	        addresssearch.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					// TODO Auto-generated method stub
					LatLng location=getLocationFromAddress(result.get(position).get("description"));
					SearchHistory sh=new SearchHistory(result.get(position).get("description"));
					Selectedlocation=location;
					SearchhistoryDatabase  sd=new SearchhistoryDatabase(getApplicationContext());
					
					sd.addBrand(sh);
					finish();
				}
			});
	        }
	    }
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		Selectedlocation=null;
		return super.onKeyDown(keyCode, event);
		
	}
	
	public static LatLng Selectedlocation;

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		
		
	}

	
	public LatLng getLocationFromAddress(String strAddress) {
        System.out.println("adrresssss==="+strAddress);
	    Geocoder coder = new Geocoder(this);
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
}
