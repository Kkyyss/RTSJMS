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
	private boolean in;
	private boolean falldown = false;
	
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
	
	public boolean isFalldown() {
		return falldown;
	}

	public void setFalldown(boolean falldown) {
		this.falldown = falldown;
	}

	public boolean isIn() {
		return in;
	}

	public void setIn(boolean in) {
		this.in = in;
	}

	public void run() {
		while (true) {
			// tripping and falling
			if (!falldown) {
				falldown = MyUtils.getRandomEventOccur();
			}
			
			if (!falldown) {
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
					person.increaseForward(1);
				} else {
					if (in) {
						person.getRoad().getPedestrian().getFirstHalfFallDown().incrementAndGet();	
					} else {
						person.getRoad().getPedestrian().getSecondHalfFallDown().incrementAndGet();
					}
					
					System.out.println(person.getName() + " falling down...");
					start = new RelativeTime();
					period = new RelativeTime(5000, 0);
					rp = new PeriodicParameters(start, period);
					FallDownCensor fdc = new FallDownCensor(rp, this);
					fdc.start();
					period = new RelativeTime(5000, 0);
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
