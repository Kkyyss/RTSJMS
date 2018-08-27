package ky.rtt.consumer.weather;

import java.util.Random;

import javax.realtime.RealtimeThread;
import javax.realtime.ReleaseParameters;

import ky.Utils.MyUtils;
import ky.model.Components;
import ky.model.Weather;

public class WeatherConsumer extends RealtimeThread {
	private Components com;

	public WeatherConsumer(String name, ReleaseParameters rp, Components com) {
		super(null, rp);
		super.setName(name);
		this.com = com;
	}
	
	public void run() {
		MyUtils.titleLog(super.getName() + " -> [openweathermap API] OR [RANDOM WC] [START]");
		while (true) {
			com.setWeather(MyUtils.getCurrentWeather("Bukit Jalil", "a3c2995dcf357dbba9c7d78282aa1598"));
			if (com.getWeather() == null) {
				MyUtils.titleLog("Getting random weather instead of WEATHER API");
				Random rand = new Random();
				com.setWeather(Weather.values()[rand.nextInt(Weather.values().length)]);
				// Test flood event
				// com.setWeather(Weather.values()[3]);
				MyUtils.titleLog("[RANDOM] " + com.getWeather().toString());
			} else {
				MyUtils.titleLog("[openweathermap API] " + com.getWeather().toString());
			}
			waitForNextPeriod();
		}
	}
}
