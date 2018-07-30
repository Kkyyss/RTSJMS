package ky.model;

public class Components {
	private String name;
	private Traffic tf1, tf2, tf3;
	private String weather; // Maybe class----API slow down shits
	
	public Components(String name, Traffic tf1) {
		this.name = name;
		this.tf1 = tf1;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Traffic getTf1() {
		return tf1;
	}
	public void setTf1(Traffic tf1) {
		this.tf1 = tf1;
	}
	public Traffic getTf2() {
		return tf2;
	}
	public void setTf2(Traffic tf2) {
		this.tf2 = tf2;
	}
	public Traffic getTf3() {
		return tf3;
	}
	public void setTf3(Traffic tf3) {
		this.tf3 = tf3;
	}
	public String getWeather() {
		return weather;
	}
	public void setWeather(String weather) {
		this.weather = weather;
	}
}
