package ky.rtt.consumer.traffic;

import javax.realtime.AsyncEvent;
import javax.realtime.AsyncEventHandler;
import javax.realtime.AsynchronouslyInterruptedException;
import javax.realtime.Interruptible;
import javax.realtime.PeriodicParameters;
import javax.realtime.RealtimeThread;
import javax.realtime.RelativeTime;
import javax.realtime.ReleaseParameters;

import ky.Utils.MyUtils;
import ky.model.Components;
import ky.model.Direction;
import ky.model.Light;
import ky.model.Road;
import ky.model.Traffic;

public class TrafficContainer {
	private String name;
	private Components com;
	private AsyncEvent startEvt;
	private AsyncEvent stopEvt;
	private RelativeTime start, period;
	private AsynchronouslyInterruptedException aie = null;
	private ReleaseParameters rel;
	private GreenConsumer rc;
	private Traffic tf;
	private Direction previousGreenSignalRoad = null;

	public TrafficContainer(String name, ReleaseParameters rel, Components com, Traffic tf) {
		aie = new AsynchronouslyInterruptedException();
		this.name = name;
		this.com = com;
		this.rel = rel;
		this.tf = tf;

		rc = new GreenConsumer(rel);
		
		Stop stop = new Stop();
		Start start = new Start();
		
		stopEvt = new AsyncEvent();
		stopEvt.addHandler(stop);
		startEvt = new AsyncEvent();
		startEvt.addHandler(start);
		
		TrafficConsumerSensor rcs = new TrafficConsumerSensor();
	}
	private void renewConsumer() {
		rc = new GreenConsumer(rel);
	}
	
	public class GreenConsumer extends RealtimeThread implements Interruptible {	
		private boolean initial = true;
		
		public GreenConsumer(ReleaseParameters rel) {
			super(null, rel);
			super.setName(name);
			this.start();
		}
		
		public void run() {
			aie.doInterruptible(this);
		}
		
		@Override
		public void interruptAction(AsynchronouslyInterruptedException exception) {
			this.deschedulePeriodic();
		}

		@Override
		public void run(AsynchronouslyInterruptedException aie) throws AsynchronouslyInterruptedException {
			while (true) {
				if (initial) {
					if (previousGreenSignalRoad != null) {
						tf.getRoad(previousGreenSignalRoad).setLight(Light.GREEN);
						MyUtils.log(tf.getIndex(), tf.getRoad(previousGreenSignalRoad).getName() + " -> GREEN [CONT]");
						previousGreenSignalRoad = null;
					} else {
						tf.getLeftRoad().setLight(Light.GREEN);
						MyUtils.log(tf.getIndex(), tf.getLeftRoad().getName() + " -> GREEN [INIT]");
					}
					initial = false;
				} else {
						for (int j = 0; j < Direction.values().length; j++) { 
							Direction direction = Direction.values()[j];
							Road r = tf.getRoad(direction);
							String sLog = "";
							if (r.getLight() == Light.GREEN) {
								r.setLight(Light.RED);
								MyUtils.log(tf.getIndex(), r.getName() + " -> RED");
								switch (r.getDirection()) {
								case DOWN:
									tf.getLeftRoad().setLight(Light.GREEN);
									sLog = tf.getLeftRoad().getName();
									break;
								case LEFT:
									tf.getTopRoad().setLight(Light.GREEN);
									sLog = tf.getTopRoad().getName();
									break;
								case RIGHT:
									tf.getDownRoad().setLight(Light.GREEN);
									sLog = tf.getDownRoad().getName();
									break;
								case TOP:
									tf.getRightRoad().setLight(Light.GREEN);
									sLog = tf.getRightRoad().getName();
									break;
								default:
									break;
								}			
								MyUtils.log(tf.getIndex(), sLog + " -> GREEN");
								break;
							}
						}

				}
				
				try {
					Thread.sleep(5900);
				} catch (InterruptedException e) {
				}

				waitForNextPeriod();
			}
		}
	}
	
	public class Stop extends AsyncEventHandler {

		public void handleAsyncEvent() {
			aie.fire();
		}
	}
	
	public class Start extends AsyncEventHandler {
		
		public void handleAsyncEvent() {
			renewConsumer();
		}
	}
	
	public class TrafficConsumerSensor extends RealtimeThread {
		private boolean accidentTrigger;
		private boolean floodTrigger;
		public TrafficConsumerSensor() {
			super();
			RelativeTime period = new RelativeTime(90, 0);
			ReleaseParameters rp = new PeriodicParameters(period);
			this.setReleaseParameters(rp);
			start();
		}
		
		public void run() {
			while (true) {
				accidentLogic(com.isAccidentHappened());	
				if (com.isAccidentCleared()) {
					floodLogic(com.isFloodHappened());	
				}
				
				waitForNextPeriod();
			}
		}
		
		private void accidentLogic(boolean occurs) {
			if (occurs && !accidentTrigger && !com.isAccidentCleared()) {
				if (tf.isAccidentOrIncidentOccurs()) {
					adjustTrafficLight();
					accidentTrigger = true;
					stopEvt.fire();
				}
			}
			
			if (accidentTrigger && com.isAccidentCleared()) {
				tf.setAccidentOrIncidentOccurs(false);
				initialTrafficLight();
				accidentTrigger = false;
				startEvt.fire();	
			}			
		}
		
		private void floodLogic(boolean occurs) {
			if (occurs && !floodTrigger) {
				if (tf.isAccidentOrIncidentOccurs()) {
					adjustTrafficLight();
					floodTrigger = true;
					stopEvt.fire();						
				}
			}
			
			if (floodTrigger && com.isFloodCleared()) {
				tf.setAccidentOrIncidentOccurs(false);
				initialTrafficLight();
				floodTrigger = false;
				startEvt.fire();	
			}				
		}
		
		private void adjustTrafficLight() {
				for (int j = 0; j < Direction.values().length; j++) { 
					Direction direction = Direction.values()[j];
					Road r = tf.getRoad(direction);
					Traffic linkedTf = (r.getLinkedTrafficIdx() != -1) ? com.getTfs().get(r.getLinkedTrafficIdx() - 1) : null;
					if (r.isAccidentOccurs() || r.isFloodOccurs()) {
						if (r.getLight() == Light.GREEN) {
							previousGreenSignalRoad = r.getDirection();
							MyUtils.log(tf.getIndex(), r.getName() + " -> GREEN [EXT]");
						} else {
							r.setLight(Light.GREEN);
							MyUtils.log(tf.getIndex(), r.getName() + " -> GREEN [CHG]");
						}
					} else {
						if (r.getLight() == Light.GREEN) {
							previousGreenSignalRoad = r.getDirection();
							r.setLight(Light.RED);
							MyUtils.log(tf.getIndex(), r.getName() + " -> RED [CHG]");									
						}
					}
				}
		}
		
		private void initialTrafficLight() {
			for (int i = 0; i < Direction.values().length; i++) { 
				Direction direction = Direction.values()[i];
				Road r = tf.getRoad(direction);
				
				if (r.getLight() == Light.GREEN) {
					r.setLight(Light.RED);	
					MyUtils.log(tf.getIndex(), r.getName() + " -> RED [CHG]");
					break;
				}
			}
		}
	}
}
