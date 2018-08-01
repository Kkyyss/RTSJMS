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
	private boolean in;
	
	public PersonConsumer(String name, Pedestrian pedestrian, Person person, Boolean in) {
		super();
		start = new RelativeTime(1000, 0);
		period = new RelativeTime(1000, 0);
		rp = new PeriodicParameters(start, period);
		setReleaseParameters(rp);
		super.setName(name);
		this.person = person;
		this.pedestrian = pedestrian;
		this.in = in;
	}
	
	public void run() {
		while (true) {
			// tripping and falling
			if (person.getForwarding() == 0) {
				person.increaseForward(1);
			} else {
				if (isFallDown) {
					isFallDown = false;
				}
				
				isFallDown = MyUtils.getRandomEventOccur();
				
				if (!isFallDown) {
					if (person.getForwarding() == pedestrian.getLength()) {
						if (in) {
							pedestrian.getFirstHalfPerson().decrementAndGet();
						} else {
							pedestrian.getSecondHalfPerson().decrementAndGet();
						}
						System.out.println(person.getName() + " leave...");
						return;
					}
					
					if (person.getForwarding() == pedestrian.getLength() - 1) {
						if (in) {
							pedestrian.getFirstHalfPerson().decrementAndGet();
							pedestrian.getSecondHalfPerson().incrementAndGet();
						} else {
							pedestrian.getSecondHalfPerson().decrementAndGet();
							pedestrian.getFirstHalfPerson().incrementAndGet();
						}						
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
