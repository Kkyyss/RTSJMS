package ky.model;

public class Person {
	private String name;
	private Components com;
	private Road road;
	private int forwarding;
	private boolean leave;
	private Traffic tf;

	public Traffic getTf() {
		return tf;
	}
	public void setTf(Traffic tf) {
		this.tf = tf;
	}
	public boolean isLeave() {
		return leave;
	}
	public void setLeave(boolean leave) {
		this.leave = leave;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Components getCom() {
		return com;
	}
	public void setCom(Components com) {
		this.com = com;
	}
	public Road getRoad() {
		return road;
	}
	public void setRoad(Road road) {
		this.road = road;
	}
	public int getForwarding() {
		return forwarding;
	}
	public void setForwarding(int forwarding) {
		this.forwarding = forwarding;
	}
	public void increaseForward(int num) {
		forwarding += num;
	}
}
