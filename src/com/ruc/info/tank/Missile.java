package com.ruc.info.tank;

import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Missile {
	
	public static final int XSPEED = 10;
	public static final int YSPEED = 10;
	
	public static final int WIDTH = 10;
	public static final int HEIGHT = 10;
	
	int x, y;
	Direction dir;
	
	private boolean good;  
	private boolean live = true;
	private TankClient tc;
	
	private static Random r = new Random();
	
	private static Toolkit tk = Toolkit.getDefaultToolkit();
	
	private static AudioClip hit = Applet.newAudioClip(Missile.class.getClassLoader().getResource("music/hit.WAV"));
	private static AudioClip blast = Applet.newAudioClip(Explode.class.getClassLoader().getResource("music/explode.WAV"));
	
	//子弹图片的引入
	private static Image[] missileImages = null;
	//用hashmap为图片另起名字
	private static Map<String, Image> imgs = new HashMap<String, Image>();
	
	//另一种引入图片的方法，静态代码区
	static{
		missileImages = new Image[] {
			tk.getImage(Explode.class.getClassLoader().getResource("images/tankmissile.gif")),
			tk.getImage(Explode.class.getClassLoader().getResource("images/enemymissile.gif"))
		};
		
		imgs.put("tankmissile", missileImages[0]);
		imgs.put("enemymissile", missileImages[1]);
	}

	public Missile(int x, int y, Direction dir) {
		this.x = x;
		this.y = y;
		this.dir = dir;
	}
	
	public Missile(int x, int y, boolean good,Direction dir, TankClient tc) {
		this(x, y, dir);
		this.good = good;
		this.tc = tc;
	}
	
	public void draw(Graphics g) {
		if(!live){
			tc.missiles.remove(this);
			return;
		}
		if(good) g.drawImage(imgs.get("tankmissile"), x, y, WIDTH, HEIGHT, null);
		else g.drawImage(imgs.get("enemymissile"), x, y, WIDTH, HEIGHT, null);
		
		move();
	}
	private void move() {
		switch (dir){
		case L:
			x -= XSPEED;
			break;
		case U:
			y -= YSPEED;
			break;
		case R:
			x += XSPEED;
			break;
		case D:
			y += YSPEED;
			break;
		}
		
		//炮弹消失
		if(x < 0 || y < 0 || x > TankClient.GAME_WIDTH || y > TankClient.GAME_HEIGHT){
			live = false;
			tc.missiles.remove(this);
		}
	}
	
	//控制炮弹是否消失
	public boolean isLive() {
		return live;
	}
	
	//获取边框
	public Rectangle getRect() {
		return new Rectangle(x, y, WIDTH, HEIGHT);
	}
	
	//打坦克
	public boolean hitTank(Tank t) {
		if(this.live && this.getRect().intersects(t.getRect()) && t.isLive() && this.good != t.isGood()){
			if(t.isStrong()) {
				t.setLife(t.getLife() - 1);
				if(t.getLife() <= 0) {
					tc.enemy_strong_num ++;
					t.setLive(false);	
				}
			}
			else {
				if(t.isHavebonus()) {
					tc.bonuses.add(new Bonus(r.nextInt(600) + 100, r.nextInt(400) + 100, r.nextInt(3)));
				}
				switch(t.getKind()) {
				case enemyNormal: 
					tc.enemy_normal_num ++;
					break;
				case enemyFast:
					tc.enemy_fast_num ++;
					break;
				case enemyStrong:
					tc.enemy_strong_num ++;
					break;
				}
				t.setLive(false);
				if(t.isGood()) {
					if(Tank.getMyLife() != 0) {
						Tank.setMyLife(Tank.getMyLife() - 1);
						if(t.isOne()) {
							tc.myTank = new Tank(tc.GAME_WIDTH / 2 - 100, tc.GAME_HEIGHT - 40, true, false, false, true, Direction.STOP, TankKind.mine, tc);
						}
						else {
							tc.myTank2 = new Tank(tc.GAME_WIDTH / 2 - 100, tc.GAME_HEIGHT - 40, true, false, false, true, Direction.STOP, TankKind.mine, tc);
						}
					}else tc.game_over =true;
				}
			}
			hit.play();
			this.live = false;
			Explode e = new Explode(x - 50, y - 50, tc);
			blast.play();
			tc.explodes.add(e);
			return true;
		}
		return false;
	}
	
	public boolean hitTanks(List<Tank> tanks) {
		for(int i = 0; i < tanks.size(); i++) {
			if(hitTank(tanks.get(i))) {
				return true;
			}
		}
		return false;
	}
	
	public boolean hitWall(Wall w) {
		if(this.live && this.getRect().intersects(w.getRect()) && w.isLive()){
			hit.play();
			this.live = false;
			w.setLive(false);
			return true;
		}
		return false;
	}
	
	public boolean hitWalls(List<Wall> walls) {
		for(int i = 0; i < walls.size(); i++) {
			if(hitWall(walls.get(i))) {
				return true;
			}
		}
		return false;
	}
	
	public boolean hitIron(Iron ir) {
		if(this.live && this.getRect().intersects(ir.getRect())){
			hit.play();
			this.live = false;
			return true;
		}
		return false;
	}
	
	public boolean hitIrons(List<Iron> irons) {
		for(int i = 0; i < irons.size(); i++) {
			if(hitIron(irons.get(i))) {
				return true;
			}
		}
		return false;
	}
	
	public boolean hitMissiles(List<Missile> ms) {
		for(int i = 0; i < ms.size(); i++) {
			Missile m = ms.get(i);
			if(this != m && this.good != m.good) {
				if(this.live && m.isLive() && this.getRect().intersects(m.getRect()) ) {
					hit.play();
					m.live = false;
					this.live = false;
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean hitBase(Base base) {
		if(this.live && base.isLive() && this.getRect().intersects(base.getRect())) {
			hit.play();
			this.live = false;
			base.setLive(false);
			tc.myTank.setLive(false);
			tc.myTank2.setLive(false);
			Explode e = new Explode(x - 50, y - 50, tc);
			tc.explodes.add(e);
			tc.game_over = true;
			return true;
		}
		return false;
	}
}
  