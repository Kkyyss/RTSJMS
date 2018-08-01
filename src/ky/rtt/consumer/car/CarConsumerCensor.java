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
	private CarConsumerHandler cch;
	
	public CarConsumerCensor(CarConsumer rtt) {
		period = new RelativeTime(1000, 0);
		rp = new PeriodicParameters(period);
		setReleaseParameters(rp);
		this.rtt = rtt;
		CarConsumerHandler cch = new CarConsumerHandler(rtt.getCar().getName(), rtt);
		this.cch = cch;
		slowEvt.addHandler(cch.getSlowdown());
		speedEvt.addHandler(cch.getSpeedup());
	}
	
	public void run() {
		System.out.println(rtt.getName() + " censor started...");
		while(!rtt.isLeave()) {
			Car car = rtt.getCar();
			Road road = car.getRoad();
			int acc = road.getAccident()[car.getForwarding()].get();

			// Accident
			if (!isSlow) {
				if (rtt.nearCondo()) {
					System.out.println("Near Condo");
					cch.getSlowdown().setSpeed(4000);
					slowEvt.fire();
					isSlow = true;
				}
				
				if (acc > 0 || road.isFlooded()) {
					System.out.println("Accident / Flooded");
					cch.getSlowdown().setSpeed(5000);
					slowEvt.fire();
					isSlow = true;
				}
			}
			if (isSlow) {
				if (!rtt.nearCondo() && acc <= 0 && !road.isFlooded()) {
					cch.getSpeedup().setSpeed(1000);
					speedEvt.fire();
					isSlow = false;
				}
			}
			
			waitForNextPeriod();
		}
		System.out.println(rtt.getName() + " censor ended...");
	}
}
