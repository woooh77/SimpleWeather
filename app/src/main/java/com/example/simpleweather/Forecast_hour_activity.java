package com.example.simpleweather;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.Toast;

import com.viki.simpleweather.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class Forecast_hour_activity extends Activity{
	private ListView swipelistview;
	String city;
	Handler handler;
    public Forecast_hour_activity(){   
    	handler = new Handler();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.forecast_hour);
	    SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		city = pref.getString("city", "Boston,MA");
	    updateWeatherData(city);

    }
	private void updateWeatherData(final String city2) {
		new Thread(){
	           public void run(){
	               final JSONObject json = Forecast_hour.getJSON(getApplication(), city);
	               if(json == null){
	                   handler.post(new Runnable(){
	                       public void run(){
	                           Toast.makeText(getApplicationContext(), getApplication().getString(R.string.place_not_found), 
	                                   Toast.LENGTH_LONG).show(); 
	                       }
	                   });
	               } else {
	                   handler.post(new Runnable(){
	                       public void run(){
	                           renderWeather(json);
	                       }
	                   });
	               }               
	           }
	       }.start();
	}
	   private void renderWeather(JSONObject json){
		   String[] dt = new String[20];
		   String[] tp = new String[20];
		   int[] condition = new int[20];
		   SimpleDateFormat sdf = new SimpleDateFormat("EEE hh:mm a",Locale.US);
	       try {
	    	   JSONArray list = json.getJSONArray("list");
	    	   for(int i=0; i < 20; i++){
	    		   dt[i] = sdf.format((list.getJSONObject(i).getLong("dt"))*1000);
	    		   tp[i] = list.getJSONObject(i).getJSONObject("main").getString("temp");
	    		   condition[i] = list.getJSONObject(i).getJSONArray("weather").getJSONObject(0).getInt("id");
	    	   }
	       }catch(Exception e){
	           Log.e("SimpleWeather", "One or more fields not found in the JSON data");
	       }
	       Weather[] weather_data = new Weather[20];
	       for (int i = 0; i < 20; i++)
	    	   weather_data[i] = new Weather(dt[i],tp[i]+" ℃", condition[i]);
           WeatherAdapter adapter = new WeatherAdapter(this, R.layout.listview_item_row, weather_data);
		   swipelistview = (ListView)findViewById(R.id.listView1);
		   swipelistview.setAdapter(adapter);
	   }
	   
	   @Override
	    public void onBackPressed(){
	    	finish();
	    	overridePendingTransition(R.anim.trans_right_in, R.anim.trans_left_out);
	    }
}
