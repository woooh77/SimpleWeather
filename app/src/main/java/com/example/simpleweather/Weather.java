package com.example.simpleweather;

public class Weather {
    public String time;
    public String temp;
    public int condition;
    
    public Weather(){
        super();
    }
    
    public Weather(String time, String temp, int condition) {
        super();
        this.time = time;
        this.temp = temp;
        this.condition = condition;
    }
}