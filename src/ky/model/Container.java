package ky.model;

import javax.realtime.PeriodicParameters;
import javax.realtime.RelativeTime;
import javax.realtime.ReleaseParameters;

import ky.rtt.consumer.traffic.AccidentOrIncidentContainer;
import ky.rtt.consumer.traffic.TrafficContainer;
import ky.rtt.consumer.weather.WeatherConsumer;
import ky.rtt.producer.car.CarProducerContainer;
import ky.rtt.producer.person.PersonProducer;
import ky.rtt.producer.person.PersonProducerCensor;

public class Container {
	public Container() {
		Components com = new Components("Bukit Jalil");
		// Initial components
		buildBluePrint(com, 4);
//		buildBluePrint(com, 1);
		
		// Scenario
		Traffic tf1 = com.getTfs().get(0);
		Traffic tf2 = com.getTfs().get(1);
		Traffic tf3 = com.getTfs().get(2);
		Traffic tf4 = com.getTfs().get(3);
		
		// TF1 scenario
		// School - RIGHT
		School school = new School(tf1.getName() + "_S_[R]", 4, 4);
		tf1.getRightRoad().setSchool(school);
		// Pedestrian - TOP
		Pedestrian pedestrian = new Pedestrian(tf1.getName() + "_P_[T]", 2);
		tf1.getTopRoad().setPedestrian(pedestrian);
		// Pedestrian - RIGHT
		pedestrian = new Pedestrian(tf1.getName() + "_P_[R]", 2);
		tf1.getRightRoad().setPedestrian(pedestrian);
		
		// TF1 <---> TF2
		tf2.getLeftRoad().setPedestrian(pedestrian);
		tf2.getLeftRoad().setSchool(school);
		tf1.getRightRoad().setLinkedTrafficIdx(tf2.getIndex());
		tf2.getLeftRoad().setLinkedTrafficIdx(tf1.getIndex());
		
		// TF2 scenario
		// TF2 Right prone to flood
		// TF3 Left prone to flood
		tf2.getRightRoad().setFloodArea(true);
		tf3.getLeftRoad().setFloodArea(true);
		// TF2 <---> TF3
		tf2.getRightRoad().setLinkedTrafficIdx(tf3.getIndex());
		tf3.getLeftRoad().setLinkedTrafficIdx(tf2.getIndex());
		
		// TF3 scenario
		// Large Condo - TOP
		Condominium condo = new Condominium(tf3.getName() + "_CD_[T]", 1, 2);
		tf3.getTopRoad().setCondominium(condo);
		
		// TF3 <---> TF4
		tf3.getRightRoad().setLinkedTrafficIdx(tf4.getIndex());
		tf4.getLeftRoad().setLinkedTrafficIdx(tf3.getIndex());
		
		// TF4 scenario
		WeatherContainer(com);
		CarProducerContainer(com);
		PersonProducerContainer(com);
		TrafficContainer(com);
		AccidentOrIncidentContainer ac = new AccidentOrIncidentContainer(com);
	}
	
	private void buildBluePrint(Components com, int size) {
		for (int i = 0; i < size; i++) {
			String tfName = "TF" + (i+1);
			
			Road leftRoad = new Road(tfName + "_R_[L]", Direction.LEFT, 3);
			Road topRoad = new Road(tfName + "_R_[T]", Direction.TOP, 4);
			Road downRoad = new Road(tfName + "_R_[D]", Direction.DOWN, 4);
			Road rightRoad = new Road(tfName + "_R_[R]", Direction.RIGHT, 5);
					
			Traffic tf = new Traffic(
					i+1,
					tfName,
					leftRoad, topRoad, downRoad, rightRoad);
			com.getTfs().add(tf);
		}
	}
	
	private void PersonProducerContainer(Components com) {
		for (Traffic tf: com.getTfs()) {
			for (Direction d: Direction.values()) {
				Road road = tf.getRoad(d);
				if (road.getPedestrian() != null) {
					PersonProducer pp1, pp2;
					PersonProducerCensor pps1, pps2;
					RelativeTime start = new RelativeTime(1000, 0);
					RelativeTime period = new RelativeTime(7000, 0);
					ReleaseParameters rp = new PeriodicParameters(start, period);
					// IN
					pp1 = new PersonProducer(road.getPedestrian().getName() + "_[U]", rp, com, road, tf, true);
					start = new RelativeTime(5000, 0);
					period = new RelativeTime(9000, 0);
					rp = new PeriodicParameters(start, period);
					// OUT
					pp2 = new PersonProducer(road.getPedestrian().getName() + "_[D]", rp, com, road, tf, false);
					pps1 = new PersonProducerCensor(pp1);
					pps2 = new PersonProducerCensor(pp2);
					pp1.start();
					pp2.start();
					pps1.start();
					pps2.start();
				}
			}
		}
	}
	
	private void WeatherContainer(Components com) {
		RelativeTime period = new RelativeTime(5000, 0);
		ReleaseParameters rp = new PeriodicParameters(period);
		
		WeatherConsumer wc = new WeatherConsumer("WC", rp, com);
		wc.start();
	}
	
	private void TrafficContainer(Components com) {
		RelativeTime start;
		RelativeTime period;
		ReleaseParameters rel;
		
		for (Traffic tf: com.getTfs()) {
			int t = 6000;
			period = new RelativeTime(t, 0);
			rel = new PeriodicParameters(period);
			TrafficContainer rc = new TrafficContainer(tf.getName() + "_[TCS]", rel, com, tf);
		}
	}
	
	private void CarProducerContainer(Components com) {
		for (Traffic tf: com.getTfs()) {
			int time = 3000;
			for (Direction d: Direction.values()) {
				Road road = tf.getRoad(d);
				if (road.getLinkedTrafficIdx() == -1) {
					if (road.isHighway())
						time = 2000;

					CarProducerContainer cpc = new CarProducerContainer(time, com, tf, road);
					time += 1000;
				}
			}
		}
	}
}
