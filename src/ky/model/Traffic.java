package ky.model;

public class Traffic {
	private String name;
	private int centerArea = 400;
	private Position pos;
	private Road leftRoad, topRoad, downRoad, rightRoad;
	
	public Traffic(String name, Position pos,
			Road leftRoad, Road topRoad, Road downRoad, Road rightRoad) {
		this.leftRoad = leftRoad;
		this.topRoad = topRoad;
		this.downRoad = downRoad;
		this.rightRoad = rightRoad;
		this.name = name;
		this.pos = pos;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Position getPos() {
		return pos;
	}
	public void setPos(Position pos) {
		this.pos = pos;
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
