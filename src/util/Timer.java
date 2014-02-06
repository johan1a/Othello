package util;


public class Timer {
	private long startTime;
	private double timeLimitSeconds;
	private boolean timeOut = false;

	public void startTimer(double timeLimitSeconds) {
		startTime = System.currentTimeMillis();
		this.timeLimitSeconds = timeLimitSeconds;
		timeOut = false;
	}

	public boolean timeOut() {
		return timeOut ? true : startTime + 1000 * timeLimitSeconds < System
				.currentTimeMillis();
	}

	public double getSeconds() {
		return System.currentTimeMillis() - (startTime + 1000 * timeLimitSeconds);
	}

}
