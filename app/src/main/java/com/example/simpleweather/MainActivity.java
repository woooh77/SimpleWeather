package com.example.simpleweather;

import java.util.Date;
import java.util.Locale;
import org.json.JSONObject;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.viki.simpleweather.R;
public class MainActivity extends Activity  {
    Typeface weatherFont;
    final Context context = this;
    TextView cityField;
    TextView detailsField;
    TextView currentTemperatureField;
    TextView weatherIcon;
    Handler handler;
    LinearLayout Background;
    LinearLayout moreDetails;
    String wind_speed;
    String maxTemp;
    String minTemp;
    public MainActivity(){   
    	handler = new Handler();
    }

   @Override
	protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
		SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(getBaseContext());
		boolean check = pref.getBoolean("secondtime", true);
		if(check==true){
			Intent i = new Intent(MainActivity.this, Tutorial_screen.class);
			startActivity(i);
		}
	    weatherFont = Typeface.createFromAsset(this.getAssets(), "fonts/weather.ttf");     
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
	    setContentView(R.layout.activity_main);
        updateWeatherData(new CityPreference(this).getCity());
        cityField = (TextView)findViewById(R.id.city_field);
		cityField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInputDialog();
            }
        });
		
        detailsField = (TextView)findViewById(R.id.details_field);
        currentTemperatureField = (TextView)findViewById(R.id.current_temperature_field);
        weatherIcon = (TextView)findViewById(R.id.weather_icon);
        weatherIcon.setTypeface(weatherFont);
        final SwipeRefreshLayout swipeView = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        swipeView.setColorScheme(R.color.blue, R.color.purple, R.color.green, R.color.orange);
        swipeView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeView.setRefreshing(true);
                ( new Handler()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                    	updateWeatherData(cityField.getText().toString());
                        swipeView.setRefreshing(false);
                    }
                }, 3000);
            }
        });
   }
   private void updateWeatherData(final String city){
	   new Thread(){
           public void run(){
               final JSONObject json = RemoteFetch.getJSON(getApplication(), city);
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
           cityField.setText(json.getString("name").toUpperCase(Locale.US) + 
                   ", " + 
                   json.getJSONObject("sys").getString("country"));
            
           JSONObject details = json.getJSONArray("weather").getJSONObject(0);
           JSONObject main = json.getJSONObject("main");
           detailsField.setText(
                   details.getString("description").toUpperCase(Locale.US) +
                   "\n" + "Humidity: " + main.getString("humidity") + "%" +
                   "\n" + "Pressure: " + main.getString("pressure") + " hPa");
           currentTemperatureField.setText(String.format("%.2f", main.getDouble("temp"))+ " ℃");
           setWeatherIcon(details.getInt("id"),
                   json.getJSONObject("sys").getLong("sunrise") * 1000,
                   json.getJSONObject("sys").getLong("sunset") * 1000);
           wind_speed = json.getJSONObject("wind").getString("speed");
           maxTemp = json.getJSONObject("main").getString("temp_max");
           minTemp = json.getJSONObject("main").getString("temp_min");
       }catch(Exception e){
           Log.e("SimpleWeather", "One or more fields not found in the JSON data");
       }
   }
   
   private void setWeatherIcon(int actualId, long sunrise, long sunset){
       int id = actualId / 100;
       String icon = "";
       Background= (LinearLayout)findViewById(R.id.picture);
       if(actualId == 800){
           long currentTime = new Date().getTime();
           if(currentTime>=sunrise && currentTime<sunset) {
               icon = this.getString(R.string.weather_sunny);
               Background.setBackgroundResource(R.color.sunny);
           } else {
               icon = this.getString(R.string.weather_clear_night);
               Background.setBackgroundResource(R.color.night);
           }
       } else {
           switch(id) {
           case 2 : icon = this.getString(R.string.weather_thunder);
           			Background.setBackgroundResource(R.color.thunder);
                    break;
           case 3 : icon = this.getString(R.string.weather_drizzle);
           			Background.setBackgroundResource(R.color.drizzle);
                    break;
           case 7 : icon = this.getString(R.string.weather_foggy);
           			Background.setBackgroundResource(R.color.foggy);
                    break;
           case 8 : icon = this.getString(R.string.weather_cloudy);
           			Background.setBackgroundResource(R.color.cloudy);
                    break;
           case 6 : icon = this.getString(R.string.weather_snowy);
           			Background.setBackgroundResource(R.color.snowy);
                    break;
           case 5 : icon = this.getString(R.string.weather_rainy);
           			Background.setBackgroundResource(R.color.rainy);
           			break;
           }
       }
       weatherIcon.setText(icon);
   }

	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return false;
    }
    
    private void showInputDialog(){
    	final Dialog dialog = new Dialog(context,R.style.PauseDialog);
    	dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.custom_dialog);
        final EditText input = (EditText)dialog.findViewById(R.id.textView1);
        input.setHint(cityField.getText());
        input.setHintTextColor(Color.parseColor("#ecf0f1"));
		Button dialogButton = (Button) dialog.findViewById(R.id.yesexit);
		dialogButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String cityName = input.getText().toString();
				if (!cityName.isEmpty()){
					changeCity(cityName);
				}
				dialog.dismiss();
			}
		});
		dialog.show();
    }

    public void forecast_week(View v){
		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		Editor editor = pref.edit();
		editor.putString("city", cityField.getText().toString());
		editor.commit();
		Intent i = new Intent(MainActivity.this, Forecast_activity.class);
		startActivity(i);
		overridePendingTransition(R.anim.trans_right_in, R.anim.trans_left_out);
    }
    
    public void forecast_days(View v){
		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		Editor editor = pref.edit();
		editor.putString("city", cityField.getText().toString());
		editor.commit();
		Intent i = new Intent(MainActivity.this, Forecast_hour_activity.class);
		startActivity(i);
		overridePendingTransition(R.anim.trans_left_in, R.anim.trans_right_out);
    }
    
    public void changeCity(String city){
    	new CityPreference(this).setCity(city);
        updateWeatherData(city);
    }
     
    @Override
    public void onBackPressed(){
    	finish();
    	overridePendingTransition(R.anim.trans_top_in, R.anim.trans_bottom_out);
    }
}
