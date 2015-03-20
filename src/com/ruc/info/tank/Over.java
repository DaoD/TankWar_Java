package com.ruc.info.tank;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;

public class Over {
	private TankClient tc;
	
	private static Toolkit tk = Toolkit.getDefaultToolkit();
	
	private static Image overImage = tk.getImage(Explode.class.getClassLoader().getResource("images/over.gif"));
	
	private static Image[] tankImages = null;
	
	private static Image[] numImages = null;
	
	static {
		numImages = new Image[] {
			tk.getImage(Explode.class.getClassLoader().getResource("images/0.gif")),
			tk.getImage(Explode.class.getClassLoader().getResource("images/1.gif")),
			tk.getImage(Explode.class.getClassLoader().getResource("images/2.gif")),
			tk.getImage(Explode.class.getClassLoader().getResource("images/3.gif")),
			tk.getImage(Explode.class.getClassLoader().getResource("images/4.gif")),
			tk.getImage(Explode.class.getClassLoader().getResource("images/5.gif")),
			tk.getImage(Explode.class.getClassLoader().getResource("images/6.gif")),
			tk.getImage(Explode.class.getClassLoader().getResource("images/7.gif")),
			tk.getImage(Explode.class.getClassLoader().getResource("images/8.gif")),
			tk.getImage(Explode.class.getClassLoader().getResource("images/9.gif")),
		};
		tankImages = new Image[] {
			tk.getImage(Explode.class.getClassLoader().getResource("images/enemy1U.gif")),
			tk.getImage(Explode.class.getClassLoader().getResource("images/enemy2U.gif")),
			tk.getImage(Explode.class.getClassLoader().getResource("images/enemy3U2.gif"))
		};
	}
	
	public void draw(Graphics g) {
		g.drawImage(overImage, tc.WINDOW_WIDTH/2 - 100, 100, 200, 100, null);
		g.drawImage(tankImages[0], tc.WINDOW_WIDTH/2 - 100, 250, 40, 40, null);
		g.drawImage(numImages[tc.enemy_normal_num / 10], tc.WINDOW_WIDTH/2 + 55, 260, 20, 20, null);
		g.drawImage(numImages[tc.enemy_normal_num % 10], tc.WINDOW_WIDTH/2 + 75, 260, 20, 20, null);
		g.drawImage(tankImages[1], tc.WINDOW_WIDTH/2 - 100, 300, 40, 40, null);
		g.drawImage(numImages[tc.enemy_fast_num / 10], tc.WINDOW_WIDTH/2 + 55, 310, 20, 20, null);
		g.drawImage(numImages[tc.enemy_fast_num % 10], tc.WINDOW_WIDTH/2 + 75, 310, 20, 20, null);
		g.drawImage(tankImages[2], tc.WINDOW_WIDTH/2 - 100, 350, 40, 40, null);
		g.drawImage(numImages[tc.enemy_strong_num / 10], tc.WINDOW_WIDTH/2 + 55, 360, 20, 20, null);
		g.drawImage(numImages[tc.enemy_strong_num % 10], tc.WINDOW_WIDTH/2 + 75, 360, 20, 20, null);
	}
}
