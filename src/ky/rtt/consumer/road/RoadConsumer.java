package ky.rtt.consumer.road;

import javax.realtime.PeriodicParameters;
import javax.realtime.RealtimeThread;
import javax.realtime.RelativeTime;
import javax.realtime.ReleaseParameters;

import ky.model.Direction;
import ky.model.Light;
import ky.model.Road;
import ky.model.Traffic;

public class RoadConsumer extends RealtimeThread {
	private Road road;
	private Direction direction;
	private Traffic tf;
	private int wait = 0;
	private boolean isGreen = false;
	private RelativeTime start, period;
	
	public RoadConsumer(String name, ReleaseParameters rp, Direction direction, Traffic tf) {
		super(null, rp);
		super.setName(name);
		this.direction = direction;
		this.tf = tf;
		this.road = tf.getRoad(direction);
	}
	
	public void run() {
		while (true) {
			if (road.getLight() == Light.GREEN) {
				isGreen = false;
				road.setLight(Light.RED);
				System.out.println(road.getName() + "'s traffic light into RED");
				period = new RelativeTime(18000, 0);
				setReleaseParameters(new PeriodicParameters(period));
			} else {
				isGreen = true;
				road.setLight(Light.GREEN);
				System.out.println(road.getName() + "'s traffic light into GREEN");
				period = new RelativeTime(6000, 0);
				setReleaseParameters(new PeriodicParameters(period));		
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
