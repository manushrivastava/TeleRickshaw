package com.gcs.telerickshaw;

import android.app.AlertDialog;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;

public class Panic extends SherlockFragment implements OnClickListener{
	
	TextView emrgencytext,textviewhelpline;
	Button call;
	Typeface typeFace;
	ProgressBar pbar;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		 View v = null;
		 v = inflater.inflate(R.layout.activity_panic, container, false);
		 typeFace=Typeface.createFromAsset(getActivity().getAssets(),"fontawesome.ttf");
		
		 textviewhelpline=(TextView)v.findViewById(R.id.helplinetext);
		 emrgencytext=(TextView)v.findViewById(R.id.emrgencytext);
		 
		 call=(Button)v.findViewById(R.id.button_call);
		 call.setOnClickListener(this);
		 pbar=(ProgressBar)v.findViewById(R.id.progressBar1);
		 
		 textviewhelpline.setTypeface(typeFace);
		 
		 call.setTypeface(typeFace);
		 emrgencytext.setTypeface(typeFace);
		 
		return v;
	}

	@Override
	public void onClick(View v) {
		if(v.equals(call)){
		    new Panicsituation().execute();
		}
	}
	
	
	
	private class Panicsituation extends AsyncTask<Void, Void, Void> {
	   	 int isSuccessful=0;
	   	 @Override
	        protected void onPreExecute() {
	            super.onPreExecute();
	            pbar.setVisibility(ProgressBar.VISIBLE);
	        }
	 
	        @Override
	        protected Void doInBackground(Void... params) {
//	        	ServiceHandler sh=new ServiceHandler();
//	            String url=Urlconstant.url_login+username+"&pw="+userpassword+"&type=login";
//	            System.out.println("my url"+url);
//	            url=url.replace(" ", "%20");
//	            
	        	isSuccessful=1;
//				String response=sh.makeServiceCall(url,ServiceHandler.GET);
//	            System.out.println("rEs="+response);
//	        	if(response !=null)
//				{
//					JSONObject jsonobj;
//					try
//					{
//						jsonobj =new JSONObject(response);
//						//String status=jsonobj.getString("signup");
//						
//							JSONArray jsonarr =jsonobj.getJSONArray("login");
//							 System.out.println(jsonarr.get(0).toString());
//							
//							
//						
//						
//					}catch(JSONException e)
//					{
//						e.printStackTrace();
//					}
				//}
				return null;
	        }
	        
	        @Override
	        protected void onPostExecute(Void result) {
	       	 super.onPostExecute(null);
	         pbar.setVisibility(ProgressBar.INVISIBLE);
	         
	       	 donext(isSuccessful);
	        }

	    }
	
	public void donext(int isSuccessful) {
		if(isSuccessful==1){
			popupdialog("We are here", "Telerickshaw team has recieved your request.",isSuccessful);
		}
		else if(isSuccessful==0){
			popupdialog("Not working", "Your request is not sent",isSuccessful);
		}
		
		
		
	}
	
	
	AlertDialog alertDialog1;
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
	
	
	}
	
	
	
	
	

	

	
	


