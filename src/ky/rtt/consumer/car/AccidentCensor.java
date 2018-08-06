package ky.rtt.consumer.car;

import javax.realtime.RealtimeThread;
import javax.realtime.ReleaseParameters;

import ky.model.Components;
import ky.model.Road;

public class AccidentCensor extends RealtimeThread {
	private Components com;
	private Road road;
	
	public AccidentCensor(ReleaseParameters rp, Road road, Components com) {
		super(null, rp);
		this.com = com;
		this.road = road;
	}
	
	public void run() {
		road.setAccidentArea(false);
		com.setAccidentOccurs(false);
		System.out.println("Accident buff gone...");
	}
}
