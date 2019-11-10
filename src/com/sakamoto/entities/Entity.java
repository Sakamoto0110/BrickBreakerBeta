package com.sakamoto.entities;

import java.awt.Graphics;
import java.awt.Shape;
import java.awt.image.BufferedImage;

import com.sakamoto.main.Vector2D;

public class Entity {

	protected int width;
	protected int height;
	protected int radius;
	protected Shape shape;
	protected String ID;

	protected Vector2D position;
	protected Vector2D velocity;
	protected Vector2D acceleration;
	
	public Vector2D getPosV() {return this.position;}
	public Vector2D getAccV() {return this.acceleration;}
	public Vector2D getVelV() {return this.velocity;}
	
	public float getXV() {return this.position.x;}
	public float getYV() {return this.position.y;}
	public void setXV(float n) {this.position.x = n; }
	public void setYV(float n) {this.position.y = n; }
	
	public String getID() {return this.ID;}
		
	
	public int getWidth() {	return this.width;}
	public int getHeight() { return this.height;}
	public int getRadius() { return this.radius;}
	
	public Shape getShape() { return this.shape;}
	
	
	
	
	public Entity() {
		
	}

	
	public boolean Collide(Entity other) {
		return this.getShape().getBounds().intersects(other.getShape().getBounds());
		//return this.getShape().intersects((Rectangle)other.getShape());
	}
	public boolean Collide(Shape other) {
		return this.getShape().getBounds().intersects(other.getBounds());
		//return this.getShape().intersects((Rectangle)other);
	}
	public void tick() {
		
	}
	public  void render(Graphics g) {
		
	}
	
}
