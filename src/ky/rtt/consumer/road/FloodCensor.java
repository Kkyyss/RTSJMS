package ky.rtt.consumer.road;

import javax.realtime.RealtimeThread;
import javax.realtime.ReleaseParameters;

public class FloodCensor extends RealtimeThread	 {
	private RoadConsumer rc;
	
	public FloodCensor(ReleaseParameters rp, RoadConsumer rc) {
		super(null, rp);
		this.rc = rc;
	}
	
	public void run() {
		rc.getRoad().getFloodArea().set(false);
	}
}
