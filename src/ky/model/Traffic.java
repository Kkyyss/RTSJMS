package ky.model;

import javax.realtime.AsyncEvent;

public class Traffic {
	private String name;
	private int index;
	private Road leftRoad, topRoad, downRoad, rightRoad;
	private AsyncEvent trigger;
	private boolean accidentOrIncidentOccurs;

	public boolean isAccidentOrIncidentOccurs() {
		return accidentOrIncidentOccurs;
	}
	public void setAccidentOrIncidentOccurs(boolean accidentOrIncidentOccurs) {
		this.accidentOrIncidentOccurs = accidentOrIncidentOccurs;
	}
	public AsyncEvent getTrigger() {
		return trigger;
	}
	public void setTrigger(AsyncEvent trigger) {
		this.trigger = trigger;
	}
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	public Traffic(int index, String name,
			Road leftRoad, Road topRoad, Road downRoad, Road rightRoad) {
		this.index = index;
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
