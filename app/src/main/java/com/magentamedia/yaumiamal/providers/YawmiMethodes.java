package com.magentamedia.yaumiamal.providers;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.Toast;

import com.azan.PrayerTimes;
import com.azan.TimeCalculator;
import com.azan.types.AngleCalculationType;
import com.azan.types.PrayersType;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.magentamedia.yaumiamal.DatabaseHelper;
import com.magentamedia.yaumiamal.R;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class YawmiMethodes {

    //get data from sqlite, multi methodes
    public Cursor getData(Context context, String table_name) {
        Cursor cursor;
        DatabaseHelper dbhelper = new DatabaseHelper(context);

        SQLiteDatabase sq = dbhelper.getReadableDatabase();
        cursor = sq.rawQuery("SELECT * FROM "+table_name, null);
        cursor.moveToFirst();

        dbhelper.close();

        return cursor;
    }

    public Cursor getSpecificData(Context context, String table_name, String searched, String where)
    {
        Cursor cursor;
        String sql;

        DatabaseHelper dbhelper = new DatabaseHelper(context);

        if (where != null && !where.isEmpty()) {
            sql = "SELECT " + searched + " FROM " + table_name +" WHERE "+where;
        } else {
            sql = "SELECT " + searched + " FROM " + table_name;
        }

        SQLiteDatabase sq = dbhelper.getReadableDatabase();

        cursor = sq.rawQuery(sql, null);

        cursor.moveToFirst();

        dbhelper.close();

        return cursor;
    }

    public long countSpecificData(Context context, String table_name, String where, String[] whereKey) {

        DatabaseHelper dbhelper = new DatabaseHelper(context);

        SQLiteDatabase sq = dbhelper.getReadableDatabase();

        long res =  DatabaseUtils.queryNumEntries(sq, table_name, where, whereKey);

        return res;
    }

    public boolean checkExist(Context context, String tbname, String sWhere) {
        DatabaseHelper dbhelper = new DatabaseHelper(context);
        SQLiteDatabase sq = dbhelper.getReadableDatabase();
        Cursor cursor = sq.rawQuery("SELECT * FROM "+tbname+" WHERE "+sWhere, null);

        if(cursor.moveToFirst()) {
            dbhelper.close();
            return true; //data exist
        } else {
            dbhelper.close();
            return false; //data not exist
        }
    }

    public int SQLWriteMode(Context context, String sql) {
        DatabaseHelper dbhelper = new DatabaseHelper(context);
        SQLiteDatabase sq = dbhelper.getWritableDatabase();

        try {
            sq.execSQL(sql);
            dbhelper.close();

            return 1;
        } catch (SQLException e) {
            Log.d("ERR ",e.getMessage());
            dbhelper.close();

            return 0;
        }
    }

    public Cursor joinData(Context c, String sql) {
        Cursor cursor;
        DatabaseHelper dbhelper = new DatabaseHelper(c);

        SQLiteDatabase sq = dbhelper.getReadableDatabase();
        cursor = sq.rawQuery(sql, null);
        cursor.moveToFirst();

        dbhelper.close();

        return cursor;
    }

    public long countingRow(Context context, String tablename, String where) {
        DatabaseHelper dbhelper = new DatabaseHelper(context);
        SQLiteDatabase db = dbhelper.getReadableDatabase();
        String sql;

        if (where != null && !where.isEmpty()) {
            sql = "SELECT count(*) FROM " + tablename + " WHERE " + where;
        } else {
            sql = "SELECT count(*) FROM " + tablename;
        }

        SQLiteStatement sl = db.compileStatement(sql);

        long counter = sl.simpleQueryForLong();

        dbhelper.close();

        return counter;
    }

    public void onToast(Context context, String messages) {
        Toast toast = Toast.makeText(context, messages, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.TOP|Gravity.CENTER, 0, 0);
        toast.show();
    }

    public StringBuilder capitalizem(String words) {
        String[] strArray = words.split(" ");
        StringBuilder builder = new StringBuilder();
        for (String s : strArray) {
            String cap = s.substring(0, 1).toUpperCase() + s.substring(1);
            builder.append(cap + " ");
        }
        return builder;
    }

    //milis to sqlite format date
    public String toDBDate(long milis) {
        Date date = new Date(milis);
        Calendar cal = Calendar.getInstance();
        TimeZone tz = cal.getTimeZone();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setTimeZone(tz);

        return sdf.format(date);
    }

    public String localDateFormat(long milis) {
        Date date = new Date(milis);
        Calendar cal = Calendar.getInstance();
        TimeZone tz = cal.getTimeZone();
        SimpleDateFormat sdf = new SimpleDateFormat("d MM yyy");
        sdf.setTimeZone(tz);

        return sdf.format(date);
    }

    //time only format
    public String toTimes(Date date) {
        DateFormat fr = new SimpleDateFormat("HH:mm");

        return fr.format(date);
    }

    //get azan time for date
    public PrayerTimes getAzan(double latitude, double longtitude, double altitude, double timezone) {

        GregorianCalendar date = new GregorianCalendar();
        PrayerTimes prayerTimes = new TimeCalculator()
                .date(date)
                .location(latitude, longtitude, altitude, timezone)
                .timeCalculationMethod(AngleCalculationType.MUHAMMADIYAH)
                .calculateTimes();
        prayerTimes.setUseSecond(true);

        return prayerTimes;
    }

    public boolean IsNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager connMgr = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
            return (networkInfo != null && networkInfo.isConnected());
        } else {
            return false;
        }
    }

    public boolean IsGPSEnabled(Context context) {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        if (gps) {
            return true;
        } else {
            return false;
        }
    }

    public List<Address> getCityName(Context context, double lat, double lon) {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());

        try {
            return geocoder.getFromLocation(lat, lon, 1);
        } catch (IOException e) {
            return null;
        }
    }

    public long cvInsert(Context context, String tablename, ContentValues values) {
        DatabaseHelper dbhelper = new DatabaseHelper(context);
        SQLiteDatabase sq = dbhelper.getWritableDatabase();

        long status = sq.insert(tablename, null, values);
        dbhelper.close();

        return status;
    }

    public long cvUpdate(Context context, String tablename, ContentValues values, String whereClause, String[] theID) {
        DatabaseHelper dbhelper = new DatabaseHelper(context);
        SQLiteDatabase sq = dbhelper.getWritableDatabase();

        long status = sq.update(tablename, values, whereClause, theID);

        dbhelper.close();

        return status;
    }

    public void setTextview(TextView textview, String word) {
        textview.setText(capitalizem(word).toString());
    }

    //convert gson to arraylist
    public void saveArrayList(Context context, List<String> list, String key){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(list);
        editor.putString(key, json);
        editor.apply();     // This line is IMPORTANT !!!
    }

    public List<String> getArrayList(Context context, String key){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        Gson gson = new Gson();
        String json = prefs.getString(key, null);
        Type type = new TypeToken<List<String>>() {}.getType();

        return gson.fromJson(json, type);
    }

}
