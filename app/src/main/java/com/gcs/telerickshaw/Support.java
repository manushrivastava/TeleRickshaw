package com.gcs.telerickshaw;

import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.gcs.utils.Urlconstant;

public class Support extends SherlockFragment implements OnClickListener{
	
	TextView textviewabout,textviewmail,textviewmailvalue,textviewhelpline,textviewhelplinevalue,textviewaddress,textviewaddressvalue;
	Button call;
	Typeface typeFace;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		 View v = null;
		 v = inflater.inflate(R.layout.activity_support, container, false);
		 typeFace=Typeface.createFromAsset(getActivity().getAssets(),"fontawesome.ttf");
		 
		 textviewabout=(TextView)v.findViewById(R.id.textviewabout);
		 textviewmail=(TextView)v.findViewById(R.id.mailtext);
		 textviewmailvalue=(TextView)v.findViewById(R.id.mailid);
		 textviewhelpline=(TextView)v.findViewById(R.id.helplinetext);
		 textviewhelplinevalue=(TextView)v.findViewById(R.id.texthelplinenumber);
		 textviewaddress=(TextView)v.findViewById(R.id.addresstext);
		 textviewaddressvalue=(TextView)v.findViewById(R.id.address);
		 
		 call=(Button)v.findViewById(R.id.button_call);
		 call.setOnClickListener(this);
		 
		 textviewabout.setText(Urlconstant.abouttext);
		 textviewmailvalue.setText(Urlconstant.mailtext);
		 textviewhelplinevalue.setText(Urlconstant.helplinetext);
		 textviewaddressvalue.setText(Urlconstant.addresstext);

		 textviewmail.setTypeface(typeFace);
		 textviewmailvalue.setTypeface(typeFace);
		 textviewhelpline.setTypeface(typeFace);
		 textviewhelplinevalue.setTypeface(typeFace);
		 textviewaddress.setTypeface(typeFace);
		 textviewaddressvalue.setTypeface(typeFace);
		 
		 call.setTypeface(typeFace);
		return v;
	}

	@Override
	public void onClick(View v) {
		if(v.equals(call)){
		String contactno="9098098098";
		
			Intent callIntent = new Intent(Intent.ACTION_CALL);
			callIntent.setData(Uri.parse("tel:"+contactno));
			startActivity(callIntent);
		}
	}
	}
	
	
	
	
	

	

	
	


