package ky.model;

import java.util.concurrent.atomic.AtomicBoolean;

public class Traffic {
	private String name;
	private int centerArea = 400;
	private Road leftRoad, topRoad, downRoad, rightRoad;
	private AtomicBoolean prolong = new AtomicBoolean(false);

	public AtomicBoolean getProlong() {
		return prolong;
	}
	public void setProlong(AtomicBoolean prolong) {
		this.prolong = prolong;
	}
	
	public Traffic(String name,
			Road leftRoad, Road topRoad, Road downRoad, Road rightRoad) {
		this.leftRoad = leftRoad;
		this.topRoad = topRoad;
		this.downRoad = downRoad;
		this.rightRoad = rightRoad;
		this.name = name;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getCenterArea() {
		return centerArea;
	}
	public void setCenterArea(int centerArea) {
		this.centerArea = centerArea;
	}
	public Road getLeftRoad() {
		return leftRoad;
	}
	public void setLeftRoad(Road leftRoad) {
		this.leftRoad = leftRoad;
	}
	public Road getTopRoad() {
		return topRoad;
	}
	public void setTopRoad(Road topRoad) {
		this.topRoad = topRoad;
	}
	public Road getDownRoad() {
		return downRoad;
	}
	public void setDownRoad(Road downRoad) {
		this.downRoad = downRoad;
	}
	public Road getRightRoad() {
		return rightRoad;
	}
	public void setRightRoad(Road rightRoad) {
		this.rightRoad = rightRoad;
	}
	public Road getRoad(Direction direction) {
		switch (direction) {
		case LEFT:
			return leftRoad;
		case TOP:
			return topRoad;
		case DOWN:
			return downRoad;
		case RIGHT:
			return rightRoad;
		}
		return null;
	}
}
