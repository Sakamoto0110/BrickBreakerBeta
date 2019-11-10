package com.sakamoto.game;

import java.util.ArrayList;
import java.util.Random;

import com.sakamoto.entities.Enemy;
import com.sakamoto.fileIO.FileHandler;
import com.sakamoto.templates.Enemy_template;
import com.sakamoto.templates.Level_template;

public class Grid {

	private int sx = 22;
	private  ArrayList<Enemy[]>enemies;
	private ArrayList<String>layout;
	private int height = 0;
	private int nCols = 11;
	private int startRows = 0;
	public int maxRows = 0;
	private Level_template level = null;
	public boolean limit = false;
	private int timeBetweenNewRows;
	public boolean hasUpdate = true;
	public boolean initializing = true;
	
	public boolean endless = false;
	
	private int maxImDifferent = 4;
	public int imDifferent = 0;

	
	public int getHeight() {return this.height;}
	public ArrayList<Enemy[]>getEnemies(){return enemies;}
	public Enemy[] getRow(int y) {return enemies.get(y);}
	public Enemy getEnemyAt(int gx, int gy) {return enemies.get(gy)[gx];}
	public int getMaxTime() {return timeBetweenNewRows;}
	
	
	public void setLevel(Level_template level) {	
		this.enemies = new ArrayList<Enemy[]>();
		this.level = level;
		this.layout = level.gridLayout;
		this.maxRows = level.gridLayout.size();
		this.startRows = level.startRows;
		this.timeBetweenNewRows = level.timeBetweenNewRows;
		if(startRows > 0) {	
			addRow(startRows-1);
			
		}
	}
	
	public void forceAddRow() {
		hasUpdate = true;
		height++;
		shift(Enemy.height);
		Enemy[] row = new Enemy[nCols];
		for (int c = 0; c < nCols; c++) {
			int x = (int)(sx + c * Enemy.width + c * 1);
			int y = (int)(2 - Enemy.height + Enemy.height * 0.5f);
			Enemy_template e = FileHandler.getEnemyByID(1);			
			row[c] = FileHandler.getEnemyByID(1).getEnemy(x, y);
			

				
			
		}
		
		enemies.add(row);
	}
	
	
	public void addRow(int n) {
		if(endless  && height >= maxRows) {
			forceAddRow();
		}
		if (height  < maxRows && !limit) {
			hasUpdate = true;
			String layout = level.gridLayout.get(height);
			height++;
			shift(Enemy.height);
			Enemy[] row = new Enemy[nCols];
			for (int c = 0; c < nCols; c++) {
				int x = sx + c * Enemy.width + c * 2;
				int y = 2 - Enemy.height + Enemy.height * 1 ;
				int id = layout.charAt(c)-48;
				if(!(id >= 0 && id <= 9)) {
					id+=8;
				}if(id > 16)  { 
					
				}
				 
				//if(id != 0 && layout.charAt(c) != ' ') {
				if(id != 0 ) {
					if(new Random().nextFloat()<=0.0f && imDifferent < maxImDifferent) {
						row[c] = FileHandler.getEnemyByID(id).getEnemy(x, y);
						imDifferent++;
						row[c].imDifferent = true;
						
					}else {
						Enemy_template e = FileHandler.getEnemyByID(id);
						if(e != null) {
							row[c] = FileHandler.getEnemyByID(id).getEnemy(x, y);
						}	
					}
				}else {
					row[c] = null;
				}
				
			}
			enemies.add(row);
			if (n > 0) {
				addRow(n - 1);
			}
		}else {
			limit = true;
		}
		if(height >= startRows) {
			initializing = false;
		}
		
	}
	public void addCustomRow(int n, int[] layout) {

		hasUpdate = true;
		if (height + startRows + n < maxRows) {
			String rowLayout = this.layout.get(height);
			height++;
			shift(Enemy.height);
			Enemy[] row = new Enemy[nCols];

			for (int c = 0; c < nCols; c++) {
				int x = sx + c * Enemy.width + c * 2;
				int y = 2 - Enemy.height + Enemy.height * 1;
				if(rowLayout.charAt(c) != '0') {
					Enemy enemy = FileHandler.getEnemyByID(1).getEnemy(x, y);
					if(enemy != null) {
						row[c] = enemy;
					}else {
						row[c] = null;
					}
				}
			}
			enemies.add(row);
			if (n > 0) {
				addRow(n - 1);
			}
		}

	}
	public void reValidate() {
		for (int y = 0; y < enemies.size(); y++) {
			Enemy[] e = enemies.get(y);
			int n = 0; // null enemies
			int v = 0; // void enemies
			for (int x = 0; x < e.length; x++) {
				if (e[x] == null) {
					n++;
				}else if(e[x].getID()=="-1"){
					v++;
				}
			}
			if (n-y == enemies.get(y).length) {
				enemies.remove(v);
				height--;
			}
		}
	}

	public void shift(int n) {
		for (int y = 0; y < enemies.size(); y++) {
			Enemy[] row = enemies.get(y);
			for (int x = 0; x < row.length; x++) {
				Enemy e = row[x];
				if (e != null) {
					e.setYV(e.getYV() + n + 2);
				}
			}
		}
	}

	
}
