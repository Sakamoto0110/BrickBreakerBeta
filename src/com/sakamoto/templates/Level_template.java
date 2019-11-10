package com.sakamoto.templates;

import java.util.ArrayList;

public class Level_template {

	
	public int ID = -1;
	public String tag = null;
	public ArrayList<String> gridLayout = new ArrayList<String>();
	public int[] rowLayout = new int[11];
	public int nCols = 11;
	public int maxRows;
	public int startRows;
	public int timeBetweenNewRows;
	
	public boolean a = true;
	
	
	
	public int getID() {return this.ID;}
	public Level_template(int id, String tag, int startRows,int maxRows,int timeBetweenNewRows, ArrayList<String> layout) {
		this.ID = id;
		this.tag = tag;
		this.startRows = startRows;
		this.maxRows = maxRows;
		this.gridLayout = layout;
		this.timeBetweenNewRows = timeBetweenNewRows;
		if(a) {
			a= false;
			show();
			
		}
	}
	public Level_template() {
		
	}
	public void show() {
		for(int c = 0; c < gridLayout.size(); c++) {
			//System.out.println(gridLayout.get(c));
		}
		//System.out.println("\n");
	}
}
