package com.sakamoto.templates;

import java.util.ArrayList;

import com.sakamoto.entities.Enemy;

public class Enemy_template {

	
	static char[] default_color = {'1','6','0','0','0','0','0','0'};
	static int default_life = 1;

	private int ID = 0;
	private char[] color;
	private int life;
	
	private  String colorMode = null;

	public int getID() {return this.ID;}
	public static Enemy Default(int x, int y) {
		return new Enemy(x,y,default_color,default_life,"STATIC");
	}
	
	public Enemy getEnemy(int x, int y) {
		return new Enemy(x,y,this.color,this.life,colorMode);
	}
	
	public Enemy_template(int id, int life, char[] hexColor, String colorMode) {
		this.ID = id;
		this.color = hexColor;
		this.life = life;
		this.colorMode = colorMode;
		
	}
	
}
