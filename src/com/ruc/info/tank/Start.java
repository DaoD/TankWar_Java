package com.ruc.info.tank;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;

public class Start {
	private TankClient tc;
	private static final int X = 370;
	private static final int Y = 388;
	private int x = X, y = Y;
	
	private static Toolkit tk = Toolkit.getDefaultToolkit();
	
	private static Image[] imgs = {
		tk.getImage(Explode.class.getClassLoader().getResource("images/start_1.jpg")),
		tk.getImage(Explode.class.getClassLoader().getResource("images/start_2.jpg")),
		tk.getImage(Explode.class.getClassLoader().getResource("images/selecttank.gif"))
	};
	
	public void draw(Graphics g) {
		g.drawImage(imgs[0], 200, 100, null);
		g.drawImage(imgs[1], 400, 350, null);
		g.drawImage(imgs[2], x, y, 30, 30, null);
	}
	
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		switch(key){
		case KeyEvent.VK_UP:
			if(y != Y) y -= 40;
			break;
		case KeyEvent.VK_DOWN:
			if(y == Y) y += 40;
			break;
		case KeyEvent.VK_ENTER:
			tc.setGame_finished(true);
			if(y == Y) tc.players = 1;
			else tc.players = 2 ;
			break;
		}
	}
}
