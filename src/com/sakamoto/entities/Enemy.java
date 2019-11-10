package com.sakamoto.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

import com.sakamoto.fileIO.FileHandler;
import com.sakamoto.main.Main;
import com.sakamoto.main.Utils;
import com.sakamoto.main.Vector2D;

public class Enemy extends Entity {

	public static int width = 60;
	public static int height = 20;
	private int life = 0;
	public int maxLife;
	public boolean isDead = false;
	
	// *** Colors
	private int maxRed = 255;
	private int maxGreen = 0;
	private int maxBlue = 0;
	
	private float roff = 0;
	private float goff = 0;
	private float boff = 0;
	
	private int red = 0;
	private int green = 0;
	private int blue = 0;
	private int alpha;

	public final String STATIC = "STATIC";
	public final String PULSE = "PULSE";
	public final String RAINBOW = "RAINBOW";
	private String colorMode = PULSE;
	// ***
	
	private BufferedImage whiteMask = null;
	
	public boolean imDifferent = false;

	public int getLife() {return this.life;	}
	public int getValue() {return this.maxLife*10;}
	public void hit() {this.life--;}
	
	
	

	public Enemy(int x, int y, char[] hexColor, int life,String colorMode) {
	
		this.position = new Vector2D(x,y);
		this.life = life;
		this.maxLife = this.life;
		this.colorMode = colorMode;
		this.shape = new Rectangle((int) x, (int) y, width, height);	
		this.whiteMask = FileHandler.whiteMask;
		hex(hexColor);		
	}

	
	public void hex(char[] hex) {
		char[] rh = new char[2];
		int temp1 = 0,temp2 = 0;
		rh[0] = hex[0]>=48&&hex[0]<=57?hex[0]:hex[0]>=65&&hex[0]<=70?hex[0]:0;
		rh[1] = hex[1]>=48&&hex[1]<=57?hex[1]:hex[1]>=65&&hex[1]<=70?hex[1]:0;
		temp1 = rh[0] >= 65 && rh[0] <= 70?10+rh[0]-65:rh[0]-48;
		temp2 = rh[1] >= 65 && rh[1] <= 70?10+rh[1]-65:rh[1]-48;
		maxRed = (temp1*16)+(temp2);
		
		
		char[] gh = new char[2];
		gh[0] = hex[2]>=48&&hex[2]<=57?hex[2]:hex[2]>=65&&hex[2]<=70?hex[2]:0;
		gh[1] = hex[3]>=48&&hex[3]<=57?hex[3]:hex[3]>=65&&hex[3]<=70?hex[3]:0;
		temp1 = gh[0] >= 65 && gh[0] <= 70?10+gh[0]-65:gh[0]-48;
		temp2 = gh[1] >= 65 && gh[1] <= 70?10+gh[1]-65:gh[1]-48;
		maxGreen = (temp1*16)+(temp2);
		
		
		char[] bh = new char[2];
		bh[0] = hex[4]>=48&&hex[4]<=57?hex[4]:hex[4]>=65&&hex[4]<=70?hex[4]:0;
		bh[1] = hex[5]>=48&&hex[5]<=57?hex[5]:hex[5]>=65&&hex[5]<=70?hex[5]:0;
		temp1 = bh[0] >= 65 && bh[0] <= 70?10+bh[0]-65:bh[0]-48;
		temp2 = bh[1] >= 65 && bh[1] <= 70?10+bh[1]-65:bh[1]-48;
		maxBlue = (temp1*16)+(temp2);
		
		char[] ah = new char[2];
		ah[0] = hex[6]>=48&&hex[6]<=57?hex[6]:hex[6]>=65&&hex[6]<=70?hex[6]:0;
		ah[1] = hex[7]>=48&&hex[7]<=57?hex[7]:hex[7]>=65&&hex[7]<=70?hex[7]:0;
		temp1 = ah[0] >= 65 && ah[0] <= 70?10+ah[0]-65:ah[0]-48;
		temp2 = ah[1] >= 65 && ah[1] <= 70?10+ah[1]-65:ah[1]-48;
		alpha = (temp1*16)+(temp2);
		
		//System.out.println(red + " " + green + " " + blue + " " + alpha);
	}

	
	public void colorBehaivior(String mode) {
		switch(mode) {
			case STATIC :
				red = maxRed;
				green = maxGreen;
				blue = maxBlue;
				break;
			case PULSE:
				int rate = 50;
				red = (int)Utils.mapRange(Math.sin(roff), -1, 1, maxRed-rate , maxRed);
				green = (int)Utils.mapRange(Math.sin(goff), -1, 1,maxGreen-rate >rate+1?maxGreen-rate:0, maxGreen);
				blue = (int)Utils.mapRange(Math.sin(boff), -1, 1, maxBlue-rate >rate+1?maxBlue-rate:0, maxBlue);
				roff+=0.04f;
				goff+=0.04f;
				boff+=0.04f;
				break;
			case RAINBOW:
				red = (int)Utils.mapRange(Math.sin(roff), -1, 1, 125, 253);
				green = (int)Utils.mapRange(Math.sin(goff), -1, 1, 125, 253);
				blue = (int)Utils.mapRange(Math.sin(boff), -1, 1, 125, 253);
				
				roff+=0.04f;
				goff+=0.06f;
				boff+=0.09f;
				break;
			default :
				break;
			
			
		}
	}

	
	public int explode(ArrayList<Enemy[]> others, float force) {
		int score = 0;
		for(int c = 0; c < others.size(); c++) {
			Enemy[] row = others.get(c);
			float ox = 0;
			float oy = 0;
			float dist = 0;
			for(int c1 = 0; c1 < row.length-1; c1++) {
				Enemy enemy = row[c1];
				if(enemy!=null) {
					ox = (enemy.getXV())-(this.getXV());
					oy = (enemy.getYV())-(this.getYV());
					dist = (float)Math.sqrt(ox*ox+oy*oy);
					if(dist <= 70) {
						score+=row[c1].getValue()*10;
						row[c1] = null;	
						others.remove(c);
						others.add(c, row);
						if(force > 1) {
							if(new Random().nextFloat()<0.05f) {
								score+=enemy.explode(others, force--);
							}
						}
					}		
				}
			}
		}
		return score;
	}
	
	
	public void tick() {
		if(this.imDifferent) {
			colorMode = RAINBOW;
		}
		if(colorMode != null) {
			colorBehaivior(colorMode);
		}
		if (life <= 0 || position.y>Main.HEIGHT) {
			isDead = true;
		}
		this.shape = new Rectangle((int)position.x , (int)position.y , width , height);
	}

	public void render(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		int a = (int) Utils.mapRange(life, 0, maxLife, 50, 200);
		
	
		
		// white mask
		g2.setColor(new Color(255,255,255));
		g2.drawImage(whiteMask, (int)position.x, (int)position.y, 60, 20, null);
		// dark mask
		Rectangle mask = (Rectangle)shape;
		mask.width = shape.getBounds().width-3;
		mask.x = shape.getBounds().x+3;
		mask.height = shape.getBounds().height-3;
		mask.y = shape.getBounds().y+3;
		g2.setColor(Color.black);
		g2.fill(mask);
		// color // color
		g2.setColor(new Color(red, green, blue,a));
		g2.fill((Rectangle) shape);
		

	}
	

}
