package ky.rtt.consumer.road;

import javax.realtime.AsyncEvent;
import javax.realtime.AsynchronouslyInterruptedException;
import javax.realtime.Interruptible;
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

public class RoadConsumer extends RealtimeThread implements Interruptible {
	private Road road;
	private Direction direction;
	private Traffic tf;
	private Components com;
	private int idx;
	private boolean green = false;
	private RelativeTime start, period;
	private AsynchronouslyInterruptedException aie;

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
			Road road, Traffic tf, Components com, AsynchronouslyInterruptedException aie) {
		super(null, rp);
		super.setName(name);
		this.tf = tf;
		this.road = road;
		this.com = com;
		this.aie = aie;
//		this.idx = idx;
		this.start();
	}

	public Traffic getTf() {
		return tf;
	}

	public void setTf(Traffic tf) {
		this.tf = tf;
	}
	
	public void run() {
		aie.doInterruptible(this);
	}
	
	@Override
	public void interruptAction(AsynchronouslyInterruptedException exception) {
		System.out.println("YES");
//		Light prevLight = road.getLight();
//		
//		if (road.isAccidentArea()) {
//			road.setLight(Light.GREEN);
//			MyUtils.log(tf.getIndex(), road.getName() + "'s traffic light turned into GREEN");
//		} else {
//			road.setLight(Light.RED);
//			if (prevLight != Light.GREEN) {
//				MyUtils.log(tf.getIndex(), road.getName() + "'s traffic light turned into RED");
//			}			
//		}
//		while (com.isAccidentOccured()) {
//			try {
//				RealtimeThread.sleep(new RelativeTime(500, 0));
//			} catch (InterruptedException e) {}
//		}
//		road.setLight(prevLight);
		MyUtils.log(tf.getIndex(), "Stop traffic light schedule!");
		this.deschedulePeriodic();
	}

	@Override
	public void run(AsynchronouslyInterruptedException aie) throws AsynchronouslyInterruptedException {
		while (true) {
//			if (!road.getFloodArea().get()) {
//				if (com.getWeather() == Weather.RAIN 
//						|| com.getWeather() == Weather.THUNDERSTORM) {
//					if (MyUtils.getRandomEventOccur()) {
//						road.getFloodArea().set(true);
//						start = new RelativeTime(6000, 0);
//						period = new RelativeTime(1000, 0);
//						FloodCensor fc = new FloodCensor(new PeriodicParameters(start, period), this);
//						fc.start();
//					}
//				}
//			}
			if (road.getLight() == Light.GREEN) {
				road.setLight(Light.RED);
				MyUtils.log(tf.getIndex(), road.getName() + "'s traffic light turned into RED");
				period = new RelativeTime(18000, 0);
				this.setReleaseParameters(new PeriodicParameters(period));
			} else {
				road.setLight(Light.GREEN);
				MyUtils.log(tf.getIndex(), road.getName() + "'s traffic light turned into GREEN");
				period = new RelativeTime(6000, 0);
				this.setReleaseParameters(new PeriodicParameters(period));				
			}
			waitForNextPeriod();
		}
	}
	
	
}
