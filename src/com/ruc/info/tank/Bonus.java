package com.ruc.info.tank;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

//加血道具
public class Bonus {
	private static final int WIDTH = 30;
	private static final int HEIGHT = 30;
	
	private int x, y;
	private int type;
	private int timer = 500;
	
	private TankClient tc;
	
	private static Random r = new Random();
	
	private static Map<String, Image> imgs = new HashMap<String, Image>();
	
	private static Toolkit tk = Toolkit.getDefaultToolkit();
	
	static {
		Image[] bonusImages = {
			tk.getImage(Explode.class.getClassLoader().getResource("images/bonus1.gif")),
			tk.getImage(Explode.class.getClassLoader().getResource("images/bonus3.gif")),
			tk.getImage(Explode.class.getClassLoader().getResource("images/bonus4.gif"))
		};
		imgs.put("Blood", bonusImages[0]);
		imgs.put("Base_Enhance", bonusImages[1]);
		imgs.put("Bomb", bonusImages[2]);
		
	}
	private boolean live = true;
	
	public Bonus(int x, int y, int type) {
		this.x = x;
		this.y = y;
		this.type = type;
	}
	
	public void draw(Graphics g) {
		if(!live) {
			tc.bonuses.remove(this);
			return;
		}
		
		switch(type){
		case 0: 
			g.drawImage(imgs.get("Blood"), x, y, WIDTH, HEIGHT, null);
			break;
		case 1:
			g.drawImage(imgs.get("Base_Enhance"), x, y, WIDTH, HEIGHT, null);
			break;
		case 2:
			g.drawImage(imgs.get("Bomb"), x, y, WIDTH, HEIGHT, null);
			break;
		}
		
	}
	
	public void timing() {
		this.timer --;
		if(this.timer == 0) {
			this.live = false;
		}
	}
	
	public Rectangle getRect() {
		return new Rectangle(x, y, WIDTH, HEIGHT);
	}

	public boolean isLive() {
		return live;
	}

	public void setLive(boolean live) {
		this.live = live;
	}

	public int getType() {
		return type;
	}
}
