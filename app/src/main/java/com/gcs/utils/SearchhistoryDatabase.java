package com.gcs.utils;
import java.util.ArrayList;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

 
public class SearchhistoryDatabase extends SQLiteOpenHelper {
 
    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;
 
    // Database Name
    private static final String DATABASE_NAME = "telerickshawdatabase2";
 
    // Contacts table name
    public static String TABLE_USER = "searchhistory";
 
    // Contacts Table Columns names
    private static final String KEY_ID = "id";
      public static final String TAG_ADDRESS= "address"; 
 
    public SearchhistoryDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
 
    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
    	
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_USER + "("
 + KEY_ID + " INTEGER PRIMARY KEY," + TAG_ADDRESS+ " TEXT" +")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }
 
    
    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        // Create tables again
        onCreate(db);
    }
    
    
    public void dropTable(SQLiteDatabase db){
    	// Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        // Create tables again
        onCreate(db);
    }
 
    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */
 
    // Adding new SearchHistory
   public void addBrand(SearchHistory branddata) {
        SQLiteDatabase db = this.getWritableDatabase();
 
        ContentValues values = new ContentValues();
        values.put(TAG_ADDRESS, branddata.getAddress()); 
        
        // Inserting Row
        db.insert(TABLE_USER, null, values);
        db.close(); // Closing database connection
    }
 
    // Getting single SearchHistory
    SearchHistory getbrand(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USER, new String[] { KEY_ID,
        		TAG_ADDRESS}, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
 
        SearchHistory branddata = new SearchHistory(Integer.parseInt(cursor.getString(0)),cursor.getString(1)
        		);
        
        // return SearchHistory
        return branddata;
    }
    
    // Getting  
    public String getquery() {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery;
       //if(BoothConstants.getSelectedvidhansabha()==0){
    	    selectQuery = "SELECT  * FROM " + TABLE_USER;
       //}else{
    	//   selectQuery = "SELECT  * FROM " + TABLE_GRAMPANCHAYAT + " WHERE "+ TAG_DISTRICT + "=" + "'"+BoothConstants.vidhansabhanames[BoothConstants.getSelectedvidhansabha()]+"'"+" OR " +TAG_DISTRICT + "=" + "'"+BoothConstants.vidhansabhanames[BoothConstants.getSelectedvidhansabha()].toUpperCase()+"'";
      // }
       
       
       return selectQuery;
       
    }
 
    // Getting All Partyworkers
    public ArrayList<SearchHistory> getAllBrand() {
    	ArrayList<SearchHistory> contactList = new ArrayList<SearchHistory>();
        // Select All Query
        String selectQuery = getquery();
 
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
 
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                SearchHistory SearchHistory = new SearchHistory();
                SearchHistory.setId_auto(Integer.parseInt(cursor.getString(0)));
                SearchHistory.setAddress(cursor.getString(1));
                // Adding SearchHistory to list
                contactList.add(SearchHistory);
            } while (cursor.moveToNext());
        }
 
        // return SearchHistory list
        return contactList;
    }
 
    
    // Updating single SearchHistory
    public int updatebrand(SearchHistory branddata) {
        SQLiteDatabase db = this.getWritableDatabase();
 
        ContentValues values = new ContentValues();
        values.put(TAG_ADDRESS, branddata.getAddress()); 
        // updating row
        return db.update(TABLE_USER, values, KEY_ID + " = ?",
                new String[] {1+""});
    }
 
    
    // Getting contacts Count
    public int getBrandCount() {
        String countQuery = "SELECT  * FROM " + TABLE_USER;
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
}