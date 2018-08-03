package ky.rtt.consumer.road;

import javax.realtime.PeriodicParameters;
import javax.realtime.RealtimeThread;
import javax.realtime.RelativeTime;
import javax.realtime.ReleaseParameters;

import ky.Utils.MyUtils;
import ky.model.Components;
import ky.model.Direction;
import ky.model.Light;
import ky.model.Road;
import ky.model.Traffic;
import ky.model.Weather;

public class RoadConsumer extends RealtimeThread {
	private Road road;
	private Direction direction;
	private Traffic tf;
	private Components com;
	private int wait = 0;
	private boolean green = false;
	private RelativeTime start, period;
	
	public Components getCom() {
		return com;
	}

	public Road getRoad() {
		return road;
	}

	public void setRoad(Road road) {
		this.road = road;
	}

	public void setCom(Components com) {
		this.com = com;
	}
	public RoadConsumer(String name, ReleaseParameters rp, 
			Road road, Traffic tf, Components com) {
		super(null, rp);
		super.setName(name);
		this.tf = tf;
		this.road = road;
		this.com = com;
	}
	
	public void run() {
		while (true) {
			if (!road.getFloodArea().get()) {
				if (com.getWeather() == Weather.RAIN 
						|| com.getWeather() == Weather.THUNDERSTORM) {
					if (MyUtils.getRandomEventOccur()) {
						road.getFloodArea().set(true);
						start = new RelativeTime(6000, 0);
						period = new RelativeTime(1000, 0);
						FloodCensor fc = new FloodCensor(new PeriodicParameters(start, period), this);
						fc.start();
					}
				}
			}
			waitForNextPeriod();
		}
	}

	public Traffic getTf() {
		return tf;
	}

	public void setTf(Traffic tf) {
		this.tf = tf;
	}

	public int getWait() {
		return wait;
	}

	public void setWait(int wait) {
		this.wait = wait;
	}
	
}
