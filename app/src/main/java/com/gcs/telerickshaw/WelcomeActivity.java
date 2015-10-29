package com.gcs.telerickshaw;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;

public class WelcomeActivity extends Activity implements OnClickListener {
	
	Button createaccount,login;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setTitle("Welcome");
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_welcome);
		
		init();
	}

	private void init() {
		createaccount=(Button)findViewById(R.id.button_signup);
		createaccount.setOnClickListener(this);
		login=(Button)findViewById(R.id.button_signin);
		login.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if(v.equals(login)){
			
			Intent intent=new Intent(this, LoginActivity.class);
			startActivity(intent);
		
		}
		else if(v.equals(createaccount)){
			
			Intent intent=new Intent(this, SignupActivity.class);
			startActivity(intent);
		
		}
		
	}

	

}
