package ky.rtt.consumer.traffic;

import java.util.Random;

import javax.realtime.PeriodicParameters;
import javax.realtime.RealtimeThread;
import javax.realtime.RelativeTime;
import javax.realtime.ReleaseParameters;

import ky.Utils.MyUtils;
import ky.model.Components;
import ky.model.Direction;
import ky.model.Road;
import ky.model.Traffic;

public class AccidentContainer {
	private Components com;
	
	public AccidentContainer(Components com) {
		this.com = com;
		
		AccidentCensor ac = new AccidentCensor();
	}
	
	public class AccidentCensor extends RealtimeThread {
		private Random rand = new Random();
		
		public AccidentCensor() {
			super();
			// Accident buff up to 8s
			// RelativeTime start = new RelativeTime(4000, 0);
			RelativeTime period = new RelativeTime(3000, 0);
			ReleaseParameters rp = new PeriodicParameters(period);

			
			this.setReleaseParameters(rp);
			start();
		}
		
		public void run() {
			while (true) {
				if (!com.isAccidentOccured()) {
					if (rand.nextInt(50) + 1 > 20) {
						Traffic tf = com.getTfs().get(rand.nextInt(com.getTfs().size()));
						Road r = tf.getRoad(Direction.values()[rand.nextInt(Direction.values().length)]);
						MyUtils.titleLog("[ACCIDENT] -> " + r.getName());
						com.setAccidentOccurs(true);
						r.setAccidentArea(true);
						tf.setTrafficAccident(true);
						
						AccidentDuration ad = new AccidentDuration(r, tf);
					}
				}
				
				waitForNextPeriod();
			}
		}
	}
	
	public class AccidentDuration extends RealtimeThread {
		private Road r;
		private Traffic tf;
		
		public AccidentDuration(Road r, Traffic tf) {
			this.r = r;
			this.tf = tf;
			
			RelativeTime start = new RelativeTime(3000, 0);
			RelativeTime period = new RelativeTime(1000, 0);
			ReleaseParameters rp = new PeriodicParameters(start, period);
			
			this.setReleaseParameters(rp);
			start();
		}
		
		public void run() {
			MyUtils.titleLog("[ACCIDENT] -> CLEARED");
			com.setAccidentOccurs(false);
			r.setAccidentArea(false);
			tf.setTrafficAccident(false);
		}
	}
}


