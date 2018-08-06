package ky.rtt.producer.person;

import javax.realtime.AsyncEventHandler;
import javax.realtime.PeriodicParameters;
import javax.realtime.RelativeTime;
import javax.realtime.ReleaseParameters;

import ky.Utils.MyUtils;

public class PersonProducerHandler {
	private PersonProducer rtt;
	private String name;
	private SlowDown slowdown = new SlowDown();
	private SpeedUp speedup = new SpeedUp();
	RelativeTime start;
	RelativeTime period;
	ReleaseParameters rp;
	
	public PersonProducerHandler(String name, PersonProducer rtt) {
		this.rtt = rtt;
		this.name = name;
	}
	
	public class SlowDown extends AsyncEventHandler {
		public void handleAsyncEvent() {
			MyUtils.log(rtt.getTf().getIndex(), name + " slow down...");
			period = new RelativeTime(5000, 0);
			rp = new PeriodicParameters(period);
			rtt.setReleaseParameters(rp);
		}
	}
	
	public class SpeedUp extends AsyncEventHandler {
		public void handleAsyncEvent() {
			MyUtils.log(rtt.getTf().getIndex(), name + " speed up...");
			period = new RelativeTime(2000, 0);
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
