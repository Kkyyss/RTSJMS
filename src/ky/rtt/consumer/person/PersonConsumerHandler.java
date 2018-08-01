package ky.rtt.consumer.person;

import javax.realtime.AsyncEventHandler;
import javax.realtime.PeriodicParameters;
import javax.realtime.RelativeTime;
import javax.realtime.ReleaseParameters;
import javax.realtime.Schedulable;

import ky.rtt.consumer.car.CarConsumerHandler.SlowDown;
import ky.rtt.consumer.car.CarConsumerHandler.SpeedUp;

public class PersonConsumerHandler {
	private Schedulable rtt;
	private String name;
	private SlowDown slowdown = new SlowDown();
	private SpeedUp speedup = new SpeedUp();
	RelativeTime start;
	RelativeTime period;
	ReleaseParameters rp;
	
	public PersonConsumerHandler(String name, Schedulable rtt) {
		this.name = name;
		this.rtt = rtt;
	}
	
	public class SlowDown extends AsyncEventHandler {
		private int speed = 3000;
		public SlowDown() {};
		public SlowDown(int speed) {
			this.speed = speed;
		}
		
		public void handleAsyncEvent() {
			System.out.println(name + " slow down " + speed);
			period = new RelativeTime(speed, 0);
			rp = new PeriodicParameters(period);
			rtt.setReleaseParameters(rp);
		}
		public int getSpeed() {
			return speed;
		}
		public void setSpeed(int speed) {
			this.speed = speed;
		}
	}
	public class SpeedUp extends AsyncEventHandler {
		private int speed = 1000;
		public SpeedUp() {};
		public SpeedUp(int speed) {
			this.speed = speed;
		}
		public void handleAsyncEvent() {
			System.out.println(name + " speed up " + speed);
			period = new RelativeTime(speed, 0);
			rp = new PeriodicParameters(period);
			rtt.setReleaseParameters(rp);
		}
		public int getSpeed() {
			return speed;
		}
		public void setSpeed(int speed) {
			this.speed = speed;
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
