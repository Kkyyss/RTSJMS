package ky.rtt.producer.car;

import javax.realtime.AsyncEvent;
import javax.realtime.RealtimeThread;
import javax.realtime.ReleaseParameters;

public class CarProducerCensor extends RealtimeThread {
	private AsyncEvent stopEvt = new AsyncEvent();
	private AsyncEvent startEvt = new AsyncEvent();
	private CarProducer rtt;
	private boolean isPause = false;
	
	public CarProducerCensor(ReleaseParameters rp, CarProducer rtt) {
		super(null, rp);
		this.rtt = rtt;
		CarProducerHandler cph = new CarProducerHandler(rtt);
		stopEvt.addHandler(cph.getStop());
		startEvt.addHandler(cph.getStart());
	}

	public void run() {
		while (true) {
			if (!isPause) {
				if (rtt.isMax()) {
					stopEvt.fire();
					isPause = true;
				}
			}
			
			if (isPause) {
				if (rtt.isMin()) {
					startEvt.fire();
					isPause = false;
				}
			}
			waitForNextPeriod();
		}
	}
}
