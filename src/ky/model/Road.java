package ky.model;

import java.util.Arrays;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerArray;

public class Road {
	private AtomicInteger []accident;
	private String name;
	private Traffic linkedTraffic;
	private Direction direction;
	private Light light = Light.RED;
	private Condominium condominium;
	private int length = 3;
	private LinkedBlockingQueue<Car> inCars = new LinkedBlockingQueue<>(); 
	private LinkedBlockingQueue<Car> outCars = new LinkedBlockingQueue<>();
	private Pedestrian pedestrian;
	private boolean flooded;
	private Car latestCar; 

	
	public AtomicInteger[] getAccident() {
		return accident;
	}
	public void setAccident(AtomicInteger[] accident) {
		this.accident = accident;
	}
	public Car getLatestCar() {
		return latestCar;
	}
	public void setLatestCar(Car latestCar) {
		this.latestCar = latestCar;
	}
	public LinkedBlockingQueue<Car> getInCars() {
		return inCars;
	}
	public void setInCars(LinkedBlockingQueue<Car> inCars) {
		this.inCars = inCars;
	}
	public LinkedBlockingQueue<Car> getOutCars() {
		return outCars;
	}
	public void setOutCars(LinkedBlockingQueue<Car> outCars) {
		this.outCars = outCars;
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
		accident = new AtomicInteger[length];
		Arrays.fill(accident, 0);
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
	public boolean isFlooded() {
		return flooded;
	}
	public void setFlooded(boolean flooded) {
		this.flooded = flooded;
	}
	public int getLength() {
		return length;
	}
	public void setLength(int length) {
		this.length = length;
	}
	public boolean isInCarsMax() {
		return inCars.size() == length;
	}
	public boolean isOutCarsMax() {
		return outCars.size() == length;
	}
}
