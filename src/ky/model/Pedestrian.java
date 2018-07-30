package ky.model;

import java.util.List;

public class Pedestrian {
	private String name;
	private int width;
	private int lLocated;
	private int rLocated;
	private List<Person> lPerson;
	private List<Person> rPerson;
	
	public List<Person> getlPerson() {
		return lPerson;
	}
	public void setlPerson(List<Person> lPerson) {
		this.lPerson = lPerson;
	}
	public List<Person> getrPerson() {
		return rPerson;
	}
	public void setrPerson(List<Person> rPerson) {
		this.rPerson = rPerson;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public int getlLocated() {
		return lLocated;
	}
	public void setlLocated(int lLocated) {
		this.lLocated = lLocated;
	}
	public int getrLocated() {
		return rLocated;
	}
	public void setrLocated(int rLocated) {
		this.rLocated = rLocated;
	}
}
