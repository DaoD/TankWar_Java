package com.ruc.info.tank;

import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Tank {
	
	//我方坦克移动速度
	public static final int XSPEED = 5;
	public static final int YSPEED = 5;
	//一类敌方坦克移动速度
	public static final int E1XSPEED = 5;
	public static final int E1YSPEED = 5;
	//二类敌方坦克移动速度
	public static final int E2XSPEED = 15;
	public static final int E2YSPEED = 15;
	//三类敌方坦克移动速度
	public static final int E3XSPEED = 5;
	public static final int E3YSPEED = 5;
	
	public static final int WIDTH = 40;
	public static final int HEIGHT = 40;
	
	public static int myLife = 2;
	
	private boolean live = true;
	private boolean finish = false;
	
	private boolean isOne = true;
	
	private boolean init = false; 
	private int bornstep = 0;
	
	private int life = 3;

	TankClient tc;
	
	private boolean good;
	private boolean strong;
	private boolean havebonus;
	
	private int x, y;
	private int oldX, oldY;
	
	private static Random r = new Random();
	
	private boolean bL = false, bR = false, bU = false, bD = false;//判断上下左右键是否按下
	
	private TankKind kind;
	
	private Direction dir = Direction.STOP;
	private Direction ptDir = Direction.U;
	
	private int step = r.nextInt(12) + 3;
	
	private static Toolkit tk = Toolkit.getDefaultToolkit();
	
	private static AudioClip blast = Applet.newAudioClip(Explode.class.getClassLoader().getResource("music/explode.WAV"));
	
	//坦克图片的引入
	private static Image[] tankImages = null;
	//初始图片的引入
	private static Image[] bornImages = null;
	//用hashmap为图片另起名字
	private static Map<String, Image> imgs = new HashMap<String, Image>();
	
	private static AudioClip firesound = Applet.newAudioClip(Tank.class.getClassLoader().getResource("music/shoot.wav"));
	private static AudioClip absorb = Applet.newAudioClip(Tank.class.getClassLoader().getResource("music/absorb.wav"));
	
	//另一种引入图片的方法，静态代码区
	static{
		tankImages = new Image[] {
			tk.getImage(Explode.class.getClassLoader().getResource("images/tankL.gif")),
			tk.getImage(Explode.class.getClassLoader().getResource("images/tankD.gif")),
			tk.getImage(Explode.class.getClassLoader().getResource("images/tankR.gif")),
			tk.getImage(Explode.class.getClassLoader().getResource("images/tankU.gif")),
			tk.getImage(Explode.class.getClassLoader().getResource("images/enemy1L.gif")),
			tk.getImage(Explode.class.getClassLoader().getResource("images/enemy1D.gif")),
			tk.getImage(Explode.class.getClassLoader().getResource("images/enemy1R.gif")),
			tk.getImage(Explode.class.getClassLoader().getResource("images/enemy1U.gif")),
			tk.getImage(Explode.class.getClassLoader().getResource("images/enemy2L.gif")),
			tk.getImage(Explode.class.getClassLoader().getResource("images/enemy2D.gif")),
			tk.getImage(Explode.class.getClassLoader().getResource("images/enemy2R.gif")),
			tk.getImage(Explode.class.getClassLoader().getResource("images/enemy2U.gif")),
			tk.getImage(Explode.class.getClassLoader().getResource("images/enemy3L1.gif")),
			tk.getImage(Explode.class.getClassLoader().getResource("images/enemy3D1.gif")),
			tk.getImage(Explode.class.getClassLoader().getResource("images/enemy3R1.gif")),
			tk.getImage(Explode.class.getClassLoader().getResource("images/enemy3U1.gif")),
			tk.getImage(Explode.class.getClassLoader().getResource("images/enemy3L2.gif")),
			tk.getImage(Explode.class.getClassLoader().getResource("images/enemy3D2.gif")),
			tk.getImage(Explode.class.getClassLoader().getResource("images/enemy3R2.gif")),
			tk.getImage(Explode.class.getClassLoader().getResource("images/enemy3U2.gif")),
			tk.getImage(Explode.class.getClassLoader().getResource("images/enemy3L3.gif")),
			tk.getImage(Explode.class.getClassLoader().getResource("images/enemy3D3.gif")),
			tk.getImage(Explode.class.getClassLoader().getResource("images/enemy3R3.gif")),
			tk.getImage(Explode.class.getClassLoader().getResource("images/enemy3U3.gif")),
			tk.getImage(Explode.class.getClassLoader().getResource("images/enemy1LB.gif")),
			tk.getImage(Explode.class.getClassLoader().getResource("images/enemy1DB.gif")),
			tk.getImage(Explode.class.getClassLoader().getResource("images/enemy1RB.gif")),
			tk.getImage(Explode.class.getClassLoader().getResource("images/enemy1UB.gif")),
			tk.getImage(Explode.class.getClassLoader().getResource("images/enemy2LB.gif")),
			tk.getImage(Explode.class.getClassLoader().getResource("images/enemy2DB.gif")),
			tk.getImage(Explode.class.getClassLoader().getResource("images/enemy2RB.gif")),
			tk.getImage(Explode.class.getClassLoader().getResource("images/enemy2UB.gif")),
			tk.getImage(Explode.class.getClassLoader().getResource("images/tank2L.gif")),
			tk.getImage(Explode.class.getClassLoader().getResource("images/tank2D.gif")),
			tk.getImage(Explode.class.getClassLoader().getResource("images/tank2R.gif")),
			tk.getImage(Explode.class.getClassLoader().getResource("images/tank2U.gif"))
		};
		bornImages = new Image[] {
				tk.getImage(Explode.class.getClassLoader().getResource("images/born1.gif")),
				tk.getImage(Explode.class.getClassLoader().getResource("images/born2.gif")),
				tk.getImage(Explode.class.getClassLoader().getResource("images/born3.gif")),
				tk.getImage(Explode.class.getClassLoader().getResource("images/born4.gif"))
		};
		
		imgs.put("L", tankImages[0]);
		imgs.put("D", tankImages[1]);
		imgs.put("R", tankImages[2]);
		imgs.put("U", tankImages[3]);
		imgs.put("2L", tankImages[32]);
		imgs.put("2D", tankImages[33]);
		imgs.put("2R", tankImages[34]);
		imgs.put("2U", tankImages[35]);
		imgs.put("E1L", tankImages[4]);
		imgs.put("E1D", tankImages[5]);
		imgs.put("E1R", tankImages[6]);
		imgs.put("E1U", tankImages[7]);
		imgs.put("E2L", tankImages[8]);
		imgs.put("E2D", tankImages[9]);
		imgs.put("E2R", tankImages[10]);
		imgs.put("E2U", tankImages[11]);
		imgs.put("E3L1", tankImages[12]);
		imgs.put("E3D1", tankImages[13]);
		imgs.put("E3R1", tankImages[14]);
		imgs.put("E3U1", tankImages[15]);
		imgs.put("E3L2", tankImages[16]);
		imgs.put("E3D2", tankImages[17]);
		imgs.put("E3R2", tankImages[18]);
		imgs.put("E3U2", tankImages[19]);
		imgs.put("E3L3", tankImages[20]);
		imgs.put("E3D3", tankImages[21]);
		imgs.put("E3R3", tankImages[22]);
		imgs.put("E3U3", tankImages[23]);
		imgs.put("born1", bornImages[0]);
		imgs.put("born2", bornImages[1]);
		imgs.put("born3", bornImages[2]);
		imgs.put("born4", bornImages[3]);
		imgs.put("E1LB", tankImages[24]);
		imgs.put("E1DB", tankImages[25]);
		imgs.put("E1RB", tankImages[26]);
		imgs.put("E1UB", tankImages[27]);
		imgs.put("E2LB", tankImages[28]);
		imgs.put("E2DB", tankImages[29]);
		imgs.put("E2RB", tankImages[30]);
		imgs.put("E2UB", tankImages[31]);
	}
	
	public Tank(int x, int y, boolean good, boolean strong, boolean havebonus, boolean isOne) {
		this.x = x;
		this.y = y;
		this.oldX = y;
		this.oldY = y;
		this.good = good;
		this.strong = strong;
		this.havebonus = havebonus;
		this.isOne = isOne;
	}
	
	public Tank(int x, int y, boolean good, boolean strong, boolean havebonus, boolean isOne, Direction dir, TankKind kind, TankClient tc) {
		this(x, y, good, strong, havebonus, isOne);
		this.tc = tc;
		this.dir = dir;
		this.kind = kind;
	}
	
	public void draw(Graphics g) {
		if(!live) {
			if(!good) {
				tc.tanks.remove(this);
				tc.total_enemy --;
			}
			return;
		}
		
		if(havebonus) {
			if(kind == TankKind.enemyNormal) {
				switch (ptDir) {
				case L:
					g.drawImage(imgs.get("E1LB"), x, y, WIDTH, HEIGHT, null);
					break;
				case U:
					g.drawImage(imgs.get("E1UB"), x, y, WIDTH, HEIGHT, null);
					break;
				case R:
					g.drawImage(imgs.get("E1RB"), x, y, WIDTH, HEIGHT, null);
					break;
				case D:
					g.drawImage(imgs.get("E1DB"), x, y, WIDTH, HEIGHT, null);
					break;
				}
			}
			
			else if(kind == TankKind.enemyFast) {
				switch (ptDir) {
				case L:
					g.drawImage(imgs.get("E2LB"), x, y, WIDTH, HEIGHT, null);
					break;
				case U:
					g.drawImage(imgs.get("E2UB"), x, y, WIDTH, HEIGHT, null);
					break;
				case R:
					g.drawImage(imgs.get("E2RB"), x, y, WIDTH, HEIGHT, null);
					break;
				case D:
					g.drawImage(imgs.get("E2DB"), x, y, WIDTH, HEIGHT, null);
					break;
				}
			}
		}
		else { 
			if(kind == TankKind.mine){
				if(isOne){
					switch (ptDir) {
					case L:
						g.drawImage(imgs.get("L"), x, y, WIDTH, HEIGHT, null);
						break;
					case D:
						g.drawImage(imgs.get("D"), x, y, WIDTH, HEIGHT, null);
						break;
					case R:
						g.drawImage(imgs.get("R"), x, y, WIDTH, HEIGHT, null);
						break;
					case U:
						g.drawImage(imgs.get("U"), x, y, WIDTH, HEIGHT, null);
						break;
					}
				}
				else {
					switch (ptDir) {
					case L:
						g.drawImage(imgs.get("2L"), x, y, WIDTH, HEIGHT, null);
						break;
					case D:
						g.drawImage(imgs.get("2D"), x, y, WIDTH, HEIGHT, null);
						break;
					case R:
						g.drawImage(imgs.get("2R"), x, y, WIDTH, HEIGHT, null);
						break;
					case U:
						g.drawImage(imgs.get("2U"), x, y, WIDTH, HEIGHT, null);
						break;
					}
				}
			}
			else if(kind == TankKind.enemyNormal) {
				switch (ptDir) {
				case L:
					g.drawImage(imgs.get("E1L"), x, y, WIDTH, HEIGHT, null);
					break;
				case U:
					g.drawImage(imgs.get("E1U"), x, y, WIDTH, HEIGHT, null);
					break;
				case R:
					g.drawImage(imgs.get("E1R"), x, y, WIDTH, HEIGHT, null);
					break;
				case D:
					g.drawImage(imgs.get("E1D"), x, y, WIDTH, HEIGHT, null);
					break;
				}
			}
			
			else if(kind == TankKind.enemyFast) {
				switch (ptDir) {
				case L:
					g.drawImage(imgs.get("E2L"), x, y, WIDTH, HEIGHT, null);
					break;
				case U:
					g.drawImage(imgs.get("E2U"), x, y, WIDTH, HEIGHT, null);
					break;
				case R:
					g.drawImage(imgs.get("E2R"), x, y, WIDTH, HEIGHT, null);
					break;
				case D:
					g.drawImage(imgs.get("E2D"), x, y, WIDTH, HEIGHT, null);
					break;
				}
			}
			
			else if(kind == TankKind.enemyStrong) {
				if(life == 3) {
					switch (ptDir) {
					case L:
						g.drawImage(imgs.get("E3L1"), x, y, WIDTH, HEIGHT, null);
						break;
					case U:
						g.drawImage(imgs.get("E3U1"), x, y, WIDTH, HEIGHT, null);
						break;
					case R:
						g.drawImage(imgs.get("E3R1"), x, y, WIDTH, HEIGHT, null);
						break;
					case D:
						g.drawImage(imgs.get("E3D1"), x, y, WIDTH, HEIGHT, null);
						break;
					}
				}
				else if(life == 2) {
					switch (ptDir) {
					case L:
						g.drawImage(imgs.get("E3L2"), x, y, WIDTH, HEIGHT, null);
						break;
					case U:
						g.drawImage(imgs.get("E3U2"), x, y, WIDTH, HEIGHT, null);
						break;
					case R:
						g.drawImage(imgs.get("E3R2"), x, y, WIDTH, HEIGHT, null);
						break;
					case D:
						g.drawImage(imgs.get("E3D2"), x, y, WIDTH, HEIGHT, null);
						break;
					}
				}
				else if(life == 1) {
					switch (ptDir) {
					case L:
						g.drawImage(imgs.get("E3L3"), x, y, WIDTH, HEIGHT, null);
						break;
					case U:
						g.drawImage(imgs.get("E3U3"), x, y, WIDTH, HEIGHT, null);
						break;
					case R:
						g.drawImage(imgs.get("E3R3"), x, y, WIDTH, HEIGHT, null);
						break;
					case D:
						g.drawImage(imgs.get("E3D3"), x, y, WIDTH, HEIGHT, null);
						break;
					}
				}
			}
		}
		
		move();
	}
	
	public void drawBorn(Graphics g){
		
		//初始化第一张照片
		if(!init) {
			for (int i = 0; i < bornImages.length; i++) {
				g.drawImage(bornImages[i], -100, -100, null);
			}
			init = true;
		}
		
		if(bornstep == bornImages.length * 4){
			finish = true;
			bornstep = 0;
			return;
		}
		
		if(!finish)
			g.drawImage(bornImages[bornstep / 4], x -10, y - 10, null);
		
		bornstep ++;
	}
	
	//移动，位移量需要重新考虑
	void move() {
		
		this.oldX = x;
		this.oldY = y;
		
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
		case STOP:
			break;
		}
		
		if(this.dir != Direction.STOP) {
			this.ptDir = this.dir;
		}
		
		if(x < 0) x = 0;
		if(y < 30) y = 30;
		if(x + Tank.WIDTH > TankClient.GAME_WIDTH) x = TankClient.GAME_WIDTH - Tank.WIDTH;
		if(y + Tank.HEIGHT > TankClient.GAME_HEIGHT) y = TankClient.GAME_HEIGHT - Tank.HEIGHT;
		
		if(!good) {
			Direction[] dirs = Direction.values();
			
			if(step == 0){
				step = r.nextInt(12) + 3;
				int rn = r.nextInt(dirs.length);
				dir = dirs[rn];
			}
			step --;
			if(r.nextInt(40) > 38){
				this.fire();
			}
		}
	}
	
	//停止
	private void stay() {
		x = oldX;
		y = oldY;
	}
	
	//处理键盘事件
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		switch(key){
		case KeyEvent.VK_LEFT:
			bL = true;
			break;
		case KeyEvent.VK_UP:
			bU = true;
			break;
		case KeyEvent.VK_RIGHT:
			bR = true;
			break;
		case KeyEvent.VK_DOWN:
			bD = true;
			break;
		}
		locateDirection();
	}
	
	public void keyReleased(KeyEvent e) {
		int key = e.getKeyCode();
		switch(key){
		case KeyEvent.VK_SPACE:
			fire();
			break;
		case KeyEvent.VK_LEFT:
			bL = false;
			break;
		case KeyEvent.VK_UP:
			bU = false;
			break;
		case KeyEvent.VK_RIGHT:
			bR = false;
			break;
		case KeyEvent.VK_DOWN:
			bD = false;
			break;
		case KeyEvent.VK_Z:
			superFire();
			break;
		}
		locateDirection();
	}
	
	public void keyPressed2(KeyEvent e) {
		int key = e.getKeyCode();
		switch(key){
		case KeyEvent.VK_A:
			bL = true;
			break;
		case KeyEvent.VK_W:
			bU = true;
			break;
		case KeyEvent.VK_D:
			bR = true;
			break;
		case KeyEvent.VK_S:
			bD = true;
			break;
		}
		locateDirection();
	}
	
	public void keyReleased2(KeyEvent e) {
		int key = e.getKeyCode();
		switch(key){
		case KeyEvent.VK_V:
			fire();
			break;
		case KeyEvent.VK_A:
			bL = false;
			break;
		case KeyEvent.VK_W:
			bU = false;
			break;
		case KeyEvent.VK_D:
			bR = false;
			break;
		case KeyEvent.VK_S:
			bD = false;
			break;
		case KeyEvent.VK_B:
			superFire();
			break;
		}
		locateDirection();
	}
	
	void locateDirection() {
		if(bL && !bU && !bR && !bD) dir = Direction.L;
		else if(!bL && bU && !bR && !bD) dir = Direction.U;
		else if(!bL && !bU && bR && !bD) dir = Direction.R;
		else if(!bL && !bU && !bR && bD) dir = Direction.D;
		else if(!bL && !bU && !bR && !bD) dir = Direction.STOP;
	}
	
	//打子弹
	public Missile fire() {
		if(!live) return null;
		int x = this.x + Tank.WIDTH/2 - Missile.WIDTH/2; 
		int y = this.y + Tank.HEIGHT/2 - Missile.HEIGHT/2;
		Missile m = new Missile(x, y, good, ptDir, this.tc);
		firesound.play();
		tc.missiles.add(m);
		return m;
	}
	
	public Missile fire(Direction dir){
		if(!live) return null;
		int x = this.x + Tank.WIDTH/2 - Missile.WIDTH/2; 
		int y = this.y + Tank.HEIGHT/2 - Missile.HEIGHT/2;
		Missile m = new Missile(x, y, good, dir, this.tc);
		firesound.play();
		tc.missiles.add(m);
		return m;
	}
	
	public boolean isLive() {
		return live;
	}

	public void setLive(boolean live) {
		this.live = live;
	}
	
	public Rectangle getRect() {
		return new Rectangle(x, y, WIDTH, HEIGHT);
	}

	public boolean isGood() {
		return good;
	}
	
	//碰到墙壁
	public boolean collidesWithWall(Wall w) {
		if(this.live && this.getRect().intersects(w.getRect()) && w.isLive()) {
			this.stay();
			return true;
		}
		return false;
	}
	public boolean collidesWithWalls(List<Wall> walls) {
		for(int i = 0; i < walls.size(); i++) {
			if(collidesWithWall(walls.get(i))) {
				return true;
			}
		}
		return false;
	}
	
	public boolean collidesWithIron(Iron w) {
		if(this.live && this.getRect().intersects(w.getRect())) {
			this.stay();
			return true;
		}
		return false;
	}
	public boolean collidesWithIrons(List<Iron> irons) {
		for(int i = 0; i < irons.size(); i++) {
			if(collidesWithIron(irons.get(i))) {
				return true;
			}
		}
		return false;
	}
	
	public boolean collidesWithWater(Water wa) {
		if(this.live && this.getRect().intersects(wa.getRect())) {
			this.stay();
			return true;
		}
		return false;
	}
	public boolean collidesWithWaters(List<Water> waters) {
		for(int i = 0; i < waters.size(); i++) {
			if(collidesWithWater(waters.get(i))) {
				return true;
			}
		}
		return false;
	}
	
	//碰到坦克
	public boolean collidesWithTanks(List<Tank> tanks) {
		for(int i = 0; i < tanks.size(); i++) {
			Tank t = tanks.get(i);
			if(this != t){
				if(this.live && t.isLive() && this.getRect().intersects(t.getRect())){
					this.stay();
					t.stay();
					return true;
				}
			}
		}
		return false;
	}
	
	public void superFire() {
		Direction[] dirs = Direction.values();
		for(int i = 0; i < 4; i++) {
			fire(dirs[i]);
		}
	}

	public int getLife() {
		return life;
	}

	public void setLife(int life) {
		this.life = life;
	}
	
	public boolean eat(Bonus b) {
		if(this.live && b.isLive() && this.getRect().intersects(b.getRect())){
			absorb.play();
			b.setLive(false);
			if(b.getType() == 0) {
				Tank.setMyLife(Tank.getMyLife() + 1);
			}
			else if(b.getType() == 1) {
				tc.base.setEnhanced(true);
				tc.enhancedtime = 300;
				//基地墙-左
				for(int i = 0; i < 4; i++) {
					tc.baseirons.add(new Iron(tc.GAME_WIDTH / 2 - 60, tc.GAME_HEIGHT - 20 - i * 20, 20, 20, tc));
				}
				//基地墙-上
				for(int i = 0; i < 4; i++) {
					tc.baseirons.add(new Iron(tc.GAME_WIDTH / 2 - 40 + i * 20, tc.GAME_HEIGHT - 80, 20, 20, tc));
				}
				//基地墙-右
				for(int i = 0; i < 4; i++) {
					tc.baseirons.add(new Iron(tc.GAME_WIDTH / 2 + 40, tc.GAME_HEIGHT - 20 - i * 20, 20, 20, tc));
				}
			}
			else {
				for(int i = 0; i < tc.tanks.size(); i++) {
					Tank t = tc.tanks.get(i);
					Explode e = new Explode(t.getX() - 50, t.getY() - 50, tc);
					tc.explodes.add(e);
					blast.play();
				}
				tc.total_enemy -= tc.tanks.size();
				tc.tanks.clear();
			}
			return true;
		}
		return false;
	}

	public boolean isStrong() {
		return strong;
	}

	public boolean isFinish() {
		return finish;
	}

	public boolean isHavebonus() {
		return havebonus;
	}

	public void setHavebonus(boolean havebonus) {
		this.havebonus = havebonus;
	}

	public static int getMyLife() {
		return myLife;
	}

	public static void setMyLife(int myLife) {
		Tank.myLife = myLife;
	}

	public boolean isOne() {
		return isOne;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public TankKind getKind() {
		return kind;
	}
}
