package com.gcs.telerickshaw;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;

public class Ratecard extends SherlockFragment  {
	
	TextView textstandardrates,textViewstandard1,textViewrate,textViewruppesymbol1,
	textViewstandard2,textViewrate2,textViewruppesymbol2,nightext,textViewnight1,
	textViewnightrates1,extratext,extra1,extrarate1,city;
	Typeface typeFace;
	Spinner cityselect;
	
	String quat[]={"Indore","Ujjain","Bhopal","Ludhiana","Chandigarh","Amritsar"};
	 
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		 View v = null;
		 v = inflater.inflate(R.layout.activity_fare, container, false);
		 
		 cityselect=(Spinner)v.findViewById(R.id.city);
		 ArrayAdapter<String> adapter=new ArrayAdapter<String>(getActivity(), R.layout.quantitytext,quat);
		 cityselect.setAdapter(adapter);
		 
		 city=(TextView)v.findViewById(R.id.textViewcity);
		 textstandardrates=(TextView)v.findViewById(R.id.standardtext);
		 textViewstandard1=(TextView)v.findViewById(R.id.textViewstandard1);
		 textViewrate=(TextView)v.findViewById(R.id.textViewrate);
		 textViewruppesymbol1=(TextView)v.findViewById(R.id.textViewruppesymbol1);
		 textViewstandard2=(TextView)v.findViewById(R.id.textViewstandard2);
		 textViewrate2=(TextView)v.findViewById(R.id.textViewrate2);
		 textViewruppesymbol2=(TextView)v.findViewById(R.id.textViewruppesymbol2);
		 nightext=(TextView)v.findViewById(R.id.nightext);
		 textViewnight1=(TextView)v.findViewById(R.id.textViewnight1);
		 textViewnightrates1=(TextView)v.findViewById(R.id.textViewnightrates1);
		 extratext=(TextView)v.findViewById(R.id.extratext);
		 extra1=(TextView)v.findViewById(R.id.extra1);
		 extrarate1=(TextView)v.findViewById(R.id.extrarate1);
		 
		 typeFace=Typeface.createFromAsset(getActivity().getAssets(),"fontawesome.ttf");
		 
		 city.setTypeface(typeFace);
		 textstandardrates.setTypeface(typeFace);
		 textViewstandard1.setTypeface(typeFace);
		 textViewrate.setTypeface(typeFace);
		 textViewruppesymbol1.setTypeface(typeFace);
		 textViewstandard2.setTypeface(typeFace);
		 textViewrate2.setTypeface(typeFace);
		 textViewruppesymbol2.setTypeface(typeFace);
		 nightext.setTypeface(typeFace);
		 textViewnight1.setTypeface(typeFace);
		 textViewnightrates1.setTypeface(typeFace);
		 extratext.setTypeface(typeFace);
		 extra1.setTypeface(typeFace);
		 extrarate1.setTypeface(typeFace);
		 
		return v;
	}
	
	

	

	
	

}
