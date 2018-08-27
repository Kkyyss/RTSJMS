package ky.model;

import java.util.ArrayList;
import java.util.List;

public class Components {
	private boolean accidentHappened;
	private boolean accidentCleared;
	private boolean floodHappened;
	private boolean floodCleared;
	private String name;
	private List<Traffic> tfs = new ArrayList<>();
	private Weather weather;

	public boolean isFloodCleared() {
		return floodCleared;
	}
	public void setFloodCleared(boolean floodCleared) {
		this.floodCleared = floodCleared;
	}
	public boolean isAccidentCleared() {
		return accidentCleared;
	}
	public void setAccidentCleared(boolean accidentCleared) {
		this.accidentCleared = accidentCleared;
	}
	public boolean isAccidentHappened() {
		return accidentHappened;
	}
	public void setAccidentHappened(boolean accidentHappened) {
		this.accidentHappened = accidentHappened;
	}
	public boolean isFloodHappened() {
		return floodHappened;
	}
	public void setFloodHappened(boolean floodHappened) {
		this.floodHappened = floodHappened;
	}
	public Weather getWeather() {
		return weather;
	}
	public void setWeather(Weather weather) {
		this.weather = weather;
	}
	public Components(String name) {
		this.name = name;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<Traffic> getTfs() {
		return tfs;
	}
	public void setTfs(List<Traffic> tfs) {
		this.tfs = tfs;
	}
}
