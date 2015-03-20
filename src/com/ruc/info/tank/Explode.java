package com.ruc.info.tank;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;



public class Explode {
	int x, y;
	private boolean live = true;
	
	private TankClient tc;
	
	private static Toolkit tk = Toolkit.getDefaultToolkit();
	
	//爆炸图片的引入
	private static Image[] imgs = {
		tk.getImage(Explode.class.getClassLoader().getResource("images/blast1.gif")),
		tk.getImage(Explode.class.getClassLoader().getResource("images/blast2.gif")),
		tk.getImage(Explode.class.getClassLoader().getResource("images/blast3.gif")),
		tk.getImage(Explode.class.getClassLoader().getResource("images/blast4.gif")),
		tk.getImage(Explode.class.getClassLoader().getResource("images/blast5.gif"))
	};
	
	int step = 0;
	
	private static  boolean init = false; 
	
	public Explode(int x, int y, TankClient tc) {
		this.x = x;
		this.y = y;
		this.tc = tc;
	}
	
	public void draw(Graphics g){
		
		//初始化第一张照片
		if(!init) {
			for (int i = 0; i < imgs.length; i++) {
				g.drawImage(imgs[i], -100, -100, null);
			}
			init = true;
		}
		
		if(!live) {
			tc.explodes.remove(this);
			return;
		}
		
		if(step == imgs.length){
			live = false;
			step = 0;
			return;
		}
		
		g.drawImage(imgs[step], x, y, null);
		
		step ++;
	}
}
