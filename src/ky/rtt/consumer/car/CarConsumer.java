package ky.rtt.consumer.car;

import javax.realtime.PeriodicParameters;
import javax.realtime.RealtimeThread;
import javax.realtime.RelativeTime;
import javax.realtime.ReleaseParameters;

import ky.Utils.MyUtils;
import ky.model.Car;
import ky.model.Components;
import ky.model.Direction;
import ky.model.Road;
import ky.model.Traffic;

public class CarConsumer extends RealtimeThread {
	private Car car;
	private Road road;
	private RelativeTime start, period;
	private ReleaseParameters rp;
	private Components bj;
	private Traffic tf;

	public CarConsumer(String name, Car car) {
		super();
		period = new RelativeTime(1000, 0);
		rp = new PeriodicParameters(period);
		setReleaseParameters(rp);
		super.setName(name);
		this.car = car;
		this.bj = car.getBj();
		this.tf = car.getTf();
	}
	
	public void run() {
		while (!car.isLeave()) {
			road = car.getRoad();
			Traffic tf = car.getTf();
			Traffic linkedTf = road.getLinkedTraffic();
			
			// Car going out and not linked with others traffic
			if (car.getForwarding() == road.getLength()) {
				if (linkedTf == null && !car.getIn()) {
					MyUtils.log(tf.getIndex(), car.getName() + " [LEAVE]");
					road.getTotalOutCars().decrementAndGet();
					car.setLeave(true);
					return;
				}
			}
			
			switch (road.getLight()) {
			case GREEN:
				// Only traffic light turned into GREEN and change direction.
				if (car.getForwarding() == road.getLength()) {
					if (changeDirection(car, tf, road)) {
						road = car.getRoad();
						tf = car.getTf();
						// car.setName(car.getName() + " -> " + road.getName());
						car.increaseForward(1);
						MyUtils.log(tf.getIndex(), car.getName() + " -> " + car.getForwarding() + "/" + road.getLength());
					}
				}
				break;
			default:
				// Any cases
				if (car.getFrontCar() != null && !car.getFrontCar().isLeave() &&
						car.getFrontCar().getRoad().getName().equals(road.getName())) {
					// Car in front
					pedestrianAndCarInFront();
				} else {
					// RED/GREEN signal: fill in the blank
					pedestrianInFront();
				}
				break;
			}
			waitForNextPeriod();
		}
	}
	
	public Traffic getTf() {
		return tf;
	}

	public void setTf(Traffic tf) {
		this.tf = tf;
	}
	
	private void pedestrianAndCarInFront() {
		// Near pedestrian
		if (nearPedestrian()) {
			// No person and forward
				if (car.getIn() && road.getPedestrian().getFirstHalfPerson().get() <= 0) {
					moveUntilFrontCarBehind();			
				}
				if (!car.getIn() && road.getPedestrian().getSecondHalfPerson().get() <= 0) {
					moveUntilFrontCarBehind();					
				}
		} else {
			// RED/GREEN signal: move until behind the front car
			moveUntilFrontCarBehind();
		}
	}
	
	private void pedestrianInFront() {
		// Near pedestrian
		if (nearPedestrian()) {
			// No person and forward
			if (car.getIn() && road.getPedestrian().getFirstHalfPerson().get() <= 0) {
				moveUntilTheEnd();			
			}
			if (!car.getIn() && road.getPedestrian().getSecondHalfPerson().get() <= 0) {
				moveUntilTheEnd();					
			}
		} else {
			// RED/GREEN signal: move until the end of the road
			moveUntilTheEnd();
		}		
	}
	
	private void moveUntilTheEnd() {
		if (car.getForwarding() + 1 <= road.getLength()) {
			car.increaseForward(1);
			MyUtils.log(tf.getIndex(), car.getName() + " -> " + car.getForwarding() + "/" + road.getLength());
		}
	}
	
	private void moveUntilFrontCarBehind() {
		if (car.getForwarding() + 1 < car.getFrontCar().getForwarding()) {
			car.increaseForward(1);
			MyUtils.log(tf.getIndex(), car.getName() + " -> " + car.getForwarding() + "/" + road.getLength());
		}		
	}
	
	private boolean changeDirection(Car car, Traffic tf, Road road) {
		Direction direction = MyUtils.getRandomDirections(road.getDirection());
		Road nextRoad = tf.getRoad(direction);
		Traffic linkedTf = nextRoad.getLinkedTraffic();
		
		if (linkedTf != null) {
			// Linked traffic
			// 1 - From any direction-left
			// 2 - From any direction-right
			if (!nextRoad.isInCarsMax()) {
				switch(direction) {
				case LEFT:
					car.setRoad(linkedTf.getRightRoad());
					break;
				case RIGHT:
					car.setRoad(linkedTf.getLeftRoad());
					break;
				default:
					System.out.println("Who on earth cast this magic...");
					break;
				}
				car.setIn(true);
				car.setTf(linkedTf);
				car.setForwarding(0);
				car.setFrontCar(nextRoad.getLatestCar());
				nextRoad.setLatestCar(car);
				
				road.getTotalInCars().decrementAndGet();
				car.getRoad().getTotalInCars().incrementAndGet();
				return true;
			}
		} else {
			// Not linked traffic
			// 1 - From any direction-direction
			if (!nextRoad.isOutCarsMax()) {
				car.setIn(false);
				car.setRoad(nextRoad);
				car.setForwarding(0);
				car.setFrontCar(nextRoad.getLatestCar());
				nextRoad.setLatestCar(car);
				
				road.getTotalInCars().decrementAndGet();
				car.getRoad().getTotalOutCars().incrementAndGet();
				return true;				
			}
		}
		return false;
	}

	public Car getCar() {
		return car;
	}

	public void setCar(Car car) {
		this.car = car;
	}
	
	public boolean nearCondo() {
		if (road.getCondominium() != null) {
			if (car.getIn()) {
				switch (road.getDirection()) {
				case LEFT:
				case TOP:
				case RIGHT:
				case DOWN:
					return 
							car.getForwarding() + 1 >= road.getCondominium().getStartIdx() &&
							car.getForwarding() + 1 <= road.getCondominium().getEndIdx();
					default:
						System.out.println("Who on earth cast this magic...");
						break;
				}
			} else {
				switch (road.getDirection()) {
				case LEFT:
				case TOP:
				case RIGHT:
				case DOWN:
					int startIdx = road.getLength() - road.getCondominium().getStartIdx() + 1;
					int endIdx = road.getLength() - road.getCondominium().getEndIdx() + 1;
					return car.getForwarding() + 1 >= startIdx
							&& car.getForwarding() + 1 <= endIdx;
					default:
						System.out.println("Who on earth cast this magic...");
						break;
				}			
			}			
		}

		return false;
	}
	public boolean nearSchool() {
		if (road.getSchool() != null) {
			if (car.getIn()) {
				switch (road.getDirection()) {
				case LEFT:
				case TOP:
				case RIGHT:
				case DOWN:
					return 
							car.getForwarding() + 1 >= road.getSchool().getStartIdx() &&
							car.getForwarding() + 1 <= road.getSchool().getEndIdx();
					default:
						System.out.println("Who on earth cast this magic...");
						break;
				}
			} else {
				switch (road.getDirection()) {
				case LEFT:
				case TOP:
				case RIGHT:
				case DOWN:
					int startIdx = road.getLength() - road.getSchool().getStartIdx() + 1;
					int endIdx = road.getLength() - road.getSchool().getEndIdx() + 1;
					return car.getForwarding() + 1 >= startIdx
							&& car.getForwarding() + 1 <= endIdx;
					default:
						System.out.println("Who on earth cast this magic...");
						break;
				}			
			}			
		}

		return false;
	}
	private boolean nearPedestrian() {
		if (road.getPedestrian() != null) {
			if (car.getIn()) {
				switch (road.getDirection()) {
				case LEFT:
				case TOP:
				case RIGHT:
				case DOWN:
					return car.getForwarding() == road.getPedestrian().getIndex();
					default:
						System.out.println("Who on earth cast this magic...");
						break;
				}			
			} else {
				switch (road.getDirection()) {
				case LEFT:
				case TOP:
				case RIGHT:
				case DOWN:
					int index = road.getLength() - road.getPedestrian().getIndex() + 1;
					return car.getForwarding() == index;
					default:
						System.out.println("Who on earth cast this magic...");
						break;
				}	
			}			
		}
			
		return false;
	}
}
