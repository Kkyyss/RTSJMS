package ky.rtt.consumer.traffic;

import javax.realtime.RealtimeThread;
import javax.realtime.ReleaseParameters;

import ky.Utils.MyUtils;
import ky.model.Components;
import ky.model.Light;

public class TrafficConsumerCensor extends RealtimeThread {
	private TrafficConsumer tfc;
	private Components com;
	
	public TrafficConsumerCensor(ReleaseParameters rp, TrafficConsumer tfc, Components com) {
		super(null, rp);
		this.tfc = tfc;
		this.com = com;
	}
	
	public void run() {
		while (tfc.getHighestScoreRoad().getLight() == Light.GREEN) {
			if (!com.isAccidentOccured()) {
				tfc.getHighestScoreRoad().setLight(Light.RED);
				MyUtils.log(tfc.getTf().getIndex(), tfc.getHighestScoreRoad().getName() + "'s traffic light turned into RED");				
			}
			waitForNextPeriod();
		}
	}
}
