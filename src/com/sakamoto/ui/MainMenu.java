package com.sakamoto.ui;

import java.awt.Color;
import java.awt.Graphics;

import com.sakamoto.main.Main;

public class MainMenu {

	private final static int btn_width = 240;
	private final static int btn_height = 60;
	private final static int spacing = 25;

	public static Button play = new Button(Main.WIDTH / 2 - btn_width / 2, Main.HEIGHT / 3, btn_width, btn_height);
	public static Button options = new Button(Main.WIDTH / 2 - btn_width / 2, Main.HEIGHT / 3 + btn_height + 1 * spacing, btn_width, btn_height);
	public static Button quit = new Button(Main.WIDTH / 2 - btn_width / 2, Main.HEIGHT / 3 + 2 * btn_height + 2 * spacing, btn_width, btn_height);
	public static void render(Graphics g) {
		play.render(g);
		options.render(g);
		quit.render(g);
	}

	public static class Button {
		public int x, y;
		public int width, height;
		public boolean mouseHvr = false;
		public Button(int x, int y, int width, int height) {
			this.x = x;
			this.y = y;
			this.width = width;
			this.height = height;
		}

		public void render(Graphics g) {
			if (Main.mx > x && Main.mx < x + width && Main.my > y && Main.my < y + height) {

				mouseHvr = true;

			} else {
				mouseHvr = false;
			}
			g.setColor(new Color(0,0,0));
			g.drawRect(x, y, width, height);
			if(mouseHvr) {
				g.setColor(new Color(100,100,100,100));
			}else {
				g.setColor(new Color(0,0,0));
			}
			
			g.fillRect(x + 2, y + 2, width - 3, height - 3);
		}

	}

}
