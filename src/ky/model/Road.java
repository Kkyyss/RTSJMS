package ky.model;

import java.util.concurrent.atomic.AtomicInteger;

public class Road {
	private boolean isAccidentOccurs;
	private boolean isFloodOccurs;
	private String name;
	private int linkedTrafficIdx = -1;
	private Direction direction;
	private Light light = Light.RED;
	private Condominium condominium;
	private School school;
	private int length = 3;
	private boolean highway = false;
	private Pedestrian pedestrian;
	private boolean floodArea;
	private Car latestCar;
	private AtomicInteger totalInCars = new AtomicInteger(0);
	private AtomicInteger totalOutCars = new AtomicInteger(0);

	public boolean isFloodOccurs() {
		return isFloodOccurs;
	}

	public void setFloodOccurs(boolean isFloodOccurs) {
		this.isFloodOccurs = isFloodOccurs;
	}
	public boolean isAccidentOccurs() {
		return isAccidentOccurs;
	}
	public void setAccidentOccurs(boolean isAccidentOccurs) {
		this.isAccidentOccurs = isAccidentOccurs;
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
	public boolean isFloodArea() {
		return floodArea;
	}
	public boolean isHighway() {
		return highway;
	}
	public void setHighway(boolean highway) {
		this.highway = highway;
	}
	public void setFloodArea(boolean floodArea) {
		this.floodArea = floodArea;
	}
	public Car getLatestCar() {
		return latestCar;
	}
	public void setLatestCar(Car latestCar) {
		this.latestCar = latestCar;
	}
	public int getLinkedTrafficIdx() {
		return linkedTrafficIdx;
	}
	public void setLinkedTrafficIdx(int linkedTrafficIdx) {
		this.linkedTrafficIdx = linkedTrafficIdx;
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
