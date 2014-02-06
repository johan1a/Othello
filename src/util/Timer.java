package util;

public class Timer {
	private long startTime;
	private double timeLimit;
	private boolean timeOut = false;

	public void startTimer(double timeLimitSeconds) {
		startTime = System.currentTimeMillis();
		this.timeLimit = timeLimitSeconds;
		timeOut = false;
	}

	public boolean timedOut() {
		return timeOut ? true : startTime + 1000 * timeLimit < System
				.currentTimeMillis();
	}

	public double getSeconds() {
		return (System.currentTimeMillis() - startTime)/1000;
	}
}
