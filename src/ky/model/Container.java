package ky.model;

import javax.realtime.AsyncEvent;
import javax.realtime.PeriodicParameters;
import javax.realtime.RelativeTime;
import javax.realtime.ReleaseParameters;

import ky.rtt.producer.car.CarProducer;
import ky.rtt.producer.car.CarProducerCensor;
import ky.rtt.traffic.TrafficHandler;
import ky.rtt.traffic.TrafficRTT;

public class Container {
	public Container() {
		ReleaseParameters rp;
		RelativeTime start, period;
		AsyncEvent asyncEvt = new AsyncEvent();
		// Road for T1
		Road leftRoad = new Road("T1-LEFT-R", Direction.LEFT);		// T1 LEFT
		Road topRoad = new Road("T1-TOP-R", Direction.TOP);				// T1 TOP
		Road downRoad = new Road("T1-DOWN-R", Direction.DOWN);	// T1 DOWN
		Road rightRoad = new Road("T1-RIGHT-R", Direction.RIGHT);	// T1 RIGHT
		
		Traffic tf1 = new Traffic(
				"LEFT-T1",
				Position.LEFT,
				leftRoad, topRoad, downRoad, rightRoad);
		Components com = new Components("Bukit Jalil", tf1);
		// Car Producer
		CarProducerContainer(com);
		
		// T1
		period = new RelativeTime(1000, 0);
		rp = new PeriodicParameters(period);
		TrafficRTT tfRtt1 = new TrafficRTT(rp, tf1);
		TrafficHandler tfHdlr = new TrafficHandler(tfRtt1);
		asyncEvt.setHandler(tfHdlr);
		tfRtt1.setAsyncEvt(asyncEvt);
		tfRtt1.start();		
	}
	
	private void CarProducerContainer(Components com) {
		Traffic tf1 = com.getTf1();
		RelativeTime period = new RelativeTime(3000, 0);
		ReleaseParameters rp = new PeriodicParameters(period);
		
		// Producers
		CarProducer cp1 = new CarProducer("T1-LEFT-CP", rp, com, tf1, tf1.getLeftRoad());
		CarProducer cp2 = new CarProducer("T1-TOP-CP", rp, com, tf1, tf1.getTopRoad());
		CarProducer cp3 = new CarProducer("T1-DOWN-CP", rp, com, tf1, tf1.getDownRoad());
		
		// Censors
		CarProducerCensor cpc1 = new CarProducerCensor(cp1, rp);
		CarProducerCensor cpc2 = new CarProducerCensor(cp2, rp);
		CarProducerCensor cpc3 = new CarProducerCensor(cp3, rp);
		
		cp1.start();
		cp2.start();
		cp3.start();
		cpc1.start();
		cpc2.start();
		cpc3.start();
	}
}
