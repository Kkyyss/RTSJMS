package ky.rtt.producer.person;

import javax.realtime.AsyncEvent;
import javax.realtime.PeriodicParameters;
import javax.realtime.RealtimeThread;
import javax.realtime.RelativeTime;
import javax.realtime.ReleaseParameters;

import ky.model.Components;
import ky.model.Weather;

public class PersonProducerCensor extends RealtimeThread {
	private PersonProducer rtt;
	private AsyncEvent slowEvt = new AsyncEvent();
	private AsyncEvent speedEvt = new AsyncEvent();
	private boolean isSlow = false;
	private RelativeTime start, period;
	private ReleaseParameters rp;
	
	public PersonProducerCensor(PersonProducer rtt) {
		period = new RelativeTime(1000, 0);
		rp = new PeriodicParameters(period);
		setReleaseParameters(rp);
		this.rtt = rtt;
		PersonProducerHandler cch = new PersonProducerHandler(rtt.getName(), rtt);
		slowEvt.addHandler(cch.getSlowdown());
		speedEvt.addHandler(cch.getSpeedup());
	}
	
	public void run() {
		while (true) {
			Components com = rtt.getCom();
			
			if (!isSlow) {
				if (com.getWeather() == Weather.THUNDERSTORM || com.getWeather() == Weather.RAIN) {
					slowEvt.fire();
					isSlow = true;
				}
			}
			if (isSlow) {
				if (com.getWeather() != Weather.THUNDERSTORM && com.getWeather() != Weather.RAIN) {
					speedEvt.fire();
					isSlow = false;
				}
			}
			waitForNextPeriod();
		}
	}
}
