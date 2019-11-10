package com.sakamoto.game;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;

import com.sakamoto.entities.Ball;
import com.sakamoto.entities.Enemy;
import com.sakamoto.entities.Player;
import com.sakamoto.fileIO.FileHandler;
import com.sakamoto.fileIO.Logger;
import com.sakamoto.main.Main;
import com.sakamoto.main.Utils;
import com.sakamoto.main.Vector2D;

public class Game {

	public Player player;
	public Ball ball;


	public Grid grid = new Grid();
	
	public static int centerOffset = (Main.WIDTH-Enemy.width*11+24)/4;
	
	public Wall leftWall = new Wall(0, 0, centerOffset-1 , Main.HEIGHT);
	public Wall rightWall = new Wall(Main.WIDTH - centerOffset+1, 0, centerOffset, Main.HEIGHT);
	private Wall celling = new Wall(0, -20, Main.WIDTH, 20);

	private int s = 0;
	private int s1 = 0;
	private int maxS = 80;

	public int state = 0;

	public final String MOUSE = "01";
	public final String AUTO = "02";
	public  final String NORMAL = "03";
	private  final String MOUSE_BALL = "04";
	public String CONTROL = NORMAL;
	public boolean pause = false;
	
	
	public static FileHandler fileHandler = new FileHandler();

	public static Logger log;
	
	private int level = 1;
	public Game() {
		ball = new Ball(300, 200);
		player = new Player();
		Main.Background(51);
		init();
		
	}
	
	public void init() {
		grid = new Grid();
		grid.setLevel(FileHandler.getLevelByID(level));
		maxS = grid.getMaxTime();
		ball.setXV(player.getXV()+player.getWidth()/2);
		ball.setYV(player.getYV());
		
		
	}

	public void specialMove(String ctrl) {
		
		if (ctrl == MOUSE) {
			if (Main.mx / Main.SCALE + player.getWidth() / 2 <= Main.WIDTH && Main.mx / Main.SCALE - player.getWidth() / 2 >= 0) {
				player.setXV((int) (Main.mx / Main.SCALE - player.getWidth() / 2));
				
			}
		} 
		if (ctrl == AUTO) {
			if (ball.getXV() - ball.getRadius() + player.getWidth() / 2 <= Main.WIDTH && ball.getXV() - player.getWidth() / 2 >= 0) {
				player.setXV(ball.getXV() - player.getWidth() / 2);
			}
		}
		if(ctrl == MOUSE_BALL) {
			ball.setXV(Main.mx);
			ball.setYV(Main.my);
		}
	}
	
	
	public void tick() {
		if (ball.locked) {
			ball.setXV(player.getXV() + player.getWidth() / 2 - ball.getRadius() / 2);
			ball.setYV(player.getYV() - ball.getRadius());
			ball.getVelV().x = player.getVelV().x*0.9f;
			if(CONTROL == AUTO) {
				ball.locked = false;
			}
		} else {
			
			specialMove(CONTROL);
		}
		
		
	
		// ENEMIES
		for (int y = 0; y < grid.getEnemies().size(); y++) {
			Enemy[] row = grid.getRow(y);
			for (int x = 0; x < row.length; x++) {
				if (row != null) {
					Enemy enemy = row[x];
					if (enemy != null) {
						if (enemy.isDead) {							
							player.addScore(enemy.getValue());
							enemy = null;
							row[x] = null;
						} else {
							if (enemy.Collide(ball)&&enemy.getID()!="-1"&&!ball.hitten) {
								int xxx = (int) (ball.getXV()+ball.getRadius()/2-ball.getVelV().x-(enemy.getXV()+Enemy.width/2));
								if(Math.abs(xxx) > 30) {
									ball.collide("vertical");
								}else {
									ball.collide("horizontal");									
								}
								player.combo+=0.5f;
								ball.hitten=true;
								enemy.hit();
								if(enemy.imDifferent) {
									player.addScore(enemy.explode(grid.getEnemies(), 2)*level);
									grid.imDifferent--;
								}
							}
							enemy.tick();
						}
					}
				}
			}
		}
		ball.tick();
		
		
		// PLAYER COLLISION
		if (player.Collide(ball)) {
			ball.setYV(player.getYV()-player.getHeight()/2); 
			ball.collide("horizontal");
			ball.addMomentum(player.getMomentum());
			player.combo=1;
		}
		
		player.tick();

		// LATERAL WALL COLLISIONS
		if (ball.Collide(leftWall.getShape()) || ball.Collide(rightWall.getShape())) {
			ball.setXV( ball.Collide(leftWall.getShape())?20+1:Main.WIDTH-20-ball.getRadius());
			ball.collide("vertical");
		}
		// CELLING COLLISION
		if (ball.Collide(celling.getShape())) {
			ball.setYV(0);
			ball.collide("horizontal");
			
		}

		// PLAYER HIT
		if (ball.getYV() + ball.getRadius() > Main.HEIGHT) {
			ball = new Ball((int) (player.getXV() + player.getWidth() / 2 - ball.getRadius() / 2), (int) player.getYV());
			ball.locked = true;
			player.hit();
			player.combo=1;
			
		}
		if(player.isDead()) {
			this.state = 1;
		}
		
		
		s++;	
		
		if (s > maxS) {
			s = 0;
			grid.reValidate();
			grid.addRow(0);
				
		}
		
		if(grid.getHeight()==0) {
			if(level < 3) {
				level++;
				init();
			}else {
				grid.forceAddRow();
			}
		}
		
		
	}
	

	public void render(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		if (state == 0) {
			ball.render(g);
			player.render(g);
			
			for (int y = 0; y < grid.getEnemies().size(); y++) {
				Enemy[] row = grid.getRow(y);
				for (int x = 0; x < row.length; x++) {
					Enemy enemy = row[x];
					if (enemy != null) {
						if (!enemy.isDead) {
							enemy.render(g);
						}
					}
				}
			}
			

		} else if (state == 1) {
			// GAME OVER - PLAYER DIE
			Font font = new Font("TimesRoman", Font.BOLD, 20);
			g.setFont(font);
			g.setColor(new Color(225, 225, 225,100));
			int py = 40;
			g.drawString("GAME OVER", Main.WIDTH / 2 - 65, py);
			String strScore = "Score:   " + Integer.toString(player.getScore());
			g.drawString(strScore, Main.WIDTH / 2 - 65, py + 15);
			g.drawString("Press spacebar to restart", Main.WIDTH / 2 - 105, py + 45);
		}

		leftWall.render(g2);
		rightWall.render(g2);
		celling.render(g2);
		String score = new String(Integer.toString(player.getScore()));
		String combo = new String(Float.toString(player.combo)).substring(0,3);
		Font font = new Font("TimesRoman", Font.BOLD, 15);
		g.setFont(font);

		String bestCombo = new String(Float.toString(player.bestCombo)).substring(0,3);
		g2.drawString("score: " + score, 20, Main.HEIGHT);
		g2.drawString("combo: " + combo, 20, Main.HEIGHT-12);
		g2.drawString("best combo: " + bestCombo, 20, Main.HEIGHT-24);
	}

	public class Wall {
		private Shape shape;
		private int red = 0;
		private int blue = 0;
		private int green = 0;

		public Shape getShape() {
			return this.shape;
		}
		public Wall(int x, int y, int width, int height) {
			this.shape = new Rectangle(x, y, width, height);
		}

		public void render(Graphics2D g2) {
			g2.setColor(new Color(red, blue, green));
			g2.fill(shape);

		}

	}

}
