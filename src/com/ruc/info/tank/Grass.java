package com.ruc.info.tank;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;

public class Grass {
	private int x, y, w, h;
	TankClient tc;
	private static Image grassImage = null;
	
	private static Toolkit tk = Toolkit.getDefaultToolkit();
	
	static{
		grassImage = tk.getImage(Explode.class.getClassLoader().getResource("images/grass.gif"));
	}
	
	public Grass(int x, int y, int w, int h, TankClient tc) {
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
		g.drawImage(grassImage, x, y, w, h, null);
		g.setColor(c);
	}
	
	public Rectangle getRect(){
		return new Rectangle(x, y, w, h);
	}

}
