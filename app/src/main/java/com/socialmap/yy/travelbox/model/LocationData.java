package com.socialmap.yy.travelbox.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.socialmap.yy.travelbox.data.LocationDataBaseHelper;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by gxyzw_000 on 2015/3/26.
 */
public class LocationData {
    private LocationDataBaseHelper dbhelper = null;
    private SQLiteDatabase db = null;


    public LocationData(Context context) {
        dbhelper = LocationDataBaseHelper.getDBhelper(context);
        db = LocationDataBaseHelper.opDatabase();
    }


    public long addNew(Location ub) {
        ContentValues values = new ContentValues();
        values.put("Longitude", ub.getLongitude());
        values.put("Latitude", ub.getLatitude());
        values.put("Radius", ub.getRadius());


        return db.insert("location", null, values);

    }


    public ArrayList<HashMap<String, Float>> getUserList() {
        ArrayList<HashMap<String, Float>> al = new ArrayList<HashMap<String, Float>>();
        HashMap<String, Float> map = null;
        Cursor c = db.query("location", null, null, null, null, null, null);
        while (c.moveToNext()) {

            float Longitude = c.getFloat(c.getColumnIndex("Longitude"));
            float Latitude = c.getFloat(c.getColumnIndex("Latitude"));
            float Radius = c.getFloat(c.getColumnIndex("Radius"));

            map = new HashMap<String, Float>();
            map.put("Longitude", Longitude);
            map.put("Latitude", Latitude);
            map.put("Radius", Radius);
            al.add(map);
        }

        return al;
    }


}
