package ky.rtt.consumer.person;

import javax.realtime.PeriodicParameters;
import javax.realtime.RealtimeThread;
import javax.realtime.RelativeTime;
import javax.realtime.ReleaseParameters;

import ky.Utils.MyUtils;
import ky.model.Pedestrian;
import ky.model.Person;

public class PersonConsumer extends RealtimeThread {
	private Person person;
	private Pedestrian pedestrian;
	private RelativeTime start, period;
	private ReleaseParameters rp;
	private boolean isFallDown = false;
	
	public PersonConsumer(String name, Pedestrian pedestrian, Person person) {
		super();
		start = new RelativeTime(1000, 0);
		period = new RelativeTime(1000, 0);
		rp = new PeriodicParameters(start, period);
		setReleaseParameters(rp);
		super.setName(name);
		this.person = person;
		this.pedestrian = pedestrian;
	}
	
	public void run() {
		while (true) {
			// tripping and falling
			if (isFallDown) {
				isFallDown = false;
			}
			
			isFallDown = MyUtils.getRandomEventOccur();
			
			if (!isFallDown) {
				if (person.getForwarding() == pedestrian.getLength()) {
					pedestrian.decreasePerson();
					return;
				}
				period = new RelativeTime(1000, 0);
				rp = new PeriodicParameters(period);
				setReleaseParameters(rp);
				person.increaseForward(1);
			} else {
				isFallDown = true;
				period = new RelativeTime(3000, 0);
				rp = new PeriodicParameters(period);
				setReleaseParameters(rp);
			}
			waitForNextPeriod();
		}
	}

	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}
}
