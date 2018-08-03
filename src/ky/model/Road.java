package ky.model;

import java.util.Comparator;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import ky.Utils.MyUtils;

public class Road {
	private AtomicBoolean []accidentArea;
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
	private int score;
	private int time;
	private int weight;

	public boolean isHighway() {
		return highway;
	}
	public void setHighway(boolean highway) {
		this.highway = highway;
	}
	public int getTime() {
		return time;
	}
	public void setTime(int time) {
		this.time = time;
	}
	public int getWeight() {
		return weight;
	}
	public void setWeight(int weight) {
		this.weight = weight;
	}
	public int getScore() {
		return time * weight;
	}
	public void setScore(int score) {
		this.score = score;
	}
	public School getSchool() {
		return school;
	}
	public void setSchool(School school) {
		this.school = school;
	}
	public boolean isAccidentOccurs() {
		for (AtomicBoolean aa: accidentArea) {
			if (aa.get() == true) return true;
		}
		return false;
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
	public AtomicBoolean[] getAccidentArea() {
		return accidentArea;
	}
	public void setAccidentArea(AtomicBoolean[] accidentArea) {
		this.accidentArea = accidentArea;
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
		accidentArea = new AtomicBoolean[length];
		MyUtils.initialArrayOfAtomicBooleanVal(accidentArea);
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
	
	public static class RoadsWeightRanking implements Comparator<Road> {

		@Override
		public int compare(Road o1, Road o2) {
			return o1.getScore() < o2.getScore() ? -1 
					: o1.getScore() == o2.getScore() ? 0 : 1;
		}
	}
}
