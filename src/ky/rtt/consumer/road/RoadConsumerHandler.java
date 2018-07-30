package ky.rtt.consumer.road;

import javax.realtime.AsyncEventHandler;
import javax.realtime.PeriodicParameters;
import javax.realtime.RelativeTime;
import javax.realtime.ReleaseParameters;
import javax.realtime.Schedulable;

public class RoadConsumerHandler {
	private Schedulable sc;
	private SlowDown sd = new SlowDown();
	private SpeedUp su = new SpeedUp();
	
	public Schedulable getSc() {
		return sc;
	}
	public void setSc(Schedulable sc) {
		this.sc = sc;
	}
	public SlowDown getSd() {
		return sd;
	}
	public void setSd(SlowDown sd) {
		this.sd = sd;
	}
	public SpeedUp getSu() {
		return su;
	}
	public void setSu(SpeedUp su) {
		this.su = su;
	}
	public RoadConsumerHandler(Schedulable sc) {
		this.sc = sc;
	}
	
	public class SlowDown extends AsyncEventHandler {
		public void handleAsyncEvent() {
			RelativeTime period = new RelativeTime(10000, 0);
			ReleaseParameters rp = new PeriodicParameters(period);
			sc.setReleaseParameters(rp);
		}
	}
	public class SpeedUp extends AsyncEventHandler {
		public void handleAsyncEvent() {
			RelativeTime period = new RelativeTime(2000, 0);
			ReleaseParameters rp = new PeriodicParameters(period);
			sc.setReleaseParameters(rp);
		}
	}
}
