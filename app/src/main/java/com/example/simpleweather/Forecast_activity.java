package com.example.simpleweather;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Random;

import org.json.JSONException;
import org.json.JSONObject;

import com.viki.simpleweather.R;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Forecast_activity extends Activity{
    Typeface weatherFont;
    TextView description;
    TextView day;
    final Context context = this;
    TextView weatherIcon;
    Handler handler;
    String city;
    LinearLayout Background;
    public Forecast_activity(){   
    	handler = new Handler();
    }

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	    weatherFont = Typeface.createFromAsset(this.getAssets(), "fonts/weather.ttf");     
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    	int[] androidColors = getResources().getIntArray(R.array.rainbow);
    	int randomStr = androidColors[new Random().nextInt(androidColors.length)];
	    setContentView(R.layout.forecast_layout);
	    Background = (LinearLayout)findViewById(R.id.picture);
	    Background.setBackgroundColor(randomStr);
	    SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		city = pref.getString("city", "Boston,MA");
	    updateWeatherData(city);

        int[] layout = {R.id.day_1, R.id.day_2, R.id.day_3, R.id.day_4, R.id.day_5, R.id.day_6};
        for (int i = 0; i < layout.length; i++) {
            LinearLayout day = (LinearLayout) findViewById(layout[i]);
            day.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				int id = v.getId();
				switch(id){
				case R.id.day_1:
					showMoreDetails(1);
					break;
				case R.id.day_2:
					showMoreDetails(2);
					break;
				case R.id.day_3:
					showMoreDetails(3);
					break;
				case R.id.day_4:
					showMoreDetails(4);
					break;
				case R.id.day_5:
					showMoreDetails(5);
					break;
				case R.id.day_6:
					showMoreDetails(6);
					break;
				}
			}
            });
        }
	}
	   private void updateWeatherData(final String city){
		   new Thread(){
	           public void run(){
	               final JSONObject json = Forecast.getJSON(getApplication(), city);
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
	       try {
	    	   JSONObject details = json.getJSONArray("list").getJSONObject(1);
	           JSONObject details1 = json.getJSONArray("list").getJSONObject(2);
	           JSONObject details2 = json.getJSONArray("list").getJSONObject(3);
	    	   JSONObject details3 = json.getJSONArray("list").getJSONObject(4);
	           JSONObject details4 = json.getJSONArray("list").getJSONObject(5);
	           JSONObject details5 = json.getJSONArray("list").getJSONObject(6);
	           description = (TextView)findViewById(R.id.description1);
	           description.setText(details.getJSONArray("weather").getJSONObject(0).getString("main"));
	           description = (TextView)findViewById(R.id.description2);
	           description.setText(details1.getJSONArray("weather").getJSONObject(0).getString("main"));
	           description = (TextView)findViewById(R.id.description3);
	           description.setText(details2.getJSONArray("weather").getJSONObject(0).getString("main"));
	           description = (TextView)findViewById(R.id.description4);
	           description.setText(details3.getJSONArray("weather").getJSONObject(0).getString("main"));
	           description = (TextView)findViewById(R.id.description5);
	           description.setText(details4.getJSONArray("weather").getJSONObject(0).getString("main"));
	           description = (TextView)findViewById(R.id.description6);
	           description.setText(details5.getJSONArray("weather").getJSONObject(0).getString("main"));
			   weatherIcon = (TextView)findViewById(R.id.weather_icon1);
	           setWeatherIcon(details.getJSONArray("weather").getJSONObject(0).getInt("id"), weatherIcon);
			   weatherIcon = (TextView)findViewById(R.id.weather_icon2);
	           setWeatherIcon(details1.getJSONArray("weather").getJSONObject(0).getInt("id"), weatherIcon);
			   weatherIcon = (TextView)findViewById(R.id.weather_icon3);
	           setWeatherIcon(details2.getJSONArray("weather").getJSONObject(0).getInt("id"), weatherIcon);
			   weatherIcon = (TextView)findViewById(R.id.weather_icon4);
	           setWeatherIcon(details3.getJSONArray("weather").getJSONObject(0).getInt("id"), weatherIcon);
			   weatherIcon = (TextView)findViewById(R.id.weather_icon5);
	           setWeatherIcon(details4.getJSONArray("weather").getJSONObject(0).getInt("id"), weatherIcon);
			   weatherIcon = (TextView)findViewById(R.id.weather_icon6);
	           setWeatherIcon(details5.getJSONArray("weather").getJSONObject(0).getInt("id"), weatherIcon);
	           SimpleDateFormat sdf = new SimpleDateFormat("MMM d",Locale.US);
	           day = (TextView)findViewById(R.id.day2);
	           day.setText(sdf.format(details1.getLong("dt")*1000));
	           day = (TextView)findViewById(R.id.day3);
	           day.setText(sdf.format(details2.getLong("dt")*1000));
	           day = (TextView)findViewById(R.id.day4);
	           day.setText(sdf.format(details3.getLong("dt")*1000));
	           day = (TextView)findViewById(R.id.day5);
	           day.setText(sdf.format(details4.getLong("dt")*1000));
	           day = (TextView)findViewById(R.id.day6);
	           day.setText(sdf.format(details5.getLong("dt")*1000));
	       }catch(Exception e){
	           Log.e("SimpleWeather", "One or more fields not found in the JSON data");
	       }
	   }
	   
	   protected void showMoreDetails(final int day) {
		   new Thread(){
	           public void run(){
	               final JSONObject json = Forecast.getJSON(getApplication(), city);
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
	                    	   	try {
									JSONObject details = json.getJSONArray("list").getJSONObject(day);
									String minTemp = details.getJSONObject("temp").getString("min");
									String maxTemp = details.getJSONObject("temp").getString("max");
									String wind_speed = details.getString("speed");
									String humidity = details.getString("humidity");
									String pressure = details.getString("pressure");
			                    	int[] androidColors = getResources().getIntArray(R.array.rainbow);
			               	    	int randomStr = androidColors[new Random().nextInt(androidColors.length)];
			               	    	final Dialog small_details= new Dialog(context, R.style.PauseDialog);
			               	    	small_details.requestWindowFeature(Window.FEATURE_NO_TITLE);
			               			small_details.setContentView(R.layout.more_details_new);
			               			small_details.getWindow().setBackgroundDrawable(new ColorDrawable(randomStr));
			               			TextView max_temp = (TextView)small_details.findViewById(R.id.max_temp);
			               			TextView min_temp = (TextView)small_details.findViewById(R.id.min_temp);
			               			TextView wind = (TextView)small_details.findViewById(R.id.wind);
			               			TextView pres = (TextView)small_details.findViewById(R.id.pressure);
			               			TextView humi = (TextView)small_details.findViewById(R.id.humidity);
			               			max_temp.setText(maxTemp + " ℃");
			               			min_temp.setText(minTemp + " ℃");
			               			wind.setText(wind_speed + " mps");
			               			pres.setText(pressure + " hPa");
			               			humi.setText(humidity + " %");
			               			small_details.show();
								} catch (JSONException e) {
									Log.e("SimpleWeather", "One or more fields not found in the JSON data");
								}
	                       }
	                   });
	               }               
	           }
	       }.start();
	    }
	   
	   private void setWeatherIcon(int actualId, TextView weather_icon){
           weatherIcon.setTypeface(weatherFont);
	       int id = actualId / 100;
	       String icon = "";
           switch(id) {
	           case 2 : icon = this.getString(R.string.weather_thunder);
	                    break;
	           case 3 : icon = this.getString(R.string.weather_drizzle);
	                    break;
	           case 7 : icon = this.getString(R.string.weather_foggy);
	                    break;
	           case 8 : icon = this.getString(R.string.weather_cloudy);
	                    break;
	           case 6 : icon = this.getString(R.string.weather_snowy);
	                    break;
	           case 5 : icon = this.getString(R.string.weather_rainy);
	           			break;
	       }
	       weatherIcon.setText(icon);
	   }
	    @Override
	    public void onBackPressed(){
	    	finish();
	    	overridePendingTransition(R.anim.trans_left_in, R.anim.trans_right_out);
	    }
}
