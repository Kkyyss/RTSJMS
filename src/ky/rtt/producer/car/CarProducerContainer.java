package ky.rtt.producer.car;

import javax.realtime.AsyncEvent;
import javax.realtime.AsyncEventHandler;
import javax.realtime.PeriodicParameters;
import javax.realtime.RealtimeThread;
import javax.realtime.RelativeTime;
import javax.realtime.ReleaseParameters;

import ky.Utils.MyUtils;
import ky.model.Car;
import ky.model.Components;
import ky.model.Road;
import ky.model.Traffic;
import ky.rtt.consumer.car.CarConsumer;
import ky.rtt.consumer.car.CarConsumerCensor;

public class CarProducerContainer {
	private Components bj;
	private int time;
	private Traffic tf;
	private Road road;
	
	public CarProducerContainer(int time, Components bj, Traffic tf, Road road) {
		this.bj = bj;
		this.time = time;
		this.tf = tf;
		this.road = road;
		
		RelativeTime period = new RelativeTime(time, 0);
		ReleaseParameters rp = new PeriodicParameters(period);
		CarProducer cp = new CarProducer(road.getName() + "_[CPR]", rp);
		
		CarProducerCensor cpc = new CarProducerCensor(rp, cp);
	}
	
	public class CarProducer extends RealtimeThread {
		private int carID;
		private Car previousCar;

		public Road getRoad() {
			return road;
		}
		public int getCarID() {
			return carID;
		}

		public void setCarID(int carID) {
			this.carID = carID;
		}

		public CarProducer(String name, ReleaseParameters rp) {
			super(null, rp);
			super.setName(name);
			
			start();
		}
		
		public void run() { 
			while (true) {
				carID++;
				String name = road.getName() + "_C" + carID;
				Car car = new Car();
				car.setName(name);
				car.setBj(bj);
				car.setTf(tf);
				car.setRoad(road);
				car.setFrontCar(previousCar);
				car.setIn(true);
				MyUtils.log(tf.getIndex(), name + " [INC]");
				road.getTotalInCars().incrementAndGet();
				previousCar = car;
				if (road.isInCarsMax())
					MyUtils.log(tf.getIndex(), road.getName() + " [MAX]");
				
				CarConsumer cc = new CarConsumer("CC_" + name, car);
				CarConsumerCensor censor = new CarConsumerCensor(cc);
				cc.start();
				censor.start();
				waitForNextPeriod();
			}
		}
		
		public boolean isMax() {
			return road.getTotalInCars().get() >= road.getLength() - 1;
		}
		public boolean isMin() {
			return road.getTotalInCars().get() <= 1;
		}
	}
	
	public class CarProducerHandler {
		private CarProducer rtt;
		private Stop stop = new Stop();
		private Start start = new Start();

		public CarProducer getRtt() {
			return rtt;
		}
		public void setRtt(CarProducer rtt) {
			this.rtt = rtt;
		}

		public CarProducerHandler(CarProducer rtt) {
			this.rtt = rtt;
		};

		public class Stop extends AsyncEventHandler {
			public void handleAsyncEvent() {
				rtt.deschedulePeriodic();
			}
		}
		
		public class Start extends AsyncEventHandler {
			public void handleAsyncEvent() {
				rtt.schedulePeriodic();
			}
		}

		public Stop getStop() {
			return stop;
		}

		public void setStop(Stop stop) {
			this.stop = stop;
		}

		public Start getStart() {
			return start;
		}

		public void setStart(Start start) {
			this.start = start;
		}
	}
	
	
	public class CarProducerCensor extends RealtimeThread {
		private AsyncEvent stopEvt = new AsyncEvent();
		private AsyncEvent startEvt = new AsyncEvent();
		private CarProducer rtt;
		private boolean isPause = false;
		
		public CarProducerCensor(ReleaseParameters rp, CarProducer rtt) {
			super(null, rp);
			this.rtt = rtt;
			CarProducerHandler cph = new CarProducerHandler(rtt);
			stopEvt.addHandler(cph.getStop());
			startEvt.addHandler(cph.getStart());
			
			start();
		}

		public void run() {
			while (true) {
				if (!isPause) {
					if (rtt.isMax()) {
						stopEvt.fire();
						isPause = true;
					}
				}
				
				if (isPause) {
					if (rtt.isMin()) {
						startEvt.fire();
						isPause = false;
					}
				}
				waitForNextPeriod();
			}
		}
	}
}
