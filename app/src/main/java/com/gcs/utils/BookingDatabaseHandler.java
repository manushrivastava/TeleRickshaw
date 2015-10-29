package com.gcs.utils;
import java.util.ArrayList;

import com.gcs.telerickshaw.RideHistory;
import com.gcs.telerickshawadapter.Rides;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

 
public class BookingDatabaseHandler extends SQLiteOpenHelper {
 
    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;
 
    // Database Name
    private static final String DATABASE_NAME = "telerickshawdatabase";
 
    // Contacts table name
    public static String TABLE_RIDEHISTORY = "ridehistory";
 
    // Contacts Table Columns names
    private static final String KEY_ID = "id";
      public static final String TAG_BOOKINGID= "bookingid"; 
      public static final String TAG_DRIVERNAME = "dname"; 
      public static final String TAG_DRIVERID = "did"; 
      public static final String TAG_DRIVERLAT= "dlat"; 
      public static final String TAG_DRIVERLONG= "dlong"; 
      public static final String TAG_STATUS= "pending"; 
      public static final String TAG_DRIVERAUTONUM= "dautonumber"; 
      public static final String TAG_DRIVERCONTACT= "dcontact"; 
      
 
    public BookingDatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
 
    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
    	
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_RIDEHISTORY + "("
		 + KEY_ID + " INTEGER PRIMARY KEY," + TAG_BOOKINGID + " TEXT," + TAG_DRIVERNAME+ " TEXT,"
		 + TAG_DRIVERID + " TEXT, "+ TAG_DRIVERLAT + " TEXT, "+ TAG_DRIVERLONG +TAG_DRIVERAUTONUM+ " TEXT, "
		 +TAG_DRIVERCONTACT+ " TEXT, "+TAG_STATUS+ " TEXT "+")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }
 
    
    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RIDEHISTORY);
        // Create tables again
        onCreate(db);
    }
    
    
    public void dropTable(SQLiteDatabase db){
    	// Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RIDEHISTORY);
        // Create tables again
        onCreate(db);
    }
 
    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */
 
    // Adding new Bookingdata
   public void addBrand(Rides branddata) {
        SQLiteDatabase db = this.getWritableDatabase();
 
        ContentValues values = new ContentValues();
        values.put(TAG_BOOKINGID, branddata.getBookingid()); 
        values.put(TAG_DRIVERNAME, branddata.getDrivername()); 
        values.put(TAG_DRIVERID, branddata.getDriverid()); 
        values.put(TAG_DRIVERLAT, branddata.getDriverlat()); 
        values.put(TAG_DRIVERLONG, branddata.getDriverlong()); 
        values.put(TAG_DRIVERAUTONUM, branddata.getDriverautonumber()); 
        values.put(TAG_DRIVERCONTACT, branddata.getDrivercontact()); 
        values.put(TAG_STATUS, branddata.getStatus()); 
         
        // Inserting Row
        db.insert(TABLE_RIDEHISTORY, null, values);
        db.close(); // Closing database connection
    }
 
    // Getting single Bookingdata
    Rides getbrand(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_RIDEHISTORY, new String[] { KEY_ID,
        		TAG_BOOKINGID ,
        		  TAG_DRIVERNAME,TAG_DRIVERID,TAG_DRIVERLAT,TAG_DRIVERLONG,TAG_DRIVERAUTONUM,TAG_DRIVERCONTACT,TAG_STATUS}, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
 
        Rides branddata = new Rides(Integer.parseInt(cursor.getString(0)),cursor.getString(1)
        		,cursor.getString(2),cursor.getString(3),cursor.getString(4),cursor.getString(5)
        		,cursor.getString(6),cursor.getString(7),cursor.getString(8)
        		);
        
        // return Bookingdata
        return branddata;
    }
    
    // Getting  
    public String getquery() {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery;
       //if(BoothConstants.getSelectedvidhansabha()==0){
    	    selectQuery = "SELECT  * FROM " + TABLE_RIDEHISTORY + " ORDER BY "+KEY_ID+" DESC";
       //}else{
    	//   selectQuery = "SELECT  * FROM " + TABLE_GRAMPANCHAYAT + " WHERE "+ TAG_DISTRICT + "=" + "'"+BoothConstants.vidhansabhanames[BoothConstants.getSelectedvidhansabha()]+"'"+" OR " +TAG_DISTRICT + "=" + "'"+BoothConstants.vidhansabhanames[BoothConstants.getSelectedvidhansabha()].toUpperCase()+"'";
      // }
       
       
       return selectQuery;
       
    }
 
    // Getting All Partyworkers
    public ArrayList<Rides> getAllBrand() {
    	ArrayList<Rides> contactList = new ArrayList<Rides>();
        // Select All Query
        String selectQuery = getquery();
 
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
 
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
            	Rides Bookingdata = new Rides();
                Bookingdata.setId_auto(Integer.parseInt(cursor.getString(0)));
                Bookingdata.setBookingid(cursor.getString(1));
                Bookingdata.setDrivername(cursor.getString(2));
                Bookingdata.setDriverid(cursor.getString(3));
                Bookingdata.setDriverlat(cursor.getString(4));
                Bookingdata.setDriverlong(cursor.getString(5));
                Bookingdata.setDriverautonumber(cursor.getString(6));
                Bookingdata.setDrivercontact(cursor.getString(7));
                Bookingdata.setStatus(cursor.getString(8));
                // Adding Bookingdata to list
                contactList.add(Bookingdata);
            } while (cursor.moveToNext());
        }
 
        // return Bookingdata list
        return contactList;
    }
 
    
    // Updating single Bookingdata
    public int updatebrand(Rides branddata) {
        SQLiteDatabase db = this.getWritableDatabase();
 
        ContentValues values = new ContentValues();
        values.put(TAG_BOOKINGID, branddata.getBookingid()); 
        values.put(TAG_DRIVERNAME, branddata.getDrivername()); 
        values.put(TAG_DRIVERID, branddata.getDriverid()); 
        values.put(TAG_DRIVERLAT, branddata.getDriverlat()); 
        values.put(TAG_DRIVERLONG, branddata.getDriverlong()); 
        values.put(TAG_DRIVERAUTONUM, branddata.getDriverautonumber()); 
        values.put(TAG_DRIVERCONTACT, branddata.getDrivercontact()); 
        values.put(TAG_STATUS, branddata.getStatus()); 
        // updating row
        return db.update(TABLE_RIDEHISTORY, values, KEY_ID + " = ?",
                new String[] {1+""});
    }
 
    
    // Getting contacts Count
    public int getBrandCount() {
        String countQuery = "SELECT  * FROM " + TABLE_RIDEHISTORY;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();
 
        // return count
        return cursor.getCount();
    }
 
    public boolean isTableExists(String tableName, boolean openDb) {
    	System.out.println("tablename="+tableName);
    	 SQLiteDatabase mDatabase = this.getWritableDatabase();
    	 
        if(openDb) {
            if(mDatabase == null || !mDatabase.isOpen()) {
                mDatabase = getReadableDatabase();
            }

            if(!mDatabase.isReadOnly()) {
                mDatabase.close();
                mDatabase = getReadableDatabase();
            }
        }

        Cursor cursor = mDatabase.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = '"+tableName+"'", null);
        if(cursor!=null) {
            if(cursor.getCount()>0) {
                     cursor.close();
                return true;
            }
                     cursor.close();
        }
        
        mDatabase.close();
        return false;
    }
    
    public boolean deleteSingleRow(String rowId) 
    {    
    	SQLiteDatabase mDatabase = this.getWritableDatabase();
        return mDatabase.delete(TABLE_RIDEHISTORY, KEY_ID + "=" + rowId, null) > 0;
    }
}