package com.gcs.telerickshawadapter;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.gcs.telerickshaw.R;


import android.app.Activity;
import android.content.Context;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.BaseAdapter;
import android.widget.TextView;


public class RideAdapter  extends BaseAdapter{
	
	private Activity activity;
	private static LayoutInflater inflater = null;
	View v;
	ArrayList<Rides> data;
	Typeface typeFace,typefacebold;

	
	public RideAdapter(Activity a, ArrayList<Rides> myartistlist) {
		activity = a;
		this.data = myartistlist;
		if(activity!=null){
		inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);}
		 typeFace=Typeface.createFromAsset(a.getAssets(),"Roboto-Regular.ttf");
		 typefacebold=Typeface.createFromAsset(a.getAssets(),"Roboto-Bold.ttf");
		
		
	}
	
	public int getCount() {
		return data.size();
	}


	public long getItemId(int position) {
		return position;
	}

	/********* Create a holder Class to contain inflated xml file elements *********/
	public static class ViewHolder {

		TextView textviewbookingid,textviewdnamevalue,textdautonovalue;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		v = convertView;
		ViewHolder holder;

		if (convertView == null) {
			
			//used iv campuslive
			v = inflater.inflate(R.layout.singleproduct, null);
			
			/****** View Holder Object to contain tabitem.xml file elements ******/

			holder = new ViewHolder();
			holder.textviewbookingid=(TextView)v.findViewById(R.id.textviewbookingidvalue);
			holder.textviewdnamevalue=(TextView)v.findViewById(R.id.textviewdrivernamevalue);
			holder.textdautonovalue=(TextView)v.findViewById(R.id.textviewdriverautonovalue);
			
			holder.textviewbookingid.setTypeface(typeFace);
			holder.textviewdnamevalue.setTypeface(typefacebold);
			holder.textdautonovalue.setTypeface(typefacebold);
			
			//holder.textDescription .setTypeface(tf);
			/************ Set holder with LayoutInflater ************/
			v.setTag(holder);
		} else
			
			holder = (ViewHolder) v.getTag();
			holder.textviewbookingid.setText(data.get(position).getBookingid());
			holder.textviewdnamevalue.setText(data.get(position).getDrivername());
			holder.textdautonovalue.setText(data.get(position).getDriverautonumber());
		
		return v;
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	

}