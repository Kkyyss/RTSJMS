package ky.model;

public class Car {
	private String name;
	private Components bj;
	private Traffic tf;
	private Road road;
	private Boolean in;
	private int length = 200;
	private int to;
	private int forwarding;

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

	public int getTo() {
		return to;
	}

	public void setTo(int to) {
		this.to = to;
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

	public void setForwarding(int forwarding) {
		this.forwarding = forwarding;
	}
}
