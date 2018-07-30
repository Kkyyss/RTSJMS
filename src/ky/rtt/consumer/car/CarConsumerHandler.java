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
		
		if (car.getForwarding() == road.getLength()) {
			if (linkedTf == null) {
				cc.setLeave(true);
				return;
			}
			changeDirection(car, tf, road);
		}

		car.setForwarding(car.getForwarding() + 200);
	}
	
	
	
	private void changeDirection(Car car, Traffic tf, Road road) {
		Direction direction = MyUtils.getRandomDirections(road.getDirection());
		Road nextRoad = tf.getRoad(direction);
		Traffic linkedTf = nextRoad.getLinkedTraffic();
		
		if (linkedTf != null) {
			// Linked traffic
			// 1 - From any direction-left
			// 2 - From any direction-right
			car.setTf(nextRoad.getLinkedTraffic());
			car.setIn(true);
			switch(road.getDirection()) {
			case LEFT:
				car.setRoad(car.getTf().getLeftRoad());
				break;
			case RIGHT:
				car.setRoad(car.getTf().getRightRoad());
				break;
			default:
				System.out.println("Who on earth cast this magic...");
				break;
			}
		} else {
			// Not linked traffic
			// 1 - From any direction-direction
			car.setIn(false);
			car.setRoad(nextRoad);
		}
		car.setForwarding(0);
	}
}
