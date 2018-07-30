package ky.rtt.producer.car;

import javax.realtime.AsyncEventHandler;

public class CarProducerHandler {
	public CarProducer rtt;
	public Stop stop = new Stop();
	public Start start = new Start();

	public CarProducer getRtt() {
		return rtt;
	}
	public void setRtt(CarProducer rtt) {
		this.rtt = rtt;
	}

	public CarProducerHandler(CarProducer rtt) {
		this.rtt = rtt;
	};

	public class Stop extends AsyncEventHandler {
		public void handleAsyncEvent() {
			System.out.println(rtt.getName() + " nearly fulled...");
			rtt.deschedulePeriodic();
		}
	}
	
	public class Start extends AsyncEventHandler {
		public void handleAsyncEvent() {
			System.out.println(rtt.getName() + " nearly empty...");
			rtt.schedulePeriodic();
		}
	}

	public Stop getStop() {
		return stop;
	}

	public void setStop(Stop stop) {
		this.stop = stop;
	}

	public Start getStart() {
		return start;
	}

	public void setStart(Start start) {
		this.start = start;
	}
}

