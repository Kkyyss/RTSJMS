package ky.rtt.traffic;
import javax.realtime.AsyncEventHandler;


public class TrafficHandler extends AsyncEventHandler {
	TrafficRTT rtt;
	public TrafficHandler(TrafficRTT rtt) {
		this.rtt = rtt;
	}
	
	public void handleAsyncEvent() {

		
	}
}
