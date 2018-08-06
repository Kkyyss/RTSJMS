package ky.model;

import java.util.ArrayList;
import java.util.List;

public class Components {
	private String name;
	private List<Traffic> tfs = new ArrayList<>();
	private Weather weather;
	private boolean accidentOccurs;
	
	public boolean isAccidentOccured() {
		return accidentOccurs;
	}
	public void setAccidentOccurs(boolean accidentOccurs) {
		this.accidentOccurs = accidentOccurs;
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
