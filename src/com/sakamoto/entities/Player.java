package com.sakamoto.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.Random;

import com.sakamoto.main.Main;
import com.sakamoto.main.Vector2D;

public class Player extends Entity {

	public static final String RIGHT = "right";
	public static final String LEFT = "left";
	public static final String IDLE = "3";
	private String state = IDLE;
	
	private int width = 100;
	private int height = 25;
	private float maxSpeed= 3;
	private int score = 0;
	private int life = 5;
	private boolean isDead = false;
	
	public float combo = 1f;
	public float bestCombo = 1;
	
	
	
	public int getScore() {return this.score;}
	public int getWidth() {return this.width;}
	public int getHeight() {return this.height;}
	public String getState() {return this.state;}
	public boolean isDead() {return isDead;}
	
	

	public Player() {
		this.position = new Vector2D( Main.WIDTH / 2 - 50, Main.HEIGHT - height / 2);
		this.velocity = new Vector2D(new Random().nextBoolean()?-5:5,0);
		this.acceleration = new Vector2D(0,0);
		this.shape = new Rectangle((int)position.x, (int)position.y, (int) width, (int) height);
	}
	public void hit() {life--;}
	public void addScore(int value) {score += value*combo; combo+=0.1f;}
	
	
	public float getMomentum() {
	
		return this.velocity.x * 0.2f;
	}
	

	
	public void applyForce(Vector2D f) {
		this.acceleration.add(f);
	}

	public void changeState(String newState) {
		if (newState == RIGHT || newState == LEFT || newState == IDLE) {
			state = newState;
		}

	}

	
	public void move() {
		switch(state) {
			case RIGHT:
				this.applyForce(new Vector2D(3f,0));
				break;
			case LEFT:
				this.applyForce(new Vector2D(-3f,0));
				break;
			case IDLE:
				velocity.mult(0.95f);
				break;
				
		}
	}
	
	public void tick() {
		if(combo > bestCombo) {
			bestCombo = combo;
		}
		if (life <= 0) {
			isDead = true;
		}
		if(position.x+width >= Main.WIDTH-20) {
			velocity.mult(0);
			position.set(Main.WIDTH-width-20, position.y);
		}else if( position.x <= 20) {
			velocity.mult(0);
	
			position.set(20, position.y);
		}
		move();
		if(Math.abs(velocity.x) > maxSpeed) {
			velocity.x = velocity.x<0?-maxSpeed:maxSpeed;
		}
		
		
		
		
		velocity.add(acceleration);
		position.add(velocity);
		acceleration.mult(0);
		this.shape = new Rectangle((int) position.x, (int) position.y, (int) width, (int) height);
	}

	public void render(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;

		g2.setColor(new Color(225,225,225));
		g2.fill(shape);
	}
}
