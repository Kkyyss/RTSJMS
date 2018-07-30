package ky.rtt.traffic;

import javax.realtime.AsyncEvent;
import javax.realtime.RealtimeThread;
import javax.realtime.ReleaseParameters;

import ky.model.Traffic;

	
public class TrafficRTT extends RealtimeThread {
	private Traffic tf;
	private AsyncEvent asyncEvt;

	public Traffic getTf() {
		return tf;
	}

	public void setTf(Traffic tf) {
		this.tf = tf;
	}

	public TrafficRTT(ReleaseParameters rp, Traffic tf) {
		super(null, rp);
		this.tf = tf;
	}

	public AsyncEvent getAsyncEvt() {
		return asyncEvt;
	}

	public void setAsyncEvt(AsyncEvent asyncEvt) {
		this.asyncEvt = asyncEvt;
	}

	public void run() {
		while (true) {
			asyncEvt.fire();	
			waitForNextPeriod();
		}
	}
}
