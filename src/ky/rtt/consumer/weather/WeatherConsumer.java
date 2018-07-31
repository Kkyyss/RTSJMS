package ky.rtt.consumer.weather;

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
		System.out.println(super.getName() + " started...");
		while (true) {
			com.setWeather(MyUtils.getCurrentWeather("Bukit Jalil", "a3c2995dcf357dbba9c7d78282aa1598"));
			if (com.getWeather() == null) {
				System.out.println("Getting random weather instead of WEATHER API");
				com.setWeather(MyUtils.getRandomWeather());
			}
			System.out.println("WEATHER: " + com.getWeather());
			waitForNextPeriod();
		}
	}
}
