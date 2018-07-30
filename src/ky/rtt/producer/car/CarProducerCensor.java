package ky.rtt.producer.car;

import javax.realtime.AsyncEvent;
import javax.realtime.RealtimeThread;
import javax.realtime.ReleaseParameters;

public class CarProducerCensor extends RealtimeThread {
	private CarProducer cp;
	private AsyncEvent stopEvt = new AsyncEvent();
	private AsyncEvent startEvt = new AsyncEvent();
	
	public CarProducerCensor(CarProducer cp, ReleaseParameters rp) {
		super(null, rp);
		this.cp = cp;
		CarProducerHandler cph = new CarProducerHandler(cp);
		stopEvt.addHandler(cph.getStop());
		startEvt.addHandler(cph.getStart());
	}
	
	public void run() {
		while (true) {
			if (cp.isMax()) {
				stopEvt.fire();
			} 
			if (cp.isMin()) {
				startEvt.fire();
			}
			waitForNextPeriod();
		}
	}
}
