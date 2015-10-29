package com.gcs.telerickshaw;

import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.gcs.utils.Urlconstant;

public class About extends SherlockFragment  {
	
	TextView abouttext;
	Typeface typeFace;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		 View v = null;
		 v = inflater.inflate(R.layout.activity_about, container, false);
		 abouttext=(TextView)v.findViewById(R.id.textviewabout);
		 typeFace=Typeface.createFromAsset(getActivity().getAssets(),"fontawesome.ttf");
		 abouttext.setTypeface(typeFace);
		 abouttext.setText(Html.fromHtml(Urlconstant.aboutus1));
		 
		return v;
	}
	
	

	

	
	

}
