package com.sakamoto.main;

public class Timer {

	public static double startTime = 0 ;
	public static double runningTime = 0;
	
	public static int ss = 0;
	public static int mm = 0;
	
	public Timer() {
		startTime = System.currentTimeMillis();
	}
	
	
	
	public void tick() {
		runningTime = System.currentTimeMillis() - startTime;
		if( runningTime > 1000.0f) {
			startTime =  System.currentTimeMillis();
			runningTime = 0;
			if(ss < 60) {
				ss++;
			}else {
				ss = 0;
				mm++;
			}
		}
	}
}
