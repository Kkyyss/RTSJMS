package ky.rtt.consumer.car;

import javax.realtime.AsyncEvent;
import javax.realtime.PeriodicParameters;
import javax.realtime.RealtimeThread;
import javax.realtime.RelativeTime;
import javax.realtime.ReleaseParameters;

import ky.Utils.MyUtils;
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
		start = new RelativeTime(1000, 0);
		period = new RelativeTime(1000, 0);
		rp = new PeriodicParameters(start, period);
		setReleaseParameters(rp);
		this.rtt = rtt;
		CarConsumerHandler cch = new CarConsumerHandler(rtt.getCar().getName(), rtt);
		this.cch = cch;
		slowEvt.addHandler(cch.getSlowdown());
		speedEvt.addHandler(cch.getSpeedup());
	}
	
	public CarConsumer getRtt() {
		return rtt;
	}

	public void setRtt(CarConsumer rtt) {
		this.rtt = rtt;
	}

	public void run() {
		//MyUtils.log(rtt.getTf().getIndex(), rtt.getName() + " consumer censor started...");
		while(!rtt.getCar().isLeave()) {
			Car car = rtt.getCar();
			Road road = car.getRoad();
			boolean acc = false;
			
			int speed = 0;
			// Buffs to slow down car
			if (!isSlow) {
				if (rtt.nearSchool()) {
					MyUtils.log(rtt.getTf().getIndex(), car.getName() + " [ENTERED] S");
					speed += 1000;
				}
				if (rtt.nearCondo()) {
					MyUtils.log(rtt.getTf().getIndex(), car.getName() + " [ENTERED] CD");
					speed += 1000;
				}
				if (speed > 0) {
					cch.getSlowdown().setSpeed(speed);
					slowEvt.fire();
					isSlow = true;
				}
			}
			// Buffs to speed up car
			if (isSlow) {
				if (!rtt.nearCondo() 
						&& !rtt.nearSchool() 
						&& !acc) {
					cch.getSpeedup().setSpeed(1000);
					speedEvt.fire();
					isSlow = false;
				}
			}
			
			waitForNextPeriod();
		}
	}
}
