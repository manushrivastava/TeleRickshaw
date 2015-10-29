package com.gcs.telerickshaw;

import java.util.ArrayList;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.gcs.telerickshawadapter.RideAdapter;
import com.gcs.telerickshawadapter.Rides;
import com.gcs.utils.BookingDatabaseHandler;

public class RideHistory extends SherlockFragment  {
	
	ListView lv;
	TextView text;
	ArrayList<Rides> myridelist=new ArrayList<Rides>();
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		 View v = null;
		 v = inflater.inflate(R.layout.activity_ridehistory, container, false);
		 lv=(ListView)v.findViewById(R.id.listViewlivewall);
		 text=(TextView)v.findViewById(R.id.textViewnobookingsdone);
			 
		 BookingDatabaseHandler bhd=new BookingDatabaseHandler(getActivity());
		 
		 if(bhd.isTableExists(BookingDatabaseHandler.TABLE_RIDEHISTORY, true)){
			 myridelist=bhd.getAllBrand();
			 RideAdapter wadapter=new RideAdapter(getActivity(), myridelist);
			 lv.setAdapter(wadapter);
			 text.setVisibility(View.GONE);
		 }
		 else{
			 text.setVisibility(View.VISIBLE);
		 }
		return v;
	}
	
	

	

	
	

}
