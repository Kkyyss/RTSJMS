package ky.rtt.consumer.car;

import java.util.concurrent.atomic.AtomicBoolean;

import javax.realtime.PeriodicParameters;
import javax.realtime.RealtimeThread;
import javax.realtime.RelativeTime;
import javax.realtime.ReleaseParameters;

import ky.Utils.MyUtils;
import ky.model.Car;
import ky.model.Components;
import ky.model.Direction;
import ky.model.Light;
import ky.model.Road;
import ky.model.Traffic;

public class CarConsumer extends RealtimeThread {
	private Car car;
	private Road road;
	private RelativeTime start, period;
	private ReleaseParameters rp;

	public CarConsumer(String name, Car car) {
		super();
		period = new RelativeTime(1000, 0);
		rp = new PeriodicParameters(period);
		setReleaseParameters(rp);
		super.setName(name);
		this.car = car;
	}
	
	public void run() {
		while (!car.isLeave()) {
			Components bj = car.getBj();
			road = car.getRoad();
			Traffic tf = car.getTf();
			Traffic linkedTf = road.getLinkedTraffic();
			
			// Car going out and not linked with others traffic
			if (car.getForwarding() == road.getLength()) {
				if (linkedTf == null && !car.getIn()) {
					System.out.println(car.getName() + " leave...");
					road.getTotalOutCars().decrementAndGet();
					car.setLeave(true);;
					return;
				}
			}
			
			switch (road.getLight()) {
			case GREEN:
				// Only traffic light turned into GREEN and change direction.
				if (road.getLight() == Light.GREEN) {
					if (car.getForwarding() == road.getLength()) {
						if (changeDirection(car, tf, road)) {
							road = car.getRoad();
							car.setName(car.getName() + " -> " + road.getName());
							car.increaseForward(1);
						}
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
	
	private void accidentMayOccur() {
		int carIdx = car.getForwarding();
		int frontCarIdx = car.getFrontCar().getForwarding();
		AtomicBoolean []arr= road.getAccidentArea();
		
		if (carIdx > 0 && carIdx + 1 == frontCarIdx) {
			if (arr[carIdx - 1].get() || arr[frontCarIdx - 1].get()) {
				return;
			}
			if (MyUtils.getRandomEventOccur()) {
				System.out.println("Oops...an acciddent happens in " + road.getName());
				arr[carIdx - 1].set(true);
				arr[frontCarIdx - 1].set(true);
				// Accident buff up to 8s
				start = new RelativeTime(8000, 0);
				period = new RelativeTime(1000, 0);
				rp = new PeriodicParameters(start, period);
				AccidentCensor ac1 = new AccidentCensor(rp, arr[carIdx - 1]);
				AccidentCensor ac2 = new AccidentCensor(rp, arr[frontCarIdx - 1]);
				ac1.start();
				ac2.start();
				
				car.setLeave(true);
				car.getFrontCar().setLeave(true);
				if (car.getIn()) {
					road.updateInCars(-2);
				} else {
					road.updateOutCars(-2);
				}
			}
		}
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
			accidentMayOccur();
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
		}
	}
	
	private void moveUntilFrontCarBehind() {
		if (car.getForwarding() + 1 < car.getFrontCar().getForwarding()) {
			car.increaseForward(1);
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
				switch(road.getDirection()) {
				case LEFT:
					car.setRoad(linkedTf.getLeftRoad());
					break;
				case RIGHT:
					car.setRoad(linkedTf.getRightRoad());
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
