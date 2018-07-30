package ky.rtt.consumer.road;

import javax.realtime.AsyncEvent;
import javax.realtime.RealtimeThread;
import javax.realtime.ReleaseParameters;

public class RoadConsumerCensor extends RealtimeThread {
	private RoadConsumer rtt;
	private AsyncEvent slowDownEvent = new AsyncEvent();
	private AsyncEvent speedUpEvent = new AsyncEvent();
	
	public RoadConsumerCensor(String name, ReleaseParameters rp, RoadConsumer rtt) {
		super(null, rp);
		super.setName(name);
		this.rtt = rtt;
		RoadConsumerHandler rch = new RoadConsumerHandler(this);
		slowDownEvent.addHandler(rch.getSd());
		speedUpEvent.addHandler(rch.getSu());
	}
	
	public void run() {
		while (true) {
			
		}
	}
}
