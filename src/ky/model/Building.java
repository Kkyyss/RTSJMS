package ky.model;

public class Building {
	private String name;
	private int startIdx = 1;
	private int endIdx = 1;
	
	public Building(String name, int startIdx, int endIdx) {
		this.name = name;
		this.startIdx = startIdx;
		this.endIdx = endIdx;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getStartIdx() {
		return startIdx;
	}
	public void setStartIdx(int startIdx) {
		this.startIdx = startIdx;
	}
	public int getEndIdx() {
		return endIdx;
	}
	public void setEndIdx(int endIdx) {
		this.endIdx = endIdx;
	}
}
