package ky.model;

import java.util.concurrent.atomic.AtomicInteger;

public class Pedestrian {
	private String name;
	private int index;
	private int length;
	private AtomicInteger totalPerson = new AtomicInteger(0);
	private AtomicInteger totalFallDown = new AtomicInteger(0);

	public Pedestrian(String name, int index) {
		this.name = name;
		this.index = index;
	}
	
	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public AtomicInteger getTotalFallDown() {
		return totalFallDown;
	}

	public void setTotalFallDown(AtomicInteger totalFallDown) {
		this.totalFallDown = totalFallDown;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public AtomicInteger getTotalPerson() {
		return totalPerson;
	}

	public void setTotalPerson(AtomicInteger totalPerson) {
		this.totalPerson = totalPerson;
	}
	
	public void increasePerson() {
		totalPerson.incrementAndGet();
	}
	public void decreasePerson() {
		totalPerson.decrementAndGet();
	}
	public void increaseFallDown() {
		totalFallDown.incrementAndGet();
	}
	public void decreaseFallDown() {
		totalFallDown.decrementAndGet();
	}
}
