package com.sakamoto.fileIO;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import com.sakamoto.templates.Enemy_template;
import com.sakamoto.templates.Level_template;

public class FileHandler {

	private static ArrayList<Enemy_template> enemies = new ArrayList<Enemy_template>();
	private static ArrayList<Level_template> levels = new ArrayList<Level_template>();

	
	public static BufferedImage whiteMask = null;
	
	public FileHandler() {
		loadEnemyFile("Data/Enemies/Enemies.txt");
		loadGridByTxt("Data/Levels/Level0.dat");
		loadGridByTxt("Data/Levels/Level0.1.dat");
		loadGridByTxt("Data/Levels/Level1.dat");
		try {
			whiteMask = ImageIO.read(getClass().getResource("/sprite.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static Enemy_template getEnemyByID(int id) {
		for (Enemy_template e : enemies) {
			if (e.getID() == id) {
				return e;
			}
		}
		return null;
	}
	
	public static Level_template getLevelByID(int id) {
		for (Level_template l : levels) {
			if (l.getID() == id) {
				return l;
			}
		}
	
		return null;
	}

	public static void loadEnemyFile(String path) {
		File file = new File(path);
		
		try {
			FileReader fr = new FileReader(file);
			BufferedReader buffer = new BufferedReader(fr);
			try {
				String line = buffer.readLine();
				int ID = 0;
				int life = 0;
				String colorMode = null;
				String tag = null;
				char[] color = new char[8];
				while (line != null && line.contains("Enemy")) {
					if (line.contains("Enemy")) {
						line = buffer.readLine();
						if (line.contains("ID")) {
							String[] part = line.split(":");
								ID = Integer.parseInt(part[1]);	
						}
						line = buffer.readLine();
						if (line.contains("Life")) {
							String[] part = line.split(":");
							life = Integer.parseInt(part[1]);
						}
						line = buffer.readLine();
						if(line.contains("ColorMode")) {
							String[] part = line.split(":");
							colorMode = part[1];
						}
						line = buffer.readLine();
						if (line.contains("Color")) {
							String[] part = line.split(":");
							color = part[1].toCharArray();
						}
						line = buffer.readLine();
						if(line.contains("Tag")) {
							String[] part = line.split(":");
							tag = part[1];
						}
						Enemy_template template = new Enemy_template(ID, life, color, colorMode);
						enemies.add(template);
						line = buffer.readLine();
						line = buffer.readLine();
					}
				}

				buffer.close();
				fr.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {

			e.printStackTrace();
		}
	}

	public static void loadGridByTxt(String path) {
		File file = new File(path);
		ArrayList<String> gridLayout = new ArrayList<String>();
		
		String levelTag = null;
		int levelId = 0;
		int startRows = 0;
		int timeBetweenNewRows = 0;
		try {
			FileReader fr = new FileReader(file);
			BufferedReader buffer = new BufferedReader(fr);
			try {
				String line = buffer.readLine();
				// Get Pattern.
				while( (line = buffer.readLine()) != null ) {
				if (line.contains("StartRows")) {
					String[] part = line.split(":");
					startRows = Integer.parseInt(part[1]);
				}else if(line.contains("TimeBetweenNewRows")) {
					String[] part = line.split(":");
					timeBetweenNewRows = Integer.parseInt(part[1]);
				}else if(line.contains("LevelId")) {
					
					String[] part = line.split(":");
					levelId = Integer.parseInt(part[1]);
					
				}else if(line.contains("LevelTag")) {
					String[] part = line.split(":");
					levelTag = part[1];
				}else if (line.contains("Start")) {
					String rowLayout = "";
					while (!line.contains("End")) {
						line = buffer.readLine();
						if(!line.contains("End") && line.length() < 11) {
							System.out.println("Error, invalid layout length.");
							break;
						}
						rowLayout = line;
						if (!line.contains("End")) {
							gridLayout.add(rowLayout);
						}
					}
				}
				}
				ArrayList<String> inGridLayout = new ArrayList<String>();
				
				
				for(int c = gridLayout.size()-1; c >= 0; c--) {
					inGridLayout.add(gridLayout.get(c));
				
				}
				int maxRows = gridLayout.size();
				Level_template template = new Level_template();
				template.ID = levelId;
				template.tag = levelTag;
				template.startRows = startRows;
				template.maxRows = maxRows;
				template.gridLayout = inGridLayout;
				template.timeBetweenNewRows = timeBetweenNewRows;
				levels.add(template);
				//levels.add(new Level_template(0,startRows, maxRows, timeBetweenNewRows,gridLayout));
				

				// END

				buffer.close();;
				fr.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			System.out.println("Failed to load level at: " + path);
			e.printStackTrace();
		}
		
	}
	
	
}
