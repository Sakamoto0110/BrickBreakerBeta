package com.sakamoto.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.util.ArrayList;

import com.sakamoto.main.Utils;
import com.sakamoto.main.Vector2D;


public class Ball extends Entity {

	private int radius = 10;

	public boolean hitten = false;
	
	public boolean locked = true;
	private float maxForce = 5;

	private ArrayList<Float> tailX;
	private ArrayList<Float> tailY;
	private float tailSize = 40;

	public static final String HORIZONTAL=  "horizontal";
	public static final String VERTICAL = "vertical";
	
	public Ball(int x, int y) {
		tailX = new ArrayList<Float>();
		tailY = new ArrayList<Float>();
		shape = new Rectangle((int) x, (int) y, (int) radius, (int) radius);
		this.position = new Vector2D(x,y);
		velocity = new Vector2D(0,-6);
		acceleration = new Vector2D(0,0);

	}


	public void collide(String type) {
		switch(type) {
			case HORIZONTAL:
				
				//position.y-=velocity.y;
				velocity.y*=-1;
				break;
			case VERTICAL:
				//position.x-=velocity.x;
				velocity.x*=-1;
				break;
		}
		
	}
	
	public int getRadius() {
		return this.radius;
	}

	public void addMomentum(float n) {
		if(Math.abs(velocity.x) < maxForce) {
			velocity.x+=n;
		}

	}
	public void applyForce(Vector2D f) {
		this.acceleration.add(f);
	}
	

	public void tick() {	
		if(!locked) {
			// PHYSICS ROLLBACK
			if(hitten) {
				position.x = tailX.get(tailX.size()-1);
				position.y = tailY.get(tailY.size()-1);
				hitten = false;
			}
			
			// ACTUAL PHYSICS
			velocity.add(acceleration);
			position.add(velocity);
			//acceleration.mult(0);
			
			// TAIL
			tailX.add(this.getXV());
			tailY.add(this.getYV());
			if (tailX.size() > tailSize && tailY.size() > tailSize) {
				tailX.remove(0);
				tailY.remove(0);
			}			
		}
		
		
		this.shape = new Rectangle((int)position.x, (int) position.y, (int) radius, (int) radius);
	}

	public void render(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setColor(new Color(225, 225, 225));
		g2.fillOval((int) position.x, (int) position.y, (int) radius, (int) radius);
		for (int c = tailX.size() - 1; c >= 0; c--) {
			float x = tailX.get(c);
			float y = tailY.get(c);
			g2.setColor(new Color(225, 225, 225,c*2));
			double r =  Utils.mapRange(c, 0, tailSize,0, this.radius);
			g2.fillOval((int)(x), (int) y,this.radius, this.radius);		
		}
	}

}
