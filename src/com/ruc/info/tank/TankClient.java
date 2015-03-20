package com.ruc.info.tank;

import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TankClient extends Frame{

	public static final int GAME_WIDTH = 800;
	public static final int GAME_HEIGHT = 610;
	
	public static final int WINDOW_WIDTH = 1000;
	public static final int WINDOW_HEIGHT = 610;
	
	public static boolean game_finished = false;
	public static boolean game_over = false;
	
	public static int enhancedtime = 0;
	
	private TankKind kind;
	
	public static int players = 1;
	
	public static int enemy_normal_num = 0;
	public static int enemy_fast_num = 0;
	public static int enemy_strong_num = 0;
	public static int total_enemy = 18;
	
	private static Random r = new Random();
	
	Tank myTank = new Tank(GAME_WIDTH / 2 - 100, GAME_HEIGHT - 40, true, false, false, true, Direction.STOP, TankKind.mine, this);
	Tank myTank2 = new Tank(GAME_WIDTH / 2 + 60, GAME_HEIGHT - 40, true, false, false, false, Direction.STOP, TankKind.mine, this);
	
	Base base = new Base(GAME_WIDTH / 2 - 30, GAME_HEIGHT - 50, this);
		
	private static AudioClip bgm = Applet.newAudioClip(TankClient.class.getClassLoader().getResource("music/main.WAV"));

	List<Explode> explodes = new ArrayList<Explode>();
	List<Missile> missiles = new ArrayList<Missile>(); 
	List<Tank> tanks = new ArrayList<Tank>();
	List<Wall> walls = new ArrayList<Wall>();
	List<Wall> basewalls = new ArrayList<Wall>();
	List<Iron> irons = new ArrayList<Iron>();
	List<Iron> baseirons = new ArrayList<Iron>();
	List<Water> waters = new ArrayList<Water>();
	List<Grass> grasses = new ArrayList<Grass>();
	List<Bonus> bonuses = new ArrayList<Bonus>();
	
	Start st = new Start();
	Over ov = new Over();
	
	//利用双缓冲消除闪烁
	Image offscreenImage = null;
	private static Image healthImage = null;
	private static Image myhealthImage = null;
	
	TankKind[] tkk = TankKind.values();
	
	private static Toolkit tk = Toolkit.getDefaultToolkit();
	
	static {
		healthImage = tk.getImage(Explode.class.getClassLoader().getResource("images/health.gif"));
		myhealthImage = tk.getImage(Explode.class.getClassLoader().getResource("images/myhealth.gif"));
	}
	
	//绘制坦克
	public void paint(Graphics g) {
		if(game_finished && !game_over) {
			if(tanks.size() <= 0 && total_enemy > 0) {
				for(int i = 0; i < Integer.parseInt(PropertyMgr.getProperty("reProduceTankCount")); i++){
					int rtk = r.nextInt(tkk.length - 1) + 1;
					kind = tkk[rtk];
					if(kind == TankKind.enemyStrong) {
						tanks.add(new Tank(200 * (i + 1), 50, false, true, false, false, Direction.D, kind, this));
					}
					else {
						int rhb = r.nextInt(10);
						if(rhb > 7) {
							tanks.add(new Tank(200 * (i + 1), 50, false, false, true, false, Direction.D, kind, this));
						}
						else tanks.add(new Tank(200 * (i + 1), 50, false, false, false, false, Direction.D, kind, this));
					}
				}
			}
			else if(total_enemy <= 0) game_over = true;
			
			initMap();
			
			for(int i = 0; i < missiles.size(); i++) {
				Missile m = missiles.get(i);
				m.hitTanks(tanks);
				m.hitTank(myTank);
				m.hitTank(myTank2);
				m.hitMissiles(missiles);
				m.draw(g);
				m.hitWalls(walls);
				m.hitWalls(basewalls);
				m.hitIrons(irons);
				m.hitIrons(baseirons);
				m.hitBase(base);
			}
			
			for(int i = 0; i < explodes.size(); i++) {
				Explode e = explodes.get(i);
				e.draw(g);
			}
			
			for(int i = 0; i < tanks.size(); i++) {
				Tank t = tanks.get(i);
				t.collidesWithWalls(walls);
				t.collidesWithWalls(basewalls);
				t.collidesWithIrons(irons);
				t.collidesWithIrons(baseirons);
				t.collidesWithWaters(waters);
				t.collidesWithTanks(tanks);
				if(!t.isFinish())
					t.drawBorn(g);
				else
					t.draw(g);
			}
			
			myTank.draw(g);
			myTank.collidesWithWalls(walls);
			myTank.collidesWithWalls(basewalls);
			myTank.collidesWithIrons(irons);
			myTank.collidesWithIrons(baseirons);
			myTank.collidesWithWaters(waters);
			myTank.collidesWithTanks(tanks);
			
			if (players == 2){
				myTank2.draw(g);
				myTank2.collidesWithWalls(walls);
				myTank2.collidesWithWalls(basewalls);
				myTank2.collidesWithIrons(irons);
				myTank2.collidesWithIrons(baseirons);
				myTank2.collidesWithWaters(waters);
				myTank2.collidesWithTanks(tanks);
			}
			
			for(int i = 0; i < bonuses.size(); i ++){
				Bonus bo = bonuses.get(i);
				myTank.eat(bo);
				myTank2.eat(bo);
			}
			
			for(int i = 0; i < walls.size(); i++) {
				Wall w = walls.get(i);
				if(w.isLive()){
					w.draw(g);
				}
			}
			
			for(int i = 0; i < basewalls.size(); i++) {
				Wall w = basewalls.get(i);
				if(w.isLive()){
					w.draw(g);
				}
			}
			
			for(int i = 0; i < irons.size(); i++) {
				Iron ir = irons.get(i);
				ir.draw(g);
			}
			
			for(int i = 0; i < baseirons.size(); i++) {
				Iron ir = baseirons.get(i);
				ir.draw(g);
			}
			
			for(int i = 0; i < waters.size(); i++) {
				Water wa = waters.get(i);
				wa.draw(g);
			}
			
			for(int i = 0; i < grasses.size(); i++) {
				Grass gr = grasses.get(i);
				gr.draw(g);
			}
			
			base.draw(g);
			
			for(int i = 0; i < bonuses.size(); i++) {
				Bonus bo = bonuses.get(i);
				bo.timing();
				if(bo.isLive()) {
					bo.draw(g);
				}
			}
			
			if(base.isEnhanced() && enhancedtime == 0) {
				baseirons.clear();
				basewalls.clear();
				//基地墙-左
				for(int i = 0; i < 4; i++) {
					basewalls.add(new Wall(GAME_WIDTH / 2 - 60, GAME_HEIGHT - 20 - i * 20, 20, 20, this));
				}
				//基地墙-上
				for(int i = 0; i < 4; i++) {
					basewalls.add(new Wall(GAME_WIDTH / 2 - 40 + i * 20, GAME_HEIGHT - 80, 20, 20, this));
				}
				//基地墙-右
				for(int i = 0; i < 4; i++) {
					basewalls.add(new Wall(GAME_WIDTH / 2 + 40, GAME_HEIGHT - 20 - i * 20, 20, 20, this));
				}
				base.setEnhanced(false);
			}
			
			enhancedtime --;
			
			
			Color c = g.getColor();
			g.setColor(Color.GRAY);
			g.fillRect(GAME_WIDTH, 0, WINDOW_WIDTH - GAME_WIDTH, WINDOW_HEIGHT);
			g.setColor(c);
			
			int count = 0;
			for(int i = 0; i < total_enemy/2 + 1; i++) {
				if (count >= total_enemy) break;
				for(int j = 0; j < 2; j++) {
					g.drawImage(healthImage, 860 + j * 35, 60 + 35 * i, 30, 30, null);
					count ++;
					if (count >= total_enemy) break;
				}
			}
			
			int mycount = 0;
			for(int i = 0; i < Tank.myLife/3 + 1; i++) {
				if(mycount >= Tank.myLife) break;
				for(int j = 0; j < 3; j++) {
					g.drawImage(myhealthImage, 843 + j * 35, 500 + 35 * i, 30, 30, null);
					mycount ++;
					if (mycount >= Tank.myLife) break;
				}
			}
		}
		else if(!game_finished && !game_over){
			st.draw(g);
		}
		else ov.draw(g);
	}
	
	public void initMap() {
		if(basewalls.size() <= 0) {
			//基地墙-左
			for(int i = 0; i < 4; i++) {
				basewalls.add(new Wall(GAME_WIDTH / 2 - 60, GAME_HEIGHT - 20 - i * 20, 20, 20, this));
			}
			//基地墙-上
			for(int i = 0; i < 4; i++) {
				basewalls.add(new Wall(GAME_WIDTH / 2 - 40 + i * 20, GAME_HEIGHT - 80, 20, 20, this));
			}
			//基地墙-右
			for(int i = 0; i < 4; i++) {
				basewalls.add(new Wall(GAME_WIDTH / 2 + 40, GAME_HEIGHT - 20 - i * 20, 20, 20, this));
			}
		}
		
		if(walls.size() <= 0) {
			//左上
			for(int i = 0; i < 6; i++) {
				walls.add(new Wall(40 + i * 20, 70, 20, 20, this));
				walls.add(new Wall(40 + i * 20, 90, 20, 20, this));
			}
			for(int i = 0; i < 2; i++) {
				walls.add(new Wall(80, 50 - i * 20, 20, 20, this));
				walls.add(new Wall(100, 50 - i * 20, 20, 20, this));
			}
			//上
			for(int j = 0; j < 5; j++) {
				for(int i = 0; i < 5; i++) {
					walls.add(new Wall(300 + 20 * j, 30 + i * 20, 20, 20, this));
				}
			}
			for(int i = 0; i < 4; i++) {
				walls.add(new Wall(480, 50 + i * 20, 20, 20, this));
				walls.add(new Wall(500, 50 + i * 20, 20, 20, this));
			}
			for(int i = 0; i < 6; i++) {
				walls.add(new Wall(660, 30 + i * 20, 20, 20, this));
				walls.add(new Wall(680, 30 + i * 20, 20, 20, this));
			}
			for(int i = 0; i < 6; i++) {
				walls.add(new Wall(760, 30 + i * 20, 20, 20, this));
				walls.add(new Wall(780, 30 + i * 20, 20, 20, this));
			}
			for(int i = 0; i < 2; i++) {
				walls.add(new Wall(40, 190 + i * 20, 20, 20, this));
				walls.add(new Wall(60, 190 + i * 20, 20, 20, this));
			}
			for(int i = 0; i < 2; i++) {
				walls.add(new Wall(80, 230 + i * 20, 20, 20, this));
				walls.add(new Wall(100, 230 + i * 20, 20, 20, this));
			}
			for(int i = 0; i < 8; i++) {
				if(i == 5 || i == 4) continue;
				walls.add(new Wall(20 * i, 270, 20, 20, this));
				walls.add(new Wall(20 * i, 290, 20, 20, this));
			}
			for(int j = 0; j < 5; j++) {
				for(int i = 0; i < 9; i++) {
					walls.add(new Wall(300 + i * 20, 290 - j * 20, 20, 20, this));
				}
			}
			for(int i = 0; i < 9; i++) {
				walls.add(new Wall(480 + 20 * i, 230, 20, 20, this));
			}
			for(int i = 0; i < 5; i++) {
				walls.add(new Wall(80, 530 - 20 * i, 20, 20, this));
			}
			for(int i = 0; i < 5; i++) {
				walls.add(new Wall(100, 530 - 20 * i, 20, 20, this));
			}
			for(int i = 0; i < 5; i++) {
				walls.add(new Wall(180, 530 - 20 * i, 20, 20, this));
			}
			for(int i = 0; i < 3; i++) {
				walls.add(new Wall(160, 430 - 20 * i, 20, 20, this));
			}
			for(int i = 0; i < 16; i++) {
				walls.add(new Wall(200 + i * 20, 430, 20, 20, this));
				walls.add(new Wall(200 + i * 20, 450, 20, 20, this));
			}
			for(int i = 0; i < 3; i++) {
				walls.add(new Wall(600 + 20 * i, 470, 20, 20, this));
				walls.add(new Wall(600 + 20 * i, 570, 20, 20, this));
				walls.add(new Wall(600 + 20 * i, 590, 20, 20, this));
			}
			for(int i = 0; i < 4; i++) {
				walls.add(new Wall(660 + 20 * i, 470, 20, 20, this));
			}
			for(int i = 0; i < 3; i++) {
				walls.add(new Wall(700, 490 + 20 * i, 20, 20, this));
				walls.add(new Wall(720, 490 + 20 * i, 20, 20, this));
			}
		}
		
		if(irons.size() <= 0) {
			irons.add(new Iron(480, 130, 20, 20, this));
			irons.add(new Iron(500, 130, 20, 20, this));
			for(int i = 0; i < 2; i++) {
				irons.add(new Iron(120, 310 + 20 * i, 20, 20, this));
				irons.add(new Iron(140, 310 + 20 * i, 20, 20, this));
			}
			for(int i = 0; i < 4; i++) {
				irons.add(new Iron(240 + i * 20, 330, 20, 20, this));
			}
			for(int i = 0; i < 7; i++) {
				irons.add(new Iron(660 + 20 * i, 230, 20, 20, this));
			}
			for(int i = 0; i < 7; i++) {
				irons.add(new Iron(580 + 20 * i, 290, 20, 20, this));
			}
			irons.add(new Iron(40, 530, 20, 20, this));
			irons.add(new Iron(60, 530, 20, 20, this));
			for(int i = 0; i < 3; i++) {
				irons.add(new Iron(600 + 20 * i, 450, 20, 20, this));
				irons.add(new Iron(600 + 20 * i, 490, 20, 20, this));
				irons.add(new Iron(600 + 20 * i, 510, 20, 20, this));
			}
		}
		
		if(waters.size() <= 0) {
			//中上
			for(int i = 0; i < 20; i++) {
				if(i == 16 || i == 17) continue;
				waters.add(new Water(40 + 40 * i, 150, 40, 40, this));
			}
			//中下
			for(int i = 0; i < 20; i++) {
				if(i == 1 || i==2 || i == 12 || i == 13) continue;
				waters.add(new Water(i * 40, 350, 40, 40, this));
			}
		}
		
		if(grasses.size() <= 0) {
			//左上
			for(int i = 0; i < 3; i++) {
				grasses.add(new Grass(0, 70 + 40 * i, 40, 40, this));
			}
			for(int i = 0; i < 5; i++) {
				grasses.add(new Grass(40 + 40 * i, 110, 40, 40, this));
			}
			//中右
			for(int i = 0; i < 5; i++) {
				grasses.add(new Grass(i * 40 + 440, 310, 40, 40, this));
			}
			grasses.add(new Grass(480, 270, 40, 40, this));
			for(int i = 0; i < 2; i ++) {
				grasses.add(new Grass(i * 40, 390, 40, 40, this));
			}
			for(int i = 0; i < 2; i ++) {
				grasses.add(new Grass(i * 40, 430, 40, 40, this));
			}
			for(int i = 0; i < 2; i ++) {
				grasses.add(new Grass(i * 40, 470, 40, 40, this));
			}
			grasses.add(new Grass(0, 510, 40, 40, this));
		}
	}
	
	//利用双缓冲消除闪烁
	public void update(Graphics g) {
		if(offscreenImage == null) {
			offscreenImage = this.createImage(WINDOW_WIDTH, WINDOW_HEIGHT);
		}
		Graphics gOffScreen = offscreenImage.getGraphics();
		Color c = gOffScreen.getColor();
		gOffScreen.setColor(Color.BLACK);
		gOffScreen.fillRect(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);
		gOffScreen.setColor(c);
		paint(gOffScreen);
		g.drawImage(offscreenImage, 0, 0, null);
	}

	//绘制底部Frame
	public void launchFrame() {
		int initTankCount = Integer.parseInt(PropertyMgr.getProperty("initTankCount"));
		
		for(int i = 0; i < initTankCount; i++){
			int rtk = r.nextInt(tkk.length - 1) + 1;
			kind = tkk[rtk];
			if(kind == TankKind.enemyStrong) {
				tanks.add(new Tank(200 * (i + 1), 50, false, true, false, false, Direction.D, kind, this));
			}
			else {
				int rhb = r.nextInt(10);
				if(rhb > 7) {
					tanks.add(new Tank(200 * (i + 1), 50, false, false, true, false, Direction.D, kind, this));
				}
				else tanks.add(new Tank(200 * (i + 1), 50, false, false, false, false, Direction.D, kind, this));
			}
		}
		
		this.setLocation(250, 100);
		this.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
		this.setTitle("TankWar");
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		this.setResizable(false);
		this.setBackground(Color.BLACK);
		this.addKeyListener(new KeyMonitor());
		setVisible(true);
		bgm.loop();
		new Thread(new PaintThread()).start();
	}
	
	public static void main(String[] args) {
		TankClient tc = new TankClient();
		tc.launchFrame();
	}

	//重画线程
	private class PaintThread implements Runnable {
		public void run() {
			while(true) {
				repaint();
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	//键盘事件监听器
	private class KeyMonitor extends KeyAdapter {
		//键盘被按下
		public void keyPressed(KeyEvent e) {
			if(game_finished) {
				myTank.keyPressed(e);
				if (players == 2)
					myTank2.keyPressed2(e);
			}
			else {
				st.keyPressed(e);
			}
		}
		
		//键盘松开
		public void keyReleased(KeyEvent e) {
			if(game_finished) {
				myTank.keyReleased(e);
				if (players == 2)
					myTank2.keyReleased2(e);
			}
		}
	}

	public static boolean isGame_finished() {
		return game_finished;
	}

	public static void setGame_finished(boolean game_finished) {
		TankClient.game_finished = game_finished;
	}
}
