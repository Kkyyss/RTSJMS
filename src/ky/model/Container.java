package ky.model;

import javax.realtime.PeriodicParameters;
import javax.realtime.RelativeTime;
import javax.realtime.ReleaseParameters;

import ky.rtt.consumer.road.RoadConsumer;
import ky.rtt.consumer.weather.WeatherConsumer;
import ky.rtt.producer.car.CarProducer;
import ky.rtt.producer.car.CarProducerCensor;
import ky.rtt.producer.person.PersonProducer;
import ky.rtt.producer.person.PersonProducerCensor;

public class Container {
	public Container() {
		// Road for T1
		Road leftRoad = new Road("T1-LEFT-R", Direction.LEFT, 3);		// T1 LEFT
		Road topRoad = new Road("T1-TOP-R", Direction.TOP, 4);				// T1 TOP
		Road downRoad = new Road("T1-DOWN-R", Direction.DOWN, 4);	// T1 DOWN
		Road rightRoad = new Road("T1-RIGHT-R", Direction.RIGHT, 5);	// T1 RIGHT

		Condominium condo = new Condominium("T1-RIGHT-CONDO", 4, 4);
		Pedestrian pedestrian = new Pedestrian("T1-TOP-P", 2);
		topRoad.setPedestrian(pedestrian);
		pedestrian = new Pedestrian("T1-RIGHT-P", 2);
		rightRoad.setPedestrian(pedestrian);
		rightRoad.setCondominium(condo);
		
		Traffic tf1 = new Traffic(
				"LEFT-T1",
				Position.LEFT,
				leftRoad, topRoad, downRoad, rightRoad);
		Components com = new Components("Bukit Jalil", tf1);
		WeatherContainer(com);
		CarProducerContainer(com);
		PersonProducerContainer(com);
		RoadConsumerContainer(com);
	}
	
	private void PersonProducerContainer(Components com) {
		Traffic tf1 = com.getTf1();
		Road topRoad = tf1.getTopRoad();
		Road leftRoad = tf1.getLeftRoad();
		Road rightRoad = tf1.getRightRoad();
		Road downRoad = tf1.getDownRoad();
		PersonProducer pp1, pp2, pp3, pp4;
		PersonProducerCensor pps1, pps2, pps3, pps4;
		
		RelativeTime start = new RelativeTime(1000, 0);
		RelativeTime period = new RelativeTime(7000, 0);
		ReleaseParameters rp = new PeriodicParameters(start, period);
		
		// in
		pp1 = new PersonProducer("PP-TOP-IN", rp, com, topRoad, true);
		pp2 = new PersonProducer("PP-RIGHT-IN", rp, com, rightRoad, true);
		
		// out
		pp3 = new PersonProducer("PP-TOP-OUT", rp, com, topRoad, false);
		pp4 = new PersonProducer("PP-RIGHT-OUT", rp, com, rightRoad, false);
		
		pps1 = new PersonProducerCensor(pp1);
		pps2 = new PersonProducerCensor(pp2);
		pps3 = new PersonProducerCensor(pp3);
		pps4 = new PersonProducerCensor(pp4);
		
		pp1.start();
		pp2.start();
		pp3.start();
		pp4.start();
		
		pps1.start();
		pps2.start();
		pps3.start();
		pps4.start();
	}
	
	private void WeatherContainer(Components com) {
		RelativeTime period = new RelativeTime(5000, 0);
		ReleaseParameters rp = new PeriodicParameters(period);
		
		WeatherConsumer wc = new WeatherConsumer("WC", rp, com);
		wc.start();
	}
	
	private void RoadConsumerContainer(Components com) {
		Traffic tf1 = com.getTf1();
		RelativeTime start;
		RelativeTime period = new RelativeTime(6000, 0);
		ReleaseParameters rp = new PeriodicParameters(period);
		// Consumers
		RoadConsumer rc1 = new RoadConsumer("T1-LEFT-RC", rp, Direction.LEFT, tf1);
		start = new RelativeTime(6000, 0);
		period = new RelativeTime(6000, 0);
		rp = new PeriodicParameters(start, period);
		RoadConsumer rc2 = new RoadConsumer("T1-TOP-RC", rp, Direction.TOP, tf1);
		start = new RelativeTime(12000, 0);
		period = new RelativeTime(6000, 0);
		rp = new PeriodicParameters(start, period);
		RoadConsumer rc3 = new RoadConsumer("T1-RIGHT-RC", rp, Direction.RIGHT, tf1);
		start = new RelativeTime(18000, 0);
		period = new RelativeTime(6000, 0);
		rp = new PeriodicParameters(start, period);		
		RoadConsumer rc4 = new RoadConsumer("T1-DOWN-RC", rp, Direction.DOWN, tf1);
		
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
		start = new RelativeTime(1500, 0);
	  period = new RelativeTime(1500, 0);
		rp = new PeriodicParameters(start, period);		
		CarProducer cp1 = new CarProducer("T1-LEFT-CP", rp, com, tf1, tf1.getLeftRoad());
		start = new RelativeTime(3000, 0);
	  period = new RelativeTime(3000, 0);
		rp = new PeriodicParameters(start, period);		
		CarProducer cp2 = new CarProducer("T1-TOP-CP", rp, com, tf1, tf1.getTopRoad());
		start = new RelativeTime(4500, 0);
	  period = new RelativeTime(3000, 0);
		rp = new PeriodicParameters(start, period);	
		CarProducer cp3 = new CarProducer("T1-DOWN-CP", rp, com, tf1, tf1.getDownRoad());
		
		start = new RelativeTime(1500, 0);
	  period = new RelativeTime(1500, 0);
		rp = new PeriodicParameters(start, period);		
		// Censors
		CarProducerCensor cpc1 = new CarProducerCensor(rp, cp1);
		CarProducerCensor cpc2 = new CarProducerCensor(rp, cp2);
		CarProducerCensor cpc3 = new CarProducerCensor(rp, cp3);
		
		cp1.start();
//		cp2.start();
//		cp3.start();
		cpc1.start();
//		cpc2.start();
//		cpc3.start();
	}
}
