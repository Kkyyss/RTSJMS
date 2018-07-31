package ky.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import ky.model.Direction;

public class MyUtils {
	public static Direction getRandomDirections(Direction selfDirection) {
		List<Direction> d = new ArrayList<>();
		for (Direction direction: Direction.values()) {
			if (direction != selfDirection) {
				d.add(direction);
			}
		}
		
		Random rand = new Random();
		return d.get(rand.nextInt(d.size()));
	}
}
