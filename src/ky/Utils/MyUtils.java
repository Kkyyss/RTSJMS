package ky.Utils;

import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

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
		case "clear":
		 return Weather.CLEAR;
		case "clouds":
			return Weather.CLOUDS;
		case "atmosphere":
		 return Weather.ATMOSPHERE;
		case "drizzle":
			return Weather.DRIZZLE;
		case "rain":
			return Weather.RAIN;
		case "thunderstorm":
			return Weather.THUNDERSTORM;
		case "snow":
			return Weather.SNOW;
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
		int res = rand.nextInt(50) + 1;
		if (res > 35) return true;
		else return false;
	}
	
	public static void initialArrayOfAtomicIntegerVal(AtomicInteger []arr) {
		for (int i = 0; i < arr.length; i++) {
			arr[i] = new AtomicInteger(0);
		}
	}
	public static void initialArrayOfAtomicBooleanVal(AtomicBoolean []arr) {
		for (int i = 0; i < arr.length; i++) {
			arr[i] = new AtomicBoolean(false);
		}
	}
	public static void titleLog(String title) {
		// Center the title
		
		log(String.format("%120s", "").replaceAll(" ", "=") + "\n"
				+ String.format("%45s%-25s", "", title) + "\n"
				+ String.format("%120s", "").replaceAll(" ", "="));
	}
	public static void log(String any) {
		System.out.println(any);
	}
	public static void log(int trafficNo, String any) {
		int spacing = trafficNo * 30;
		// Right Pad + Left Pad
		String str = "%" + ((spacing - 30 == 0) ? "" : spacing - 30) + "s%-" + 25 + "s";
		str = String.format(str, "", any);
		
		System.out.println(str);
	}
}
