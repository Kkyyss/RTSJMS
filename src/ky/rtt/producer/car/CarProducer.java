package ky.rtt.producer.car;

import javax.realtime.RealtimeThread;
import javax.realtime.ReleaseParameters;

import ky.Utils.MyUtils;
import ky.model.Car;
import ky.model.Components;
import ky.model.Road;
import ky.model.Traffic;
import ky.rtt.consumer.car.CarConsumer;
import ky.rtt.consumer.car.CarConsumerCensor;

public class CarProducer extends RealtimeThread {
	private Components bj;
	private Traffic tf;
	private Road road;
	private int carID;
	private Car previousCar;

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
			String name = super.getName() + "_CAR_" + carID;
			Car car = new Car();
			car.setName(name);
			car.setBj(bj);
			car.setTf(tf);
			car.setRoad(road);
			car.setFrontCar(previousCar);
			car.setIn(true);
			MyUtils.log(tf.getIndex(), name + " produced.");
			road.getTotalInCars().incrementAndGet();
			previousCar = car;
			if (road.isInCarsMax())
				MyUtils.log(tf.getIndex(), road.getName() + " fulled...");
			
			CarConsumer cc = new CarConsumer("CC-" + name, car);
			CarConsumerCensor censor = new CarConsumerCensor(cc);
			cc.start();
			censor.start();
			waitForNextPeriod();
		}
	}
	
	public boolean isMax() {
		return road.getTotalInCars().get() >= road.getLength() - 1;
	}
	public boolean isMin() {
		return road.getTotalInCars().get() <= 1;
	}
}
