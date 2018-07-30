package ky.model;

public class Road {
	private String name;
	private Traffic linkedTraffic;
	private Direction direction;
	private Light light;
	private Condominium condominium;
	private int length = 1000;
	private int totalInCarLength = 0;
	private int totalOutCarLength = 0;
	private int width = 200;
	private Pedestrian pedestrian;
	private boolean flooded;
	
	public int getTotalOutCarLength() {
		return totalOutCarLength;
	}
	public void setTotalOutCarLength(int totalOutCarLength) {
		this.totalOutCarLength = totalOutCarLength;
	}
	public Traffic getLinkedTraffic() {
		return linkedTraffic;
	}
	public void setLinkedTraffic(Traffic linkedTraffic) {
		this.linkedTraffic = linkedTraffic;
	}
	public int getTotalInCarLength() {
		return totalInCarLength;
	}
	public void setTotalInCarLength(int totalInCarLength) {
		this.totalInCarLength = totalInCarLength;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Light getLight() {
		return light;
	}
	public void setLight(Light light) {
		this.light = light;
	}
	public Road(String name, Direction direction) {
		this.direction = direction;
		this.name = name;
	}
	public Direction getDirection() {
		return direction;
	}
	public void setDirection(Direction direction) {
		this.direction = direction;
	}
	public Condominium getCondominium() {
		return condominium;
	}
	public void setCondominium(Condominium condominium) {
		this.condominium = condominium;
	}
	public Pedestrian getPedestrian() {
		return pedestrian;
	}
	public void setPedestrian(Pedestrian pedestrian) {
		this.pedestrian = pedestrian;
	}
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public boolean isFlooded() {
		return flooded;
	}
	public void setFlooded(boolean flooded) {
		this.flooded = flooded;
	}
	public int getLength() {
		return length;
	}
	public void setLength(int length) {
		this.length = length;
	}
}
