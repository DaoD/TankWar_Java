package com.ruc.info.tank;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;

public class Base {
	private int x, y;
	private static final int WIDTH = 60;
	private static final int HEIGHT = 45;
	TankClient tc;
	private static Image baseImage = null;
	private static Image destroyImage = null;
	
	private static boolean Enhanced = false;
	
	private boolean live = true; 
	
	private static Toolkit tk = Toolkit.getDefaultToolkit();
	
	static{
		baseImage = tk.getImage(Explode.class.getClassLoader().getResource("images/base.gif"));
		destroyImage = tk.getImage(Explode.class.getClassLoader().getResource("images/destroy.gif"));
	}
	
	public Base(int x, int y, TankClient tc ) {
		this.x = x;
		this.y = y;
		this.tc = tc;
	}
	
	public void draw(Graphics g) {
		if(!live) {
			g.drawImage(destroyImage, x, y, WIDTH, HEIGHT, null);;
		}
		else {
			g.drawImage(baseImage, x, y, WIDTH, HEIGHT, null);
		}
	}
	
	public Rectangle getRect(){
		return new Rectangle(x, y, WIDTH, HEIGHT);
	}

	public boolean isLive() {
		return live;
	}

	public void setLive(boolean live) {
		this.live = live;
	}

	public static boolean isEnhanced() {
		return Enhanced;
	}

	public static void setEnhanced(boolean enhanced) {
		Enhanced = enhanced;
	}
}
