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
import ky.model.Weather;

public class AccidentOrIncidentContainer {
	private Components com;
	
	public AccidentOrIncidentContainer(Components com) {
		this.com = com;
		
		AccidentOrIncidentTrigger ac = new AccidentOrIncidentTrigger();
	}
	
	public class AccidentOrIncidentTrigger extends RealtimeThread {
		private Random rand = new Random();
		
		public AccidentOrIncidentTrigger() {
			super();
			// Accident buff up to 8s
			// RelativeTime start = new RelativeTime(4000, 0);
			RelativeTime period = new RelativeTime(3000, 0);
			ReleaseParameters rp = new PeriodicParameters(period);

			this.setReleaseParameters(rp);
			start();
		}
		
		public void run() {
			while (!com.isAccidentHappened() || !com.isFloodHappened()) {
					// Accident
					if (!com.isAccidentHappened()) {
						if (rand.nextInt(50) + 1 > 20) {
							Traffic tf = com.getTfs().get(rand.nextInt(com.getTfs().size()));
							Road r = tf.getRoad(Direction.values()[rand.nextInt(Direction.values().length)]);
							Traffic linkedTf = (r.getLinkedTrafficIdx() != -1) ? com.getTfs().get(r.getLinkedTrafficIdx() -1) : null;
							Road linkedRoad = getLinkedRoad(linkedTf, r);
							
							com.setAccidentHappened(true);
							if (linkedRoad != null) {
								linkedRoad.setAccidentOccurs(true);
								linkedTf.setAccidentOrIncidentOccurs(true);
								MyUtils.titleLog("[ACCIDENT] -> " + r.getName() + " AND " + linkedRoad.getName());
							} else {
								MyUtils.titleLog("[ACCIDENT] -> " + r.getName());
							}		
							
							r.setAccidentOccurs(true);
							tf.setAccidentOrIncidentOccurs(true);
							createDuration(r, linkedRoad, tf, linkedTf, 3000);
						}						
					} 
					if (!com.isFloodHappened() && com.isAccidentCleared()) {
						if (com.getWeather() == Weather.RAIN || com.getWeather() == Weather.THUNDERSTORM) {
							if (rand.nextInt(50) + 1 > 20) {
								// TF2/TF3 prone to flood
								Traffic tf = com.getTfs().get(1);
								Road r = tf.getRightRoad();
								Traffic linkedTf = (r.getLinkedTrafficIdx() != -1) ? com.getTfs().get(r.getLinkedTrafficIdx() -1) : null;
								Road linkedRoad = getLinkedRoad(linkedTf, r);
								
								com.setFloodHappened(true);
								if (linkedRoad != null) {
									linkedRoad.setFloodOccurs(true);
									linkedTf.setAccidentOrIncidentOccurs(true);
									MyUtils.titleLog("[FLOOD] -> " + r.getName() + " AND " + linkedRoad.getName());
								} else {
									MyUtils.titleLog("[FLOOD] -> " + r.getName());
								}	
								
								r.setFloodOccurs(true);
								tf.setAccidentOrIncidentOccurs(true);
								createDuration(r, linkedRoad, tf, linkedTf, 5000);
							}							
						}
					}
				waitForNextPeriod();
			}
		}
	}
	
	private Road getLinkedRoad(Traffic linkedTf, Road r) {
		Road linkedRoad = null;
		if (linkedTf != null) {
			switch (r.getDirection()) {
			case LEFT:
				linkedRoad = linkedTf.getRightRoad();
				break;
			case RIGHT:
				linkedRoad = linkedTf.getLeftRoad();
				break;
			}
		}
		
		return linkedRoad;
	}
	
	public void createDuration(Road r, Road linkedRoad, Traffic tf, Traffic linkedTf, int duration) {
		RelativeTime start = new RelativeTime(duration, 0);
		RelativeTime period = new RelativeTime(1000, 0);
		ReleaseParameters rp = new PeriodicParameters(start, period);
		SignalDelayDuration ad = new SignalDelayDuration(r, linkedRoad, tf, linkedTf, rp);
	}
	
	public class SignalDelayDuration extends RealtimeThread {
		private Road r;
		private Road linkedRoad;
		private Traffic tf;
		private Traffic linkedTf;
		
		public SignalDelayDuration(Road r, Road linkedRoad, Traffic tf, Traffic linkedTf, ReleaseParameters rp) {
			super(null, rp);
			this.r = r;
			this.tf = tf;
			this.linkedRoad = linkedRoad;
			this.linkedTf = linkedTf;
			
			start();
		}
		
		public void run() {
			if (r.isAccidentOccurs()) {
				MyUtils.titleLog("[ACCIDENT] -> CLEARED");
				r.setAccidentOccurs(false);
				if (linkedRoad != null) {
					linkedRoad.setAccidentOccurs(false);
				}
				com.setAccidentCleared(true);
			}
			if (r.isFloodOccurs()) {
				MyUtils.titleLog("[FLOOD] -> CLEARED");
				r.setFloodOccurs(false);		
				if (linkedRoad != null) {
					linkedRoad.setFloodOccurs(false);
				}
				com.setFloodCleared(true);
			}
		}
	}
}


