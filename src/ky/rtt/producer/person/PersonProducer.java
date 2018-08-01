package ky.rtt.producer.person;

import javax.realtime.RealtimeThread;
import javax.realtime.ReleaseParameters;

import ky.model.Components;
import ky.model.Person;
import ky.model.Road;
import ky.rtt.consumer.person.PersonConsumer;

public class PersonProducer extends RealtimeThread {
	private Components com;
	private Road road;
	private int personID;
	private boolean in;
	
	public PersonProducer(String name, ReleaseParameters rp, 
			Components com, Road road, boolean in) {
		super(null, rp);
		super.setName(name);
		this.com = com;
		this.road = road;
	}
	
	public void run() {
		while (true) {
			personID++;
			Person person = new Person();
			String name = super.getName() + "-PERSON-" + personID;
			person.setName(name);
			person.setForwarding(0);
			person.setCom(com);
			person.setRoad(road);
			System.out.println(name + " produced.");
			PersonConsumer pc = new PersonConsumer("PC-" + name, road.getPedestrian(), person);
//			PersonConsumerCensor censor = new PersonConsumerCensor(pc);
			pc.start();
//			censor.start();
			waitForNextPeriod();
		}
	}

	public Components getCom() {
		return com;
	}

	public void setCom(Components com) {
		this.com = com;
	}

	public Road getRoad() {
		return road;
	}

	public void setRoad(Road road) {
		this.road = road;
	}
}
