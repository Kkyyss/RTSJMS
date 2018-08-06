package ky.rtt.consumer.person;

import javax.realtime.PeriodicParameters;
import javax.realtime.RealtimeThread;
import javax.realtime.RelativeTime;
import javax.realtime.ReleaseParameters;

import ky.Utils.MyUtils;

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
		MyUtils.log(rtt.getTf().getIndex(),rtt.getPerson().getName() + " standing up...");
		if (rtt.isIn()) {
			rtt.getPerson().getRoad().getPedestrian().getFirstHalfFallDown().decrementAndGet();	
		} else {
			rtt.getPerson().getRoad().getPedestrian().getSecondHalfFallDown().decrementAndGet();	
		}
		
		period = new RelativeTime(1000, 0);
		rp = new PeriodicParameters(period);
		rtt.setReleaseParameters(rp);
	}
}
