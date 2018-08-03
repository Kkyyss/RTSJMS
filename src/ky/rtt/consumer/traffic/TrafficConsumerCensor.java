package ky.rtt.consumer.traffic;

import javax.realtime.RealtimeThread;
import javax.realtime.ReleaseParameters;

import ky.model.Light;

public class TrafficConsumerCensor extends RealtimeThread {
	private TrafficConsumer tfc;
	
	public TrafficConsumerCensor(ReleaseParameters rp, TrafficConsumer tfc) {
		super(null, rp);
		this.tfc = tfc;
	}
	
	public void run() {
		tfc.getHighestScoreRoad().setLight(Light.RED);
		System.out.println(tfc.getHighestScoreRoad().getName() + "'s traffic light turned into RED");
	}
}
