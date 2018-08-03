package ky.rtt.consumer.traffic;

import java.util.Arrays;
import java.util.Random;

import javax.realtime.PeriodicParameters;
import javax.realtime.RealtimeThread;
import javax.realtime.RelativeTime;
import javax.realtime.ReleaseParameters;

import ky.model.Components;
import ky.model.Light;
import ky.model.Road;
import ky.model.Traffic;
import ky.model.Weather;

public class TrafficConsumer extends RealtimeThread {
	private Road topRoad;
	private Road leftRoad;
	private Road rightRoad;
	private Road downRoad;
	private Traffic tf;
	private RelativeTime start, period;
	private Components com;
	private Road highestScoreRoad;
	
	public TrafficConsumer(String name, ReleaseParameters rp, Components com, Traffic tf) {
		super(null, rp);
		super.setName(name);
		this.tf = tf;
		this.com = com;
		this.topRoad = tf.getTopRoad();
		this.leftRoad = tf.getLeftRoad();
		this.rightRoad = tf.getRightRoad();
		this.downRoad = tf.getDownRoad();
	}
	
	public void run() {
		System.out.println(super.getName() + " started...");
		while(true) {
			// Prioritize traffic signal
			topRoad.setTime(trafficTime(topRoad));
			leftRoad.setTime(trafficTime(leftRoad));
			rightRoad.setTime(trafficTime(rightRoad));
			downRoad.setTime(trafficTime(downRoad));

			topRoad.setWeight(trafficWeight(topRoad));
			leftRoad.setWeight(trafficWeight(leftRoad));
			rightRoad.setWeight(trafficWeight(rightRoad));
			downRoad.setWeight(trafficWeight(downRoad));
			
			Road[] roads = { topRoad, leftRoad, rightRoad, downRoad };
			
			Arrays.sort(roads, new Road.RoadsWeightRanking());
			
			// Highest == 0
			highestScoreRoad = roads[roads.length - 1];
			if (highestScoreRoad.getScore() == 0) {
				Random rand = new Random();
				highestScoreRoad = roads[rand.nextInt(4)]; 
			}
			
			highestScoreRoad.setLight(Light.GREEN);
			System.out.println(highestScoreRoad.getName() + "'s traffic light turned into GREEN");
			start = new RelativeTime(highestScoreRoad.getTime(), 0);
			period = new RelativeTime(1000, 0);
			TrafficConsumerCensor tcc = new TrafficConsumerCensor(new PeriodicParameters(start, period), this);
			tcc.start();
			period = new RelativeTime(highestScoreRoad.getTime() + 500, 0);
			setReleaseParameters(new PeriodicParameters(period));
			waitForNextPeriod();
		}
	}
	
	public Road getHighestScoreRoad() {
		return highestScoreRoad;
	}

	public void setHighestScoreRoad(Road highestScoreRoad) {
		this.highestScoreRoad = highestScoreRoad;
	}

	private int trafficTime(Road road) {
		int time = 5000;
		
		if (com.getWeather() == Weather.RAIN || com.getWeather() == Weather.THUNDERSTORM) {
			time += 2000;
		}
		if (road.isAccidentOccurs()) {
			time += 3000;
		}
		if (road.getFloodArea().get()) {
			time += 3000;
		}
		if (isFallDown(road)) {
			time += 1000;
		}
		return time;
	}
	
	private int trafficWeight(Road road) {
		int totalInCars = road.getTotalInCars().get();
		if (totalInCars == 0) return 0;
		
		if (totalInCars < road.getLength() / 2) {
			return 1;
		}
		if (totalInCars == road.getLength() / 2) {
			return 2;
		}
		if (totalInCars > road.getLength() / 2) {
			return 3;
		}
		return 1;
	}
	
	private boolean isFallDown(Road road) {
		if (road.getPedestrian() != null) {
			switch (road.getDirection()) {
			case LEFT:
			case DOWN:
				if (road.getPedestrian().getFirstHalfFallDown().get() > 0) {
					return true;
				}
				break;
			case RIGHT:
			case TOP:
				if (road.getPedestrian().getSecondHalfFallDown().get() > 0) {
					return true;
				}
				break;
			}
		}
		return false;
	}
}
