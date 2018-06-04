package com.hjsoft.affdelguestapp.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.hjsoft.affdelguestapp.DatabaseHandler;

import java.util.ArrayList;

public class DBAdapter {

    static final String DATABASE_NAME = "cabs.db";
    static final int DATABASE_VERSION = 55;
    public static final int NAME_COLUMN = 1;


    public static final String TABLE_USER_LOCATIONS="create table if not exists "+"USER_LOCATIONS"+
            "( "+"PLACE_ID text,PLACE_NAME text,LAT double,LNG double);";

    public SQLiteDatabase db;
    // Context of the application using the database.
    private final Context context;
    // Database open/upgrade helper
    private DatabaseHandler dbHelper;

    public  DBAdapter(Context _context)
    {
        context = _context;
        dbHelper = new DatabaseHandler(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public  DBAdapter open() throws SQLException
    {
        db = dbHelper.getWritableDatabase();
        return this;
    }
    public void close()
    {
        db.close();
    }

    public  SQLiteDatabase getDatabaseInstance()
    {
        return db;
    }


    public void insertUserLocation(String place_id,String place_name,double latitude,double longitude){

        db=dbHelper.getWritableDatabase();
        ContentValues newValues = new ContentValues();
        // Assign values for each row.
        newValues.put("PLACE_ID",place_id);
        newValues.put("PLACE_NAME",place_name);
        newValues.put("LAT",latitude);
        newValues.put("LNG",longitude);
        // Insert the row into your table
        db.insert("USER_LOCATIONS", null, newValues);
    }

    public ArrayList<String> getUserLocations(){

        ArrayList<String> place=new ArrayList<>();

        db=dbHelper.getReadableDatabase();
        Cursor c=db.rawQuery("SELECT * FROM USER_LOCATIONS",null);

        if(c.getCount()>0) {
            for(int i=0;i<c.getCount();i++) {
                c.moveToNext();
                place.add(c.getString(1));
            }
        }
        c.close();
        // close();
        return place;

    }

    public String getUserLocationFromPosition(int position){

        String place="";

        db=dbHelper.getReadableDatabase();
        Cursor c=db.rawQuery("SELECT * FROM USER_LOCATIONS",null);

        if(c.getCount()>0) {
            for(int i=0;i<c.getCount();i++) {
                c.moveToNext();
                if(i==position)
                {
                    place=c.getString(1);
                }

            }
        }
        c.close();
        // close();
        return place;

    }


}
