package ky.model;

import java.util.Comparator;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import ky.rtt.consumer.road.RoadConsumer;

public class Road {
	private boolean isAccidentArea;
	private String name;
	private Traffic linkedTraffic;
	private Direction direction;
	private Light light = Light.RED;
	private Condominium condominium;
	private School school;
	private int length = 3;
	private boolean highway = false;
	private Pedestrian pedestrian;
	private AtomicBoolean floodArea = new AtomicBoolean(false);
	private Car latestCar;
	private AtomicInteger totalInCars = new AtomicInteger(0);
	private AtomicInteger totalOutCars = new AtomicInteger(0);
	private RoadConsumer rc;

	public RoadConsumer getRc() {
		return rc;
	}
	public void setRc(RoadConsumer rc) {
		this.rc = rc;
	}
	public boolean isAccidentArea() {
		return isAccidentArea;
	}
	public void setAccidentArea(boolean isAccidentArea) {
		this.isAccidentArea = isAccidentArea;
	}
	public boolean isHighway() {
		return highway;
	}
	public void setHighway(boolean highway) {
		this.highway = highway;
	}
	public School getSchool() {
		return school;
	}
	public void setSchool(School school) {
		this.school = school;
	}
	public AtomicInteger getTotalInCars() {
		return totalInCars;
	}
	public void setTotalInCars(AtomicInteger totalInCars) {
		this.totalInCars = totalInCars;
	}
	public AtomicInteger getTotalOutCars() {
		return totalOutCars;
	}
	public void setTotalOutCars(AtomicInteger totalOutCars) {
		this.totalOutCars = totalOutCars;
	}
	public AtomicBoolean getFloodArea() {
		return floodArea;
	}
	public void setFloodArea(AtomicBoolean floodArea) {
		this.floodArea = floodArea;
	}
	public Car getLatestCar() {
		return latestCar;
	}
	public void setLatestCar(Car latestCar) {
		this.latestCar = latestCar;
	}
	public Traffic getLinkedTraffic() {
		return linkedTraffic;
	}
	public void setLinkedTraffic(Traffic linkedTraffic) {
		this.linkedTraffic = linkedTraffic;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Light getLight() {
		return light;
	}
	public void setLight(Light light) {
		this.light = light;
	}
	public Road(String name, Direction direction, int length) {
		this.direction = direction;
		this.name = name;
		this.length = length;
	}
	public Direction getDirection() {
		return direction;
	}
	public void setDirection(Direction direction) {
		this.direction = direction;
	}
	public Condominium getCondominium() {
		return condominium;
	}
	public void setCondominium(Condominium condominium) {
		this.condominium = condominium;
	}
	public Pedestrian getPedestrian() {
		return pedestrian;
	}
	public void setPedestrian(Pedestrian pedestrian) {
		this.pedestrian = pedestrian;
	}
	public int getLength() {
		return length;
	}
	public void setLength(int length) {
		this.length = length;
	}
	public boolean isInCarsMax() {
		return totalInCars.get() == length;
	}
	public boolean isOutCarsMax() {
		return totalOutCars.get() == length;
	}
	public void updateInCars(int num) {
    while(true) {
        int existingValue = totalInCars.get();
        int newValue = existingValue + num;
        if(totalInCars.compareAndSet(existingValue, newValue)) {
            return;
        }
    }
	}
	public void updateOutCars(int num) {
    while(true) {
        int existingValue = totalOutCars.get();
        int newValue = existingValue + num;
        if(totalOutCars.compareAndSet(existingValue, newValue)) {
            return;
        }
    }
	}
}
