package com.sakamoto.fileIO;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Logger {
	
	private static ArrayList<String> logBuffer = new ArrayList<String>();
	private static int timer = 0;
	public Logger() {
		
	}
	
	public static void log(String data) {
		logBuffer.add(data);
	}
	
	public static void run() {
		timer++;
		if(timer > 1000) {
			System.out.println("Logging...");
			String result = null;
			timer = 0;
			try {
				result = write("Logs/log.txt");
				System.out.println(result);
			} catch (IOException e) {
				System.out.println(result);
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	private static String write(String path) throws IOException {
		int n = 0;
		File file = new File(path);
		FileWriter fw = new FileWriter(file);
		for(int c = 0; c < logBuffer.size(); c++) {
			String data = logBuffer.get(c);		
			//fw.write(data + "\n");
			fw.append(data + "\n");
			n++;
		}
		fw.close();
		if(n == logBuffer.size()) {
			logBuffer = new ArrayList<String>();
			return new String("Done.");
		}else {
			logBuffer = new ArrayList<String>();
			return new String("Oups, something went wrong during logging.");
		}
	}
	

}
