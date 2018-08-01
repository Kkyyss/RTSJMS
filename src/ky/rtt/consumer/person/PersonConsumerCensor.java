package ky.rtt.consumer.person;

import javax.realtime.AsyncEvent;
import javax.realtime.PeriodicParameters;
import javax.realtime.RealtimeThread;
import javax.realtime.RelativeTime;
import javax.realtime.ReleaseParameters;

import ky.Utils.MyUtils;
import ky.model.Car;
import ky.rtt.consumer.car.CarConsumer;
import ky.rtt.consumer.car.CarConsumerHandler;

public class PersonConsumerCensor extends RealtimeThread {
	private PersonConsumer rtt;
	private AsyncEvent slowEvt = new AsyncEvent();
	private AsyncEvent speedEvt = new AsyncEvent();
	private boolean isSlow = false;
	private RelativeTime start, period;
	private ReleaseParameters rp;
	private CarConsumerHandler cch;
	
	public PersonConsumerCensor(PersonConsumer rtt) {
		period = new RelativeTime(1000, 0);
		rp = new PeriodicParameters(period);
		setReleaseParameters(rp);
		this.rtt = rtt;
		CarConsumerHandler cch = new CarConsumerHandler(rtt.getPerson().getName(), rtt);
		this.cch = cch;
		slowEvt.addHandler(cch.getSlowdown());
		speedEvt.addHandler(cch.getSpeedup());
	}
	
	public void run() {
		while (true) {
			// TODO Weather buff to control speed of person
			waitForNextPeriod();
		}
	}
}
