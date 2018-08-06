package ky.model;

public class Car {
	private String name;
	private Components bj;
	private Traffic tf;
	private Road road;
	private boolean isLeave;
	private Boolean in;
	private int length = 1;
	private int forwarding = 0;
	private Car frontCar;

	public boolean isLeave() {
		return isLeave;
	}
	public void setLeave(boolean isLeave) {
		this.isLeave = isLeave;
	}
	public Car getFrontCar() {
		return frontCar;
	}
	public void setFrontCar(Car frontCar) {
		this.frontCar = frontCar;
	}
	public Components getBj() {
		return bj;
	}
	public Boolean getIn() {
		return in;
	}

	public void setIn(Boolean in) {
		this.in = in;
	}

	public void setBj(Components bj) {
		this.bj = bj;
	}
	public Traffic getTf() {
		return tf;
	}

	public void setTf(Traffic tf) {
		this.tf = tf;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public Road getRoad() {
		return road;
	}

	public void setRoad(Road road) {
		this.road = road;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getForwarding() {
		return forwarding;
	}
	public void increaseForward(int num) {
		forwarding += num;
	}

	public void setForwarding(int forwarding) {
		this.forwarding = forwarding;
	}
}
