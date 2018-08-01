package ky.rtt.consumer.car;

import java.util.concurrent.atomic.AtomicBoolean;

import javax.realtime.RealtimeThread;
import javax.realtime.ReleaseParameters;

public class AccidentCensor extends RealtimeThread {
	private AtomicBoolean accident;
	
	public AccidentCensor(ReleaseParameters rp, AtomicBoolean accident) {
		super(null, rp);
		this.accident = accident;
	}
	
	public void run() {
		accident.set(false);
		System.out.println("Accident buff gone...");
	}
}
