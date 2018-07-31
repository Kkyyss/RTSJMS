package ky.rtt.consumer.car;

import javax.realtime.AsyncEventHandler;
import javax.realtime.PeriodicParameters;
import javax.realtime.RelativeTime;
import javax.realtime.ReleaseParameters;
import javax.realtime.Schedulable;

public class CarConsumerHandler {
	private Schedulable rtt;
	private String name;
	private SlowDown slowdown = new SlowDown();
	private SpeedUp speedup = new SpeedUp();
	RelativeTime start;
	RelativeTime period;
	ReleaseParameters rp;
	
	public CarConsumerHandler(String name, Schedulable rtt) {
		this.rtt = rtt;
		this.name = name;
	}
	
	public class SlowDown extends AsyncEventHandler {
		public void handleAsyncEvent() {
			System.out.println(name + " slow down...");
			period = new RelativeTime(2000, 0);
			rp = new PeriodicParameters(period);
			rtt.setReleaseParameters(rp);
		}
	}
	public class SpeedUp extends AsyncEventHandler {
		public void handleAsyncEvent() {
			System.out.println(name + " speed up...");
			period = new RelativeTime(1000, 0);
			rp = new PeriodicParameters(period);
			rtt.setReleaseParameters(rp);
		}			
	}
	public SlowDown getSlowdown() {
		return slowdown;
	}
	public void setSlowdown(SlowDown slowdown) {
		this.slowdown = slowdown;
	}
	public SpeedUp getSpeedup() {
		return speedup;
	}
	public void setSpeedup(SpeedUp speedup) {
		this.speedup = speedup;
	}
}
