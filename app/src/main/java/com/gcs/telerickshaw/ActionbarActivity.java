package com.gcs.telerickshaw;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.gcs.utils.User;
import com.gcs.utils.UserDatabaseHandler;
import com.parse.Parse;

public class ActionbarActivity extends SherlockFragmentActivity implements OnItemClickListener{


	static Menu mymenu;
	
		private DrawerLayout mDrawerLayout;
	    private ListView mDrawerList;
	    private ActionBarDrawerToggle mDrawerToggle;
	 
	    // nav drawer title
	    private CharSequence mDrawerTitle;
	 
	    // used to store app title
	    private CharSequence mTitle;
	 
	    // slide menu items
	    private String[] navMenuTitles;
	    private TypedArray navMenuIcons;
	 
	    private ArrayList<NavDrawerItem> navDrawerItems;
	    private NavDrawerListAdapter madapter;
	    public static ArrayList<User> userlist=new ArrayList<User>();
	    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_actionbar);
		TextView name=(TextView)findViewById(R.id.textViewuname);
		TextView num=(TextView)findViewById(R.id.textViewucontact);
		UserDatabaseHandler udata=new UserDatabaseHandler(getApplicationContext());
		 userlist=udata.getAllBrand();
		 name.setText(userlist.get(0).getUserfirstname());
		 num.setText(userlist.get(0).getUsernumber());
		ActionBar actionBar = getSupportActionBar();
		actionBar.setHomeButtonEnabled(false);
		actionBar.setDisplayHomeAsUpEnabled(false);
		actionBar.setDisplayShowCustomEnabled(false);
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setDisplayUseLogoEnabled(false);
		//actionBar.setIcon(R.drawable.homeicon);
		//setTitle("iHD Wall");
		 Parse.initialize(this, "dCI0nQzQ6PU2SJJKWVZB1c8v9FfRvOca93OXSBvK", "4m6akJ5I2Sib4OzGYKfhvDb70x4fXAHL8OXypLHa");
			
		//lLayout = (DrawerLayout) findViewById(R.id.llayout);
		// provide zero dimen drawable for logo icon
	//	actionBar.setIcon(R.drawable.wishlogo_flat);
		
		createDrawer(savedInstanceState);
		//new Getcategories().execute();
 	}

	
	
	private void createDrawer(Bundle savedInstanceState) {
		mTitle = mDrawerTitle = getTitle();
		 System.out.println("lkl;k;l");
        // load slide menu items
        navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);
 
        // nav drawer icons from resources
        navMenuIcons = getResources()
                .obtainTypedArray(R.array.nav_drawer_icons);
 
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.list_slidermenu);
        mDrawerList.setOnItemClickListener(this);
        navDrawerItems = new ArrayList<NavDrawerItem>();
 
        // adding nav drawer items to array
        // Home
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[0], navMenuIcons.getResourceId(0, -1)));
        // Find People
      //  navDrawerItems.add(new NavDrawerItem(navMenuTitles[1], navMenuIcons.getResourceId(1, -1),true, "6"));
        // Photos
       // navDrawerItems.add(new NavDrawerItem(navMenuTitles[2], navMenuIcons.getResourceId(2, -1) ));
        // Communities, Will add a counter here
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[1], navMenuIcons.getResourceId(1, -1)));
        // Pages
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[2], navMenuIcons.getResourceId(2, -1)));
        // What's hot, We  will add a counter here
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[3], navMenuIcons.getResourceId(3, -1)));
        // What's hot, We  will add a counter here
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[4], navMenuIcons.getResourceId(4, -1)));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[5], navMenuIcons.getResourceId(5, -1)));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[6], navMenuIcons.getResourceId(6, -1)));
//         
 
        // Recycle the typed array
        navMenuIcons.recycle();
 
        mDrawerList.setOnItemClickListener(new SlideMenuClickListener());
 
        // setting the nav drawer list adapter
        madapter = new NavDrawerListAdapter(getApplicationContext(),
                navDrawerItems);
        mDrawerList.setAdapter(madapter);
 
        // enabling action bar app icon and behaving it as toggle button
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
 
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.drawable.homeicon, //nav menu toggle icon
                R.string.app_name, // nav drawer open - description for accessibility
                R.string.app_name // nav drawer close - description for accessibility
        ) {
            public void onDrawerClosed(View view) {
                getActionBar().setTitle(mTitle);
                // calling onPrepareOptionsMenu() to show action bar icons
                invalidateOptionsMenu();
            }
 
            public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle(mDrawerTitle);
                // calling onPrepareOptionsMenu() to hide action bar icons
                invalidateOptionsMenu();
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);
 
        if (savedInstanceState == null) {
            // on first time display view for first nav item
            displayView(0);
        }
	}
	
	
	 /**
     * Slide menu item click listener
     * */
    private class SlideMenuClickListener implements
            ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                long id) {
            // display view for selected nav drawer item
            displayView(position);
        }
    }
 
   
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        // toggle nav drawer on selecting action bar app icon/title
//        if (mDrawerToggle.onOptionsItemSelected(item)) {
//            return true;
//        }
        // Handle action bar actions click
    	System.out.println("item"+item);
        switch (item.getItemId()) {
        case android.R.id.home:
        	if(mDrawerLayout.isDrawerOpen(Gravity.LEFT))
        	{
        		mDrawerLayout.closeDrawer(Gravity.LEFT);
        	}else{
        		mDrawerLayout.openDrawer(Gravity.LEFT);
        	}
            return true;
            
        case R.id.menu_filter:
        	popupdialog("Emergency request!","Do you want Help",0);
            return true;
            
        default:
            return super.onOptionsItemSelected(item);
        }
    }
    
    AlertDialog alertDialog1 = null;
	private void popupdialog(String title,String message,final int isSuccessful) {
		LayoutInflater li = LayoutInflater.from(ActionbarActivity.this);
		View promptsView = li.inflate(R.layout.cutom_askforbook, null);
		TextView titleview=(TextView)promptsView.findViewById(R.id.textView1);
		TextView descview=(TextView)promptsView.findViewById(R.id.textViewcompletedes);
		Button cancelride=(Button)promptsView.findViewById(R.id.button_close);
		Button confirm=(Button)promptsView.findViewById(R.id.button_ok);
		confirm.setText("Yes");
		titleview.setText(title);
		descview.setText(message);
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ActionbarActivity.this);
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
					}
					else{
					}
			}
		});
	
	}
 
    /***
     * Called when invalidateOptionsMenu() is triggered
     */
//    @Override
//    public boolean onPrepareOptionsMenu(Menu menu) {
//        // if nav drawer is opened, hide the action items
//        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
//        menu.findItem(R.id.menu_search).setVisible(!drawerOpen);
//        return super.onPrepareOptionsMenu(menu);
//    }
    
    
    protected void search_query() {
		
	}



	/**
     * Diplaying fragment view for selected nav drawer list item
     * */
    
    MainActivity hfragment = new MainActivity() ;
    RideHistory rfragment = new RideHistory() ;
    Ratecard ratefragment = new Ratecard() ;
    Support supportfragment = new Support() ;
    Panic panicfragment = new Panic() ;
    About aboutfragment = new About() ;
    RideHistory ridesfragment = new RideHistory() ;
    
    private void displayView(int position) {
        // update the main content by replacing fragments
        SherlockFragment fragment = null;
        Handler handler = new Handler();
        
        final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        switch (position) {
        case 0:
        	
    	     mDrawerList.setItemChecked(position, true);
             mDrawerList.setSelection(position);
             setTitle(navMenuTitles[position]);
             Runnable runnable = new Runnable() {
            	 @Override
                 public void run() {
	             mDrawerLayout.closeDrawer(Gravity.LEFT);
	             ft.replace(R.id.frame_container, hfragment);
	             ft.addToBackStack("");
	             ft.commitAllowingStateLoss();
             }
             };
             handler.postDelayed(runnable, 300); // here 500 is the delay 
            break;
//        case 1:
//        	 mDrawerList.setItemChecked(position, true);
//             mDrawerList.setSelection(position);
//             setTitle(navMenuTitles[position]);
//             mDrawerLayout.closeDrawer(Gravity.LEFT);
//             ft.replace(R.id.frame_container, rfragment);
//             ft.commit();
//             mDrawerLayout.closeDrawer(Gravity.LEFT);
//            break;
//        case 2:
//        	 mDrawerList.setItemChecked(position, true);
//             mDrawerList.setSelection(position);
//             setTitle(navMenuTitles[position]);
//             mDrawerLayout.closeDrawer(Gravity.LEFT);
//             ft.replace(R.id.frame_container, ratefragment);
//             ft.commit();
//             mDrawerLayout.closeDrawer(Gravity.LEFT);
//        	
//            break;
           
        case 1:
      	   
      	   mDrawerList.setItemChecked(position, true);
             mDrawerList.setSelection(position);
             setTitle(navMenuTitles[position]);
             Runnable runnablehistory = new Runnable() {
            	 @Override
                 public void run() {
  	           mDrawerLayout.closeDrawer(Gravity.LEFT);
  	           ft.replace(R.id.frame_container, ridesfragment);
  	           ft.commit();
            	 }
             };
             handler.postDelayed(runnablehistory, 300); // here 500 is the delay 
            break;
            
        case 2:
     	   
     	   mDrawerList.setItemChecked(position, true);
            mDrawerList.setSelection(position);
            setTitle(navMenuTitles[position]);
            Runnable runnable0 = new Runnable() {
           	 @Override
                public void run() {
 	           mDrawerLayout.closeDrawer(Gravity.LEFT);
 	           ft.replace(R.id.frame_container, ratefragment);
 	           ft.commit();
           	 }
            };
            handler.postDelayed(runnable0, 300); // here 500 is the delay 
           break;
           
        case 3:
     	   
     	   mDrawerList.setItemChecked(position, true);
            mDrawerList.setSelection(position);
            setTitle(navMenuTitles[position]);
            Runnable runnable11 = new Runnable() {
           	 @Override
                public void run() {
 	           mDrawerLayout.closeDrawer(Gravity.LEFT);
 	           ft.replace(R.id.frame_container, panicfragment);
 	           ft.commit();
           	 }
            };
            handler.postDelayed(runnable11, 300); // here 500 is the delay 
           break;
       case 4:
    	   
    	   mDrawerList.setItemChecked(position, true);
           mDrawerList.setSelection(position);
           setTitle(navMenuTitles[position]);
           Runnable runnable1 = new Runnable() {
          	 @Override
               public void run() {
	           mDrawerLayout.closeDrawer(Gravity.LEFT);
	           ft.replace(R.id.frame_container, supportfragment);
	           ft.commit();
          	 }
           };
           handler.postDelayed(runnable1, 300); // here 500 is the delay 
          break;
        case 5:
        	//share
        	mDrawerList.setItemChecked(position, true);
            mDrawerList.setSelection(position);
            setTitle(navMenuTitles[position]);
            Runnable runnable2 = new Runnable() {
             	 @Override
                  public void run() {
		            mDrawerLayout.closeDrawer(Gravity.LEFT);
             	 }
            };
            handler.postDelayed(runnable2, 300); // here 500 is the delay 
		            
					Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND); 
			    	sharingIntent.setType("text/plain");
			    	String shareBody = "https://play.google.com/store/apps/details?id=com.gcs.telerickshaw&hl=en";
	    	
	    	sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
	    	startActivity(Intent.createChooser(sharingIntent, "Share this app via"));
            break;
        case 6:
        	mDrawerList.setItemChecked(position, true);
            mDrawerList.setSelection(position);
            setTitle(navMenuTitles[position]);
            Runnable runnable3 = new Runnable() {
            	 @Override
                 public void run() {
            mDrawerLayout.closeDrawer(Gravity.LEFT);
            ft.replace(R.id.frame_container, aboutfragment);
            ft.commit();
            	 }
            };
                 handler.postDelayed(runnable3, 300); // here 500 is the delay 
            break;

        default:
            break;
        }
 
//        if (hfragment != null) {
//            FragmentManager fragmentManager = getSupportFragmentManager();
//            fragmentManager.beginTransaction()
//                    .replace(R.id.frame_container, hfragment).commit();
// 
//            // update selected item and title, then close the drawer
//            mDrawerList.setItemChecked(position, true);
//            mDrawerList.setSelection(position);
//            setTitle(navMenuTitles[position]);
//            mDrawerLayout.closeDrawer(lLayout);
//            
//        } else {
//            // error in creating fragment
//            Log.e("MainActivity", "Error in creating fragment");
//        }
    }
 
    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getActionBar().setTitle(mTitle);
    }
 
    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */
 
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }
 
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }
 



	
    @Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportMenuInflater().inflate(R.menu.actionbar, menu);
	  
		return super.onCreateOptionsMenu(menu);
	}
	
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
    	if(keyCode==KeyEvent.KEYCODE_BACK&& event.getRepeatCount() == 0){
     	   exitdialog();
 		   
    }return true;
    	
    }
    	
    	  public void exitdialog(){
    			new AlertDialog.Builder(this)
    	        .setIcon(android.R.drawable.ic_dialog_alert)
    	        .setTitle("Exit!")
    	        .setMessage("Do you want to exit?")
    	        .setPositiveButton("Yes", new DialogInterface.OnClickListener()
    	        {
    	        @Override
    	        public void onClick(DialogInterface dialog, int which) {
    	        	 finish();  
    	        }

    	    })
    	    .setNegativeButton("No", new DialogInterface.OnClickListener()
    	        {
    	        @Override
    	        public void onClick(DialogInterface dialog, int which) {
    	        	 dialog.dismiss();
    	        }

    	    }
    	    )
    	    .show();
    		}

	
    	 

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		
	}
}
