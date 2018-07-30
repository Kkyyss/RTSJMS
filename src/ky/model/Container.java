package ky.model;

import javax.realtime.AsyncEvent;
import javax.realtime.PeriodicParameters;
import javax.realtime.RelativeTime;
import javax.realtime.ReleaseParameters;

import ky.rtt.consumer.road.RoadConsumer;
import ky.rtt.producer.car.CarProducer;
import ky.rtt.producer.car.CarProducerCensor;

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
		RoadConsumerContainer(com);
	}
	
	private void RoadConsumerContainer(Components com) {
		Traffic tf1 = com.getTf1();
		RelativeTime period = new RelativeTime(1000, 0);
		ReleaseParameters rp = new PeriodicParameters(period);
		
		// Consumers
		RoadConsumer rc1 = new RoadConsumer("T1-LEFT-RC", rp, Direction.LEFT, tf1);
		RoadConsumer rc2 = new RoadConsumer("T1-TOP-RC", rp, Direction.TOP, tf1);
		RoadConsumer rc3 = new RoadConsumer("T1-DOWN-RC", rp, Direction.DOWN, tf1);
		RoadConsumer rc4 = new RoadConsumer("T1-RIGHT-RC", rp, Direction.RIGHT, tf1);
		
		rc1.start();
		rc2.start();
		rc3.start();
		rc4.start();
	}
	
	private void CarProducerContainer(Components com) {
		Traffic tf1 = com.getTf1();
		RelativeTime start;
		RelativeTime period = new RelativeTime(1500, 0);
		ReleaseParameters rp = new PeriodicParameters(period);
		
		// Producers
		CarProducer cp1 = new CarProducer("T1-LEFT-CP", rp, com, tf1, tf1.getLeftRoad());
		CarProducer cp2 = new CarProducer("T1-TOP-CP", rp, com, tf1, tf1.getTopRoad());
		CarProducer cp3 = new CarProducer("T1-DOWN-CP", rp, com, tf1, tf1.getDownRoad());
		
		start = new RelativeTime(1500, 0);
	  period = new RelativeTime(1500, 0);
		rp = new PeriodicParameters(start, period);		
		// Censors
		CarProducerCensor cpc1 = new CarProducerCensor(rp, cp1);
		CarProducerCensor cpc2 = new CarProducerCensor(rp, cp2);
		CarProducerCensor cpc3 = new CarProducerCensor(rp, cp3);
		
		cp1.start();
		cp2.start();
		cp3.start();
		cpc1.start();
		cpc2.start();
		cpc3.start();
	}
}
