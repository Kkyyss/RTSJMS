package ky.rtt.consumer.car;

import javax.realtime.AsyncEventHandler;

import ky.Utils.MyUtils;
import ky.model.Components;
import ky.model.Car;
import ky.model.Direction;
import ky.model.Light;
import ky.model.Road;
import ky.model.Traffic;

public class CarConsumerHandler extends AsyncEventHandler {
	private CarConsumer cc;
	
	public CarConsumerHandler(CarConsumer cc) {
		this.cc = cc;
	}
	
	public void handleAsyncEvent() {
		Car car = cc.getCar();
		Components bj = car.getBj();
		Road road = car.getRoad();
		Traffic tf = car.getTf();
		Traffic linkedTf = road.getLinkedTraffic();
		if (road.getLight() == Light.GREEN) {
			if (car.getForwarding() == road.getLength()) {
				if (linkedTf == null) {
					cc.setLeave(true);
					return;
				}
				changeDirection(car, tf, road);
				return;
			}
		}
		if (car.getFrontCar() != null && 
				car.getFrontCar().getRoad().getName().equals(road.getName())) {
			if (car.getForwarding() + 1 < car.getFrontCar().getForwarding()) {
				car.increaseForward(1);
			}				
		} else {
			if (car.getForwarding() + 1 < road.getLength()) {
				car.increaseForward(1);
			}
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
				try {
					nextRoad.getInCars().put(road.getInCars().take());
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
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
				try {
					car.getRoad().getOutCars().put(road.getOutCars().take());
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				return true;				
			}
		}
		return false;
	}
}
