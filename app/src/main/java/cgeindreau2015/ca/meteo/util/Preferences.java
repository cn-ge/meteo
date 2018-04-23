package cgeindreau2015.ca.meteo.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Path;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.gson.Gson;

import java.util.Calendar;
import java.util.Date;

import cgeindreau2015.ca.meteo.models.OpenWeather;

/**
 * Created by cgeindreau2015 on 28/04/2017.
 */

public class Preferences {

    // Pour récupérer le fichier : Tools / Android / AndroidDevicMonitor


    private static SharedPreferences get(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static String getCity(Context context) {
        Log.v("getCity",get(context).getString(Constant.PREF_CITY,""));
        return get(context).getString(Constant.PREF_CITY,"");
    }

    public static String getLastUpdate(Context context) {
        Log.v("getLastUpdate",get(context).getString(Constant.PREF_DATE,""));
        return get(context).getString(Constant.PREF_DATE,"");
    }

    public static void setCity(Context context, String city) {
        Calendar calendar = Calendar.getInstance();
        String date = calendar.get(Calendar.DATE)+"/"+(calendar.get(Calendar.MONTH)+1)+"/"+calendar.get(Calendar.YEAR)
                +"  "+calendar.get(Calendar.HOUR) +":" + (calendar.get(Calendar.MINUTE));
        Log.v("getCity",city);
        Log.v("dateCurrent", date);
        get(context).edit().putString(Constant.PREF_DATE, date).commit();
        get(context).edit().putString(Constant.PREF_CITY, city).commit();
    }

    public static OpenWeather getOpenWeather(Context context) {
        String jsonString = get(context).getString(Constant.PREF_OPEN_WEATHER, null);
        if(jsonString != null) {
            Gson jsonObject = new Gson();
            return jsonObject.fromJson(jsonString, OpenWeather.class);
        }
        return null;
    }

    public static void setOpenWeather(Context context, OpenWeather openWeather) {
        Gson jsonObject = new Gson();
        //json.toJson(openWeather);
        get(context).edit().putString(Constant.PREF_OPEN_WEATHER, jsonObject.toJson(openWeather)).commit();
    }
}
