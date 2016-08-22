package com.example.simpleweather;

import com.viki.simpleweather.R;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class WeatherAdapter extends ArrayAdapter<Weather>{
    int i = 0;

    Context context; 
    int layoutResourceId;    
    Weather data[] = null;
    
    public WeatherAdapter(Context context, int layoutResourceId, Weather[] data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        WeatherHolder holder = null;
        
        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            
            holder = new WeatherHolder();
            holder.txtTime = (TextView)row.findViewById(R.id.time);
            holder.txtTemp = (TextView)row.findViewById(R.id.temp);
            row.setTag(holder);
        }
        else
        {
            holder = (WeatherHolder)row.getTag();
        }
/*        int[] androidColors = context.getResources().getIntArray(R.array.rainbow);
        row.setBackgroundColor(androidColors[i]);
        i++;
        if (i==9)
        	i=0;
*/
        Weather weather = data[position];
        holder.txtTime.setText(weather.time);
        holder.txtTemp.setText(weather.temp);
        int id = weather.condition;
        if (id == 800)
        	row.setBackgroundColor(Color.parseColor("#000000"));
        else
        {	id = id / 100;
        	switch(id) {
	           case 2 : row.setBackgroundResource(R.drawable.thunder);
	        			break;
			   case 3 : row.setBackgroundResource(R.drawable.drizzle);
				   		break;
			   case 7 : row.setBackgroundResource(R.drawable.foggy);
			            break;
			   case 8 : row.setBackgroundResource(R.drawable.cloudy);
			            break;
			   case 6 : row.setBackgroundResource(R.drawable.snowy);
			            break;
			   case 5 : row.setBackgroundResource(R.drawable.rainy);
			   			break;
	        }
        }
        return row;
    }
    
    static class WeatherHolder
    {
        TextView txtTime;
        TextView txtTemp;
    }
}