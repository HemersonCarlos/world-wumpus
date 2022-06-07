package com.dev.wumpusworld.view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class WumpusPanel extends JPanel implements KeyListener, ActionListener {

	public static final int PLAYING = 0;
	public static final int DEAD = 2;
	public static final int WON = 1;
	private static boolean displayFog = true;
	private static boolean wumpusKilled = false;
	private boolean gameOver = false;
	int status;
	String currentPath = null;
	Image arrow;
	Image breeze;
	Image deadWumpus;
	Image floor;
	Image gold;
	Image ladder;
	Image pit;
	Image playerDown;
	Image playerLeft;
	Image playerRight;
	Image playerUp;
	Image wumpus;
	Image stench;
	BufferedImage bufferedImage = null;
	WumpusPlayer wumpusPlayer;
	WumpusMap wumpusMap;
	List<WumpusMap> listBreeze = new ArrayList<WumpusMap>();
	List<WumpusMap> listStench = new ArrayList<WumpusMap>();

	public WumpusPanel() {
		setSize(500, 700);
		bufferedImage = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_4BYTE_ABGR);

		try {
			currentPath = Paths.get(WumpusPanel.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getParent().toString();
		} catch (URISyntaxException uriSyntaxException) {
			System.err.println("Error: " + uriSyntaxException.getMessage());
		}

		try {
			arrow = ImageIO.read((new File(currentPath + "\\resource\\image\\arrow.gif")));
			breeze = ImageIO.read((new File(currentPath + "\\resource\\image\\breeze.gif")));
			deadWumpus = ImageIO.read((new File(currentPath + "\\resource\\image\\deadwumpus.GIF")));
			floor = ImageIO.read((new File(currentPath + "\\resource\\image\\floor.gif")));
			gold = ImageIO.read((new File(currentPath + "\\resource\\image\\gold.gif")));
			ladder = ImageIO.read((new File(currentPath + "\\resource\\image\\ladder.gif")));
			pit = ImageIO.read((new File(currentPath + "\\resource\\image\\pit.gif")));
			playerDown = ImageIO.read((new File(currentPath + "\\resource\\image\\playerDown.png")));
			playerLeft = ImageIO.read((new File(currentPath + "\\resource\\image\\playerLeft.png")));
			playerRight = ImageIO.read((new File(currentPath + "\\resource\\image\\playerRight.png")));
			playerUp = ImageIO.read((new File(currentPath + "\\resource\\image\\playerUp.png")));
			stench = ImageIO.read((new File(currentPath + "\\resource\\image\\stench.gif")));
			wumpus = ImageIO.read((new File(currentPath + "\\resource\\image\\wumpus.gif")));

		} catch (Exception exception) {
			System.err.println("Error Loading Images: " + exception.getMessage());
			System.exit(-1);
		}
		addKeyListener(this);
		reset();
	}

	private void reset() {
		status = PLAYING;
		wumpusMap = new WumpusMap();
		wumpusPlayer = new WumpusPlayer();
		wumpusPlayer.setColumnPosition(wumpusMap.getLadderColumn());
		wumpusPlayer.setRowPosition(wumpusMap.getLadderRow());
		wumpusMap.getSquare(wumpusPlayer.getRowPosition(), wumpusPlayer.getColumnPosition()).setVisited(true);
	}

	@Override
	public void keyTyped(KeyEvent keyEvent) {

		switch (keyEvent.getKeyChar()) {
		case 'w':
			this.moveNorth();
			break;
		case 's':
			this.moveSouth();
			break;
		case 'a':
			this.moveWest();
			break;
		case 'd':
			this.moveEast();
			break;
		case 'i': 
			this.shootNorth();
			break;
		case 'k':
			this.shootSouth();
			break;
		case 'j':
			this.shootWest();
			break;
		case 'l':
			this.shootEast();
			break;
		case 'c': // Climb ladder
			if (wumpusPlayer.getRowPosition() == wumpusMap.getLadderRow() && wumpusPlayer.getColumnPosition() == wumpusMap.getLadderColumn() && wumpusPlayer.isGold()) {
				status = WON;
			}
			break;
		case 'p':
			this.collectElements();
			break;
		case 'n': // New game
			if (status == WON || status == DEAD) {
				reset();
				repaint();
			}
			break;
		case '*': // Toggle cheat mode
			displayFog = !displayFog;
			break;
		case '-':
			System.out.println("Map is: " + wumpusMap.toString());
			break;
		case '.':
			this.solve();
			break;
		}

		repaint();

		// check if stepped on wumpus
		if (wumpusMap.getSquare(wumpusPlayer.getRowPosition(), wumpusPlayer.getColumnPosition()).isWumpus() || wumpusMap.getSquare(wumpusPlayer.getRowPosition(), wumpusPlayer.getColumnPosition()).isPit()) {
			status = DEAD;
			System.out.println("Player is dead.");
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent actionEvent) {
		
	}

	private void solve() {
		while(!gameOver) {
			System.out.println("Executando");
			this.checkCurrentPoint(wumpusPlayer);
			this.exploreOtherWay(wumpusPlayer, wumpusMap);
			repaint();
			System.out.println("---------");
			this.delayGame();
		}	
	}

	private void checkCurrentPoint(WumpusPlayer wumpusPlayer) {
		if (wumpusMap.getSquare(wumpusPlayer.getRowPosition(), wumpusPlayer.getColumnPosition()).isGold()) {
			System.out.println("You see a glimmer.");
			this.collectElements();
		}
		if (wumpusMap.getSquare(wumpusPlayer.getRowPosition(), wumpusPlayer.getColumnPosition()).isDeadWumpus()) {
			System.out.println("Wumpus is dead.....");
			System.out.println("I'll collected your corpse!!");
			this.collectElements();
		}
		if (wumpusPlayer.getRowPosition() == wumpusMap.getLadderRow() && wumpusPlayer.getColumnPosition() == wumpusMap.getLadderColumn() && wumpusPlayer.isGold()) {
            status = WON;
            gameOver = true;
        }
		if (wumpusMap.getSquare(wumpusPlayer.getRowPosition(), wumpusPlayer.getColumnPosition()).isBreeze()) {
			listBreeze.add(wumpusMap);
		}
		if (wumpusMap.getSquare(wumpusPlayer.getRowPosition(), wumpusPlayer.getColumnPosition()).isStench()) {
			listStench.add(wumpusMap);
		}
	}

	private void exploreOtherWay(WumpusPlayer wumpusPlayer, WumpusMap wumpusMap) {
		
		if(this.isValidate(wumpusPlayer, wumpusMap)) {
			System.out.println("Moving east..");
			this.moveEast();
			return;
		}
		
		if(this.isValidate(wumpusPlayer, wumpusMap)) {
			System.out.println("Moving north..");
			this.moveNorth();
			return;
		}
		
		if(this.isValidate(wumpusPlayer, wumpusMap)) {
			System.out.println("Moving west..");
			this.moveWest();
			return;
		}
		
		if(this.isValidate(wumpusPlayer, wumpusMap)) {
			System.out.println("Moving south..");
			this.moveSouth();
			return;
		}
	}

	private boolean isValidate(WumpusPlayer wumpusPlayer, WumpusMap wumpusMap) {
		boolean Validated = true;
		if(wumpusMap.getSquare(wumpusPlayer.getRowPosition(), wumpusPlayer.getColumnPosition()).isBreeze()) {
			if(!listBreeze.contains(wumpusMap)) {
				listBreeze.add(wumpusMap);
			}
			Validated = true;
		}
		if(wumpusMap.getSquare(wumpusPlayer.getRowPosition(), wumpusPlayer.getColumnPosition()).isPit()) {
			Validated = false;
		}
		if(wumpusMap.getSquare(wumpusPlayer.getRowPosition(), wumpusPlayer.getColumnPosition()).isStench()) {
			if(!listStench.contains(wumpusMap)) {
				listStench.add(wumpusMap);
			}
			Validated = true;
		}
		if(wumpusMap.getSquare(wumpusPlayer.getRowPosition(), wumpusPlayer.getColumnPosition()).isWumpus()) {
			Validated = false;
		}
		return Validated;
	}
	
	private void moveEast() {
		if (status == PLAYING) {
			if (wumpusPlayer.getColumnPosition() + 1 < 10) {
				wumpusKilled = false;
				wumpusPlayer.setColumnPosition(wumpusPlayer.getColumnPosition() + 1);
				wumpusPlayer.setDirection(WumpusPlayer.EAST);
				wumpusMap.getSquare(wumpusPlayer.getRowPosition(), wumpusPlayer.getColumnPosition()).setVisited(true);
			}
		}
	}

	private void moveNorth() {
		if (status == PLAYING) {
				if (wumpusPlayer.getRowPosition() - 1 >= 0) {
				wumpusKilled = false;
				wumpusPlayer.setRowPosition(wumpusPlayer.getRowPosition() - 1);
				wumpusPlayer.setDirection(WumpusPlayer.NORTH);
				wumpusMap.getSquare(wumpusPlayer.getRowPosition(), wumpusPlayer.getColumnPosition()).setVisited(true);
			}
		}
	}
	
	private void moveWest() {
		if (status == PLAYING) {
			if (wumpusPlayer.getColumnPosition() - 1 >= 0) {
				wumpusKilled = false;
				wumpusPlayer.setColumnPosition(wumpusPlayer.getColumnPosition() - 1);
				wumpusPlayer.setDirection(WumpusPlayer.WEST);
				wumpusMap.getSquare(wumpusPlayer.getRowPosition(), wumpusPlayer.getColumnPosition()).setVisited(true);
			}
		}
	}
	
	private void moveSouth() {
		if (status == PLAYING) {
			if (wumpusPlayer.getRowPosition() + 1 < 10) {
				wumpusKilled = false;
				wumpusPlayer.setRowPosition(wumpusPlayer.getRowPosition() + 1);
				wumpusPlayer.setDirection(WumpusPlayer.SOUTH);
				wumpusMap.getSquare(wumpusPlayer.getRowPosition(), wumpusPlayer.getColumnPosition()).setVisited(true);
			}
		}
	}
	
	private void shootNorth() {
		if (wumpusPlayer.isArrow()) {
			wumpusPlayer.setArrow(false);
			for (int y = wumpusPlayer.getRowPosition(); y >= 0; y--) {
				if (wumpusMap.getSquare(y, wumpusPlayer.getColumnPosition()).isWumpus()) {
					System.out.println("Wumpus killed.");
					wumpusKilled = true;
					wumpusMap.getSquare(y, wumpusPlayer.getColumnPosition()).setDeadWumpus(true);
				}
			}
		}
	}
	
	private void shootEast() {
		if (wumpusPlayer.isArrow()) {
			wumpusPlayer.setArrow(false);
			for (int x = wumpusPlayer.getColumnPosition(); x < 10; x++) {
				if (wumpusMap.getSquare(wumpusPlayer.getRowPosition(), x).isWumpus()) {
					System.out.println("Wumpus killed.");
					wumpusKilled = true;
					wumpusMap.getSquare(wumpusPlayer.getRowPosition(), x).setDeadWumpus(true);
				}
			}
		}
	}
	
	private void shootWest() {
		if (wumpusPlayer.isArrow()) {
			wumpusPlayer.setArrow(false);
			for (int x = wumpusPlayer.getColumnPosition(); x >= 0; x--) {
				if (wumpusMap.getSquare(wumpusPlayer.getRowPosition(), x).isWumpus()) {
					System.out.println("Wumpus killed.");
					wumpusKilled = true;
					wumpusMap.getSquare(wumpusPlayer.getRowPosition(), x).setDeadWumpus(true);
				}
			}
		}
	}
	
	private void shootSouth() {
		if (wumpusPlayer.isArrow()) {
			wumpusPlayer.setArrow(false);
			for (int y = wumpusPlayer.getRowPosition(); y < 10; y++) {
				if (wumpusMap.getSquare(y, wumpusPlayer.getColumnPosition()).isWumpus()) {
					System.out.println("Wumpus killed.");
					wumpusKilled = true;
					wumpusMap.getSquare(y, wumpusPlayer.getColumnPosition()).setDeadWumpus(true);
				}
			}
		}
	}

	private void collectElements() {
		if (wumpusMap.getSquare(wumpusPlayer.getRowPosition(), wumpusPlayer.getColumnPosition()).isDeadWumpus()) {
			wumpusMap.getSquare(wumpusPlayer.getRowPosition(), wumpusPlayer.getColumnPosition()).setDeadWumpus(false);
			wumpusPlayer.setWumpusCorpse(true);
			System.out.println("Collected wumpus corpse");
		}
		if (wumpusMap.getSquare(wumpusPlayer.getRowPosition(), wumpusPlayer.getColumnPosition()).isGold()) {
			wumpusMap.getSquare(wumpusPlayer.getRowPosition(), wumpusPlayer.getColumnPosition()).setGold(false);
			wumpusPlayer.setGold(true);
			System.out.println("Collected gold");
		}
	}

	private void delayGame() {
		try {
			Thread.sleep(4000);
		} catch (InterruptedException interruptedException) {
			System.err.println("Error: " + interruptedException.getMessage());
			System.exit(-1);
		}
	}

	@Override
	public void paint(Graphics graph) {
		Graphics graphics = bufferedImage.getGraphics();

		for (int rowCount = 0; rowCount < WumpusMap.NUM_ROWS; rowCount++) {
			for (int columnCount = 0; columnCount < WumpusMap.NUM_COLUMNS; columnCount++) {

				graphics.drawImage(floor, columnCount * 50, rowCount * 50, null);

				if (wumpusMap.getSquare(rowCount, columnCount).isGold()) { // print gold
					graphics.drawImage(gold, columnCount * 50, rowCount * 50, null);
				}
				if (wumpusMap.getSquare(rowCount, columnCount).isPit()) { // print pit
					graphics.drawImage(pit, columnCount * 50, rowCount * 50, null);
				}
				if (wumpusMap.getSquare(rowCount, columnCount).isBreeze() && !wumpusMap.getSquare(rowCount, columnCount).isPit()) { // print breeze
					graphics.drawImage(breeze, columnCount * 50, rowCount * 50, null);
				}
				if (wumpusMap.getSquare(rowCount, columnCount).isStench() && !wumpusMap.getSquare(rowCount, columnCount).isPit()) { // print stench
					graphics.drawImage(stench, columnCount * 50, rowCount * 50, null);
				}
				if (wumpusMap.getSquare(rowCount, columnCount).isWumpus() && !wumpusMap.getSquare(rowCount, columnCount).isPit()) { // print wumpus
					graphics.drawImage(wumpus, columnCount * 50, rowCount * 50, null);
				}
				if (wumpusMap.getSquare(rowCount, columnCount).isDeadWumpus() && !wumpusMap.getSquare(rowCount, columnCount).isPit()) { // print dead wumpus
					graphics.drawImage(deadWumpus, columnCount * 50, rowCount * 50, null);
				}
				if (wumpusMap.getSquare(rowCount, columnCount).isLadder()) { // print ladder
					graphics.drawImage(ladder, columnCount * 50, rowCount * 50, null);
				}
				if (!wumpusMap.getSquare(rowCount, columnCount).isVisited() && displayFog) {
					graphics.setColor(Color.black);
					graphics.fillRect(columnCount * 50, rowCount * 50, 50, 50);
				}

				graphics.setColor(Color.white);
				graphics.fillRect(0, 500, 250, 700);

				graphics.setColor(Color.black);
				graphics.drawLine(250, 500, 500, 500);

				graphics.setColor(Color.white);
				graphics.fillRect(255, 500, 500, 700);

				graphics.setColor(Color.black);
				graphics.drawString("Inventory:", 0, 520);

				// inventory
				if (wumpusPlayer.isArrow()) {
					graphics.drawImage(arrow, 0, 550, null);
				}
				if (wumpusPlayer.isWumpusCorpse()) {
					graphics.drawImage(deadWumpus, 0, 550, null);
				}
				if (wumpusPlayer.isGold()) {
					graphics.drawImage(gold, 50, 550, null);
				}

				graphics.setColor(Color.black);
				graphics.drawString("Instruction:", 0, 650);
				graphics.drawString("Press . for agent to solve!", 0, 665);

				// messages handling
				graphics.drawString("Messages:", 300, 520);
				if (wumpusMap.getSquare(wumpusPlayer.getRowPosition(), wumpusPlayer.getColumnPosition()).isLadder()) {
					graphics.setColor(Color.black);
					if (wumpusPlayer.isGold()) {
						status = WON;
						graphics.drawString("You climb the ladder.", 300, 550);
					} else {
						graphics.drawString("You bump into the ladder.", 300, 550);
					}
				}
				if (wumpusMap.getSquare(wumpusPlayer.getRowPosition(), wumpusPlayer.getColumnPosition()).isBreeze()) {
					graphics.setColor(Color.black);
					graphics.drawString("You feel a breeze.", 300, 570);
				}
				if (wumpusMap.getSquare(wumpusPlayer.getRowPosition(), wumpusPlayer.getColumnPosition()).isStench()) {
					graphics.setColor(Color.black);
					graphics.drawString("You smell a smelly smell.", 300, 590);
					graphics.drawString("It smells... Smelly.", 300, 600);
					graphics.drawString("Press I, J, K, L to shoot.", 300, 620);
				}
				if (wumpusMap.getSquare(wumpusPlayer.getRowPosition(), wumpusPlayer.getColumnPosition()).isGold()) {
					graphics.setColor(Color.black);
					graphics.drawString("You see a glimmer.", 300, 620);
					graphics.drawString("Press p to pick up the glimmer.", 300, 690);
				}
				if (wumpusMap.getSquare(wumpusPlayer.getRowPosition(), wumpusPlayer.getColumnPosition()).isPit()) {
					graphics.setColor(Color.black);
					graphics.drawString("You fell down a pit.", 300, 640);
					graphics.drawString("Press n to reset.", 300, 690);
				}
				if (wumpusMap.getSquare(wumpusPlayer.getRowPosition(), wumpusPlayer.getColumnPosition()).isWumpus()) {
					graphics.setColor(Color.black);
					graphics.drawString("You don been eated, fool!", 300, 660);
					graphics.drawString("Press n to reset.", 300, 690);
				}
				if (status == WON) {
					graphics.drawString("You won the game!", 300, 680);
					graphics.drawString("Press n to reset.", 300, 690);
				}
				if (wumpusKilled) { // if the wumpus has been killed, display scream heard
					graphics.drawString("You hear a scream!", 300, 650);
				}
			}
		}

		// player display
		if (wumpusPlayer.getDirection() == WumpusPlayer.NORTH) {
			graphics.drawImage(playerUp, wumpusPlayer.getColumnPosition() * 50, wumpusPlayer.getRowPosition() * 50, null); // draw the player
		} else if (wumpusPlayer.getDirection() == WumpusPlayer.SOUTH) {
			graphics.drawImage(playerDown, wumpusPlayer.getColumnPosition() * 50, wumpusPlayer.getRowPosition() * 50, null); // draw the player
		} else if (wumpusPlayer.getDirection() == WumpusPlayer.EAST) {
			graphics.drawImage(playerRight, wumpusPlayer.getColumnPosition() * 50, wumpusPlayer.getRowPosition() * 50, null); // draw the player
		} else if (wumpusPlayer.getDirection() == WumpusPlayer.WEST) {
			graphics.drawImage(playerLeft, wumpusPlayer.getColumnPosition() * 50, wumpusPlayer.getRowPosition() * 50, null); // draw the player
		}

		graph.drawImage(bufferedImage, 0, 0, null);
	}

	@Override
	public void addNotify() {
		super.addNotify();
		requestFocus();
	}

	@Override
	public void keyPressed(KeyEvent keyEvent) {
	}

	@Override
	public void keyReleased(KeyEvent keyEvent) {
	}
}