package ky.Utils;

import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import org.json.JSONObject;

import ky.model.Direction;
import ky.model.Weather;

public class MyUtils {
	public static Direction getRandomDirections(Direction selfDirection) {
		List<Direction> d = new ArrayList<>();
		for (Direction direction: Direction.values()) {
			if (direction != selfDirection) {
				d.add(direction);
			}
		}
		
		Random rand = new Random();
		return d.get(rand.nextInt(d.size()));
	}
	
	public static Weather getCurrentWeather(String cityName, String apiKey) {
		try {
	    // API URL
	    String api = "http://api.openweathermap.org/data/2.5/";
			api += "weather?q=" + URLEncoder.encode(cityName, "UTF-8");
	    api += "&APPID=" + apiKey;
	    URL apiURL;
			apiURL = new URL(api);
	    // Read raw string from URL
	    Scanner scan = new Scanner(apiURL.openStream());
	    String str = new String();
	    while (scan.hasNext())
	        str += scan.nextLine();
	    scan.close();
	 
	    
	    JSONObject obj = new JSONObject(str);
	    // check whether return status OK
	    if (obj.getInt("cod") != 200)
	        return null;
	 
	    // get the weather data
	    JSONObject weatherObj = obj.getJSONArray("weather").getJSONObject(0);
	    return getWeatherType(weatherObj.getString("main"));
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return null;
	}
	
	public static Weather getWeatherType(String weather) {
		switch (weather.toLowerCase()) {
		case "clear sky":
		 return Weather.CLEAR_SKY;
		case "few clouds":
			return Weather.FEW_CLOUDS;
		case "scattered clouds":
		 return Weather.SCATTERED_CLOUDS;
		case "broken clouds":
			return Weather.BROKEN_CLOUDS;
		case "shower rain":
			return Weather.SHOWER_RAIN;
		case "rain":
			return Weather.RAIN;
		case "thunderstorm":
			return Weather.THUNDERSTORM;
		case "snow":
			return Weather.SNOW;
		case "mist":
			return Weather.MIST;
		}
		return null;
	}
	
	public static Weather getRandomWeather() {
		List<Weather> wt = new ArrayList<>();
		for (Weather t: Weather.values()) {
			wt.add(t);
		}
		
		Random rand = new Random();
		return wt.get(rand.nextInt(wt.size()));
	}
	
	public static boolean getRandomEventOccur() {
		Random rand = new Random();
		int res = rand.nextInt(9) + 1;
		if (res > 8) return true;
		else return false;
	}
}
