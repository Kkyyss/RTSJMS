package ky.rtt.consumer.person;

import javax.realtime.PeriodicParameters;
import javax.realtime.RealtimeThread;
import javax.realtime.RelativeTime;
import javax.realtime.ReleaseParameters;

public class FallDownCensor extends RealtimeThread {
	private PersonConsumer rtt;
	private RelativeTime start, period;
	private ReleaseParameters rp;
	
	public FallDownCensor(ReleaseParameters rp, PersonConsumer rtt) {
		super(null, rp);
		this.rtt = rtt;
	}
	
	public void run() {
		rtt.setFalldown(false);
		System.out.println("Fall down buff gone...");
		period = new RelativeTime(1000, 0);
		rp = new PeriodicParameters(period);
		rtt.setReleaseParameters(rp);
	}
}