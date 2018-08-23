package ky.rtt.consumer.road;

import javax.realtime.AsynchronouslyInterruptedException;
import javax.realtime.Interruptible;
import javax.realtime.RealtimeThread;
import javax.realtime.ReleaseParameters;

public class RoadInterruptible extends RealtimeThread implements Interruptible {
	private AsynchronouslyInterruptedException aie;
	
	public RoadInterruptible(ReleaseParameters rp, AsynchronouslyInterruptedException aie) {
		super(null, rp);
		this.aie = aie;
	}
	
	public void run() {
		aie.doInterruptible(this);
	}

	@Override
	public void interruptAction(AsynchronouslyInterruptedException exception) {
		
	}

	@Override
	public void run(AsynchronouslyInterruptedException exception) throws AsynchronouslyInterruptedException {
		
	}
}
