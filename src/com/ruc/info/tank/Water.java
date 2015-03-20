package com.ruc.info.tank;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;


public class Water {
	private int x, y, w, h;
	TankClient tc;
	private static Image waterImage = null;
	
	private static Toolkit tk = Toolkit.getDefaultToolkit();
	
	static{
		waterImage = tk.getImage(Explode.class.getClassLoader().getResource("images/water.gif"));
	}
	
	public Water(int x, int y, int w, int h, TankClient tc) {
		super();
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		this.tc = tc;
	}
	
	public void draw(Graphics g){
		Color c = g.getColor();
		g.setColor(Color.DARK_GRAY);
		g.drawImage(waterImage, x, y, w, h, null);
		g.setColor(c);
	}
	
	public Rectangle getRect(){
		return new Rectangle(x, y, w, h);
	}
}
