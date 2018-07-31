package ky.rtt.producer.person;

import javax.realtime.AsyncEventHandler;
import javax.realtime.PeriodicParameters;
import javax.realtime.RelativeTime;
import javax.realtime.ReleaseParameters;
import javax.realtime.Schedulable;

public class PersonProducerHandler {
	private Schedulable sc;
	private String name;
	private SlowDown slowdown = new SlowDown();
	private SpeedUp speedup = new SpeedUp();
	RelativeTime start;
	RelativeTime period;
	ReleaseParameters rp;
	
	public PersonProducerHandler(String name, Schedulable sc) {
		this.sc = sc;
		this.name = name;
	}
	
	public class SlowDown extends AsyncEventHandler {
		public void handleAsyncEvent() {
			System.out.println(name + " slow down...");
			period = new RelativeTime(5000, 0);
			rp = new PeriodicParameters(period);
			sc.setReleaseParameters(rp);
		}
	}
	
	public class SpeedUp extends AsyncEventHandler {
		public void handleAsyncEvent() {
			System.out.println(name + " speed up...");
			period = new RelativeTime(2000, 0);
			rp = new PeriodicParameters(period);
			sc.setReleaseParameters(rp);
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
