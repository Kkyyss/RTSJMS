package ky.rtt.consumer.car;

import javax.realtime.AsyncEvent;
import javax.realtime.RealtimeThread;
import javax.realtime.ReleaseParameters;

import ky.model.Car;

public class CarConsumer extends RealtimeThread {
	private Car car;
	private boolean leave = false;
	private AsyncEvent asyncEvt;
	
	public AsyncEvent getAsyncEvt() {
		return asyncEvt;
	}

	public void setAsyncEvt(AsyncEvent asyncEvt) {
		this.asyncEvt = asyncEvt;
	}

	public CarConsumer(String name, ReleaseParameters rp, Car car) {
		super(null, rp);
		super.setName(name);
		this.car = car;
	}
	
	public void handleAsyncEvent() {
		while (!leave) {
			asyncEvt.fire();
			waitForNextPeriod();
		}
	}

	public boolean isLeave() {
		return leave;
	}

	public void setLeave(boolean leave) {
		this.leave = leave;
	}

	public Car getCar() {
		return car;
	}

	public void setCar(Car car) {
		this.car = car;
	}
}
