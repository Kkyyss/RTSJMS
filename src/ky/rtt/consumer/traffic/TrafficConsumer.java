package ky.rtt.consumer.traffic;

import javax.realtime.RealtimeThread;
import javax.realtime.ReleaseParameters;

import ky.model.Traffic;

public class TrafficConsumer extends RealtimeThread {
	private Traffic tf;
	
	public TrafficConsumer(ReleaseParameters rp, Traffic tf) {
		super(null, rp);
		this.tf = tf;
	}
	
	public void run() {
		while(true) {
			
		}
	}
}
