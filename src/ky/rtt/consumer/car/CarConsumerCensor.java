package ky.rtt.consumer.car;

import javax.realtime.AsyncEvent;
import javax.realtime.PeriodicParameters;
import javax.realtime.RealtimeThread;
import javax.realtime.RelativeTime;
import javax.realtime.ReleaseParameters;

import ky.model.Car;
import ky.model.Road;

public class CarConsumerCensor extends RealtimeThread {
	private CarConsumer rtt;
	private AsyncEvent slowEvt = new AsyncEvent();
	private AsyncEvent speedEvt = new AsyncEvent();
	private boolean isSlow = false;
	private RelativeTime start, period;
	private ReleaseParameters rp;
	
	public CarConsumerCensor(CarConsumer rtt) {
		period = new RelativeTime(1000, 0);
		rp = new PeriodicParameters(period);
		setReleaseParameters(rp);
		this.rtt = rtt;
		CarConsumerHandler cch = new CarConsumerHandler(rtt.getCar().getName(), rtt);
		slowEvt.addHandler(cch.getSlowdown());
		speedEvt.addHandler(cch.getSpeedup());
	}
	
	public void run() {
		System.out.println("Car censor started...");
		while(true) {
			Car car = rtt.getCar();
			Road road = car.getRoad();
			
			// Accident
			if (!isSlow) {
				if (road.isAccident() || road.isFlooded()) {
					slowEvt.fire();
					isSlow = true;
				}
			}
			if (isSlow) {
				if (!road.isAccident() && !road.isFlooded()) {
					speedEvt.fire();
					isSlow = false;
				}
			}
			
			waitForNextPeriod();
		}
	}
}
