package ky.model;

import java.util.concurrent.atomic.AtomicInteger;

public class Pedestrian {
	private String name;
	private int index;
	private int length = 2;
	private AtomicInteger firstHalfPerson = new AtomicInteger(0);
	private AtomicInteger secondHalfPerson = new AtomicInteger(0);

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

	public AtomicInteger getFirstHalfPerson() {
		return firstHalfPerson;
	}

	public void setFirstHalfPerson(AtomicInteger firstHalfPerson) {
		this.firstHalfPerson = firstHalfPerson;
	}

	public AtomicInteger getSecondHalfPerson() {
		return secondHalfPerson;
	}

	public void setSecondHalfPerson(AtomicInteger secondHalfPerson) {
		this.secondHalfPerson = secondHalfPerson;
	}	
}
