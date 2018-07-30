package ky.rtt.producer.car;

import javax.realtime.AsyncEvent;
import javax.realtime.RealtimeThread;
import javax.realtime.ReleaseParameters;

import ky.model.Components;
import ky.model.Car;
import ky.model.Road;
import ky.model.Traffic;

public class CarProducer extends RealtimeThread {
	private Components bj;
	private Traffic tf;
	private Road road;
	private int carID;

	public Traffic getTf() {
		return tf;
	}
	public void setTf(Traffic tf) {
		this.tf = tf;
	}
	public Components getBj() {
		return bj;
	}
	public void setBj(Components bj) {
		this.bj = bj;
	}
	public Road getRoad() {
		return road;
	}
	public int getCarID() {
		return carID;
	}

	public void setCarID(int carID) {
		this.carID = carID;
	}

	public void setRoad(Road road) {
		this.road = road;
	}

	public CarProducer(String name, ReleaseParameters rp, Components bj, Traffic tf, Road road) {
		super(null, rp);
		super.setName(name);
		this.bj = bj;
		this.tf = tf;
		this.road = road;
	}
	
	public void run() {
		while (true) {
			carID++;
			String name = super.getName() + "-CAR-" + carID;
			Car car = new Car();
			car.setName(name);
			car.setRoad(road);
			car.setBj(bj);
			car.setIn(true);
			car.setTo(road.getLength() - road.getTotalInCarLength());
			road.setTotalInCarLength(
					road.getTotalInCarLength() + car.getLength());
			System.out.println(name + " produced.");
			waitForNextPeriod();
		}
	}
	
	public boolean isMax() {
		return road.getTotalInCarLength() >= road.getLength();
	}
	public boolean isMin() {
		return road.getTotalInCarLength() <= 1;
	}
}
