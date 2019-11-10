package com.sakamoto.main;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.UIManager;

import com.sakamoto.entities.Player;
import com.sakamoto.fileIO.Logger;
import com.sakamoto.game.Game;
import com.sakamoto.ui.MainMenu;

public class Main extends Canvas implements Runnable, KeyListener, MouseListener, MouseMotionListener, MouseWheelListener {

	private static final long serialVersionUID = 1L;

	private enum GlobalState {
		game, mainMenu
	}

	// ** //
	public JFrame frame;
	public static int SCALE = 1;
	//723
	public static int WIDTH = 723;
	public static int HEIGHT = 825;

	public static float ZOOM = 1;
	private boolean isRunning;
	private Thread thread;
	public static int red = 225;
	public static int blue = 225;
	public static int green = 225;
	public static int alpha = 255;
	// ** //
	private BufferedImage background;
	//
	public static boolean isDebug = true;
	public static float mx, my;
	public static GlobalState gameState = GlobalState.game;
	//
	public Game game;
	public static Timer timer = new Timer();
	public static int spd = 1;
	public Main() {

		addKeyListener(this);
		addMouseMotionListener(this);
		addMouseListener(this);
		addMouseWheelListener(this);
		setPreferredSize(new Dimension((int) (WIDTH * SCALE), (int) (HEIGHT * SCALE)));
		isRunning = true;
		initFrame();
		new UIManager();
		background = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		//

		//
		game = new Game();

	}

	public void tick() {
		timer.tick();
		switch (gameState) {
			case game :
				if (!game.pause) {
					for(int c = 0; c < spd; c++)
					game.tick();
				}
				break;
			case mainMenu :
				
				break;
		}

	}

	public void render() {
		BufferStrategy bs = this.getBufferStrategy();
		if (bs == null) {
			this.createBufferStrategy(3);
			return;
		}
		Graphics g = background.getGraphics();
		Graphics2D g2 = (Graphics2D) g;
		g.setColor(new Color(red, green, blue, alpha));
		g.fillRect(0, 0, WIDTH, HEIGHT);
		// **
		
		switch (gameState) {
			case game :
				g.setColor(new Color(0,0,0));
				//g.drawString("Press \"1\" or \"2\" to control speed. Actual: " + Main.spd + " Max: 16", 20, Main.HEIGHT);
				//g.drawString("Press \"ESC\" to pause/unpause. ", 20, Main.HEIGHT-15);
				 
				game.render(g);
				
				
				
				break;
			case mainMenu :
				game.leftWall.render(g2);
				game.rightWall.render(g2);
				MainMenu.render(g);
				break;
		}
		

		// **
		g.dispose();
		g = bs.getDrawGraphics();
		g.drawImage(background, 0, 0, (int) (WIDTH * SCALE), (int) (HEIGHT * SCALE), null);
		bs.show();
	}

	public static void Background(int _r, int _g, int _b, int _a) {
		red = _r;
		green = _g;
		blue = _b;
		alpha = _a;
	}

	public static void Background(int _r, int _g, int _b) {
		red = _r;
		green = _g;
		blue = _b;

	}

	public static void Background(int _gray) {
		red = _gray;
		green = _gray;
		blue = _gray;
	}

	public static void Background(int _gray, int _a) {
		red = _gray;
		green = _gray;
		blue = _gray;
		alpha = _a;
	}

	// AVOID EDITING THIS SECTION!
	public static void main(String[] args) {
		Main game = new Main();
		game.start();
	}

	public void run() {
		long lastTime = System.nanoTime();
		double amoutOfTicks = 60.0;
		double ns = 1000000000 / amoutOfTicks;
		double delta = 0;
		int frames = 0;
		double timer = System.currentTimeMillis();
		while (isRunning) {
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			if (delta >= 1) {
				tick();
				render();
				Logger.run();
				frames++;
				delta--;
			}
			if (System.currentTimeMillis() - timer >= 1000) {
				// System.out.println("FPS: " + frames);
				frames = 0;
				timer += 1000;
			}
		}
		stop();
	}

	public synchronized void start() {
		thread = new Thread(this);
		isRunning = true;
		thread.start();
	}

	public synchronized void stop() {
		isRunning = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void initFrame() {

		frame = new JFrame("Bricks Engine DEMO.");
		frame.add(this);
		frame.setResizable(false);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
	// AVOID EDITING THIS SECTION!

	@Override
	public void mouseDragged(MouseEvent arg0) {

	}

	@Override
	public void mouseMoved(MouseEvent e) {
		mx = e.getX();
		my = e.getY();
	}

	// **************************
	@Override
	public void mouseClicked(MouseEvent e) {

	}

	@Override
	public void mouseEntered(MouseEvent arg0) {

	}

	@Override
	public void mouseExited(MouseEvent arg0) {

	}

	@Override
	public void mousePressed(MouseEvent e) {

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if(gameState == GlobalState.mainMenu ) {
			if(MainMenu.play.mouseHvr) {
				gameState = GlobalState.game;
			}			
		}
	}

	// **************************

	@Override
	public void mouseWheelMoved(MouseWheelEvent arg0) {

	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_A) {
			game.player.changeState(Player.LEFT);
		}
		if (e.getKeyCode() == KeyEvent.VK_D) {
			game.player.changeState(Player.RIGHT);
		}

	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_A) {
			if (game.player.getState() == Player.LEFT) {
				game.player.changeState(Player.IDLE);
			}
		} else if (e.getKeyCode() == KeyEvent.VK_D) {
			if (game.player.getState() == Player.RIGHT) {
				game.player.changeState(Player.IDLE);
			}
		} else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
			if (game.state == 0) {
				game.ball.locked = false;

			} else {
				game = new Game();
			}
		} else if (e.getKeyCode() == KeyEvent.VK_E) {
			// game.grid.shift(2);
		} else if (e.getKeyCode() == KeyEvent.VK_Q) {
			//game.grid.addRow(0);
			 

		} else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			game.pause = game.pause ? false : true;
		}else if(e.getKeyCode() == KeyEvent.VK_1) {
			if(spd > 1) {
				spd--;
			}
		}else if(e.getKeyCode() == KeyEvent.VK_2) {
			if(spd < 16) {
				spd++;
			}
		}
		if(e.getKeyCode() == KeyEvent.VK_TAB) {
			if(game.CONTROL == game.AUTO) {
				game.CONTROL = game.NORMAL;
			}else if(game.CONTROL == game.NORMAL) {
				game.CONTROL = game.AUTO;
			}
		}

	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

}
