package com.example.simpleweather;

import android.content.Context;

import com.viki.simpleweather.R;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class Forecast {
	private static final String OPEN_WEATHER_MAP_API = 
			"http://api.openweathermap.org/data/2.5/forecast/daily?q=%s&units=metric&cnt=8&appid=" + R.string.app_id;
     
    public static JSONObject getJSON(Context context, String city){
        try {
            URL url = new URL(String.format(OPEN_WEATHER_MAP_API, URLEncoder.encode(city)));
            HttpURLConnection connection = 
                    (HttpURLConnection)url.openConnection();
             
            connection.addRequestProperty("x-api-key", 
                    context.getString(R.string.open_weather_maps_app_id));
             
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()));
             
            StringBuffer json = new StringBuffer(1024);
            String tmp="";
            while((tmp=reader.readLine())!=null)
                json.append(tmp).append("\n");
            reader.close();
             
            JSONObject data = new JSONObject(json.toString());
             
            // This value will be 404 if the request was not
            // successful
            if(data.getInt("cod") != 200){
                return null;
            }
             
            return data;
        }catch(Exception e){
            return null;
        }
    }   
}