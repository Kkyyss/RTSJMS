package ky.rtt.producer.person;

import javax.realtime.RealtimeThread;
import javax.realtime.ReleaseParameters;

import ky.Utils.MyUtils;
import ky.model.Components;
import ky.model.Person;
import ky.model.Road;
import ky.model.Traffic;
import ky.rtt.consumer.person.PersonConsumer;

public class PersonProducer extends RealtimeThread {
	private Components com;
	private Road road;
	private int personID;
	private boolean in;
	private Traffic tf;
	
	public PersonProducer(String name, ReleaseParameters rp, 
			Components com, Road road, Traffic tf, boolean in) {
		super(null, rp);
		super.setName(name);
		this.com = com;
		this.road = road;
		this.in = in;
		this.tf = tf;
	}
	
	public Traffic getTf() {
		return tf;
	}

	public void setTf(Traffic tf) {
		this.tf = tf;
	}

	public void run() {
		while (true) {
			personID++;
			Person person = new Person();
			String name = super.getName() + "_PERSON_" + personID;
			person.setName(name);
			person.setForwarding(0);
			person.setCom(com);
			person.setRoad(road);
			person.setTf(tf);
			MyUtils.log(tf.getIndex(), name + " produced.");
			if (in) {
				road.getPedestrian().getFirstHalfPerson().incrementAndGet();
			} else {
				road.getPedestrian().getSecondHalfPerson().incrementAndGet();
			}
			
			PersonConsumer pc = new PersonConsumer("PC-" + name, road.getPedestrian(), person, in);
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
