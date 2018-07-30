package ky.rtt.consumer.road;

import javax.realtime.RealtimeThread;
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
	
	public RoadConsumer(String name, ReleaseParameters rp, Direction direction, Traffic tf) {
		super(null, rp);
		super.setName(name);
		this.direction = direction;
		this.tf = tf;
		this.road = tf.getRoad(direction);
	}
	
	public void run() {
		while (true) {
			if (road.getLight() == Light.RED) {
				wait += 1000;				
			} else {
				wait = 0;
			}
			waitForNextPeriod();
		}
	}
	
	private void switchLight() {
		if (road.getLight() == Light.GREEN) {
			road.setLight(Light.RED);
		} else {
			road.setLight(Light.GREEN);
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
