package com.dev.wumpusworld.view;

public class WumpusPlayer {
    public static final int NORTH = 0;
    public static final int EAST = 1;
    public static final int SOUTH = 2;
    public static final int WEST = 3;
    private int direction;
    private boolean arrow;
    private boolean gold;
    private boolean wumpusCorpse;
    private int columnPosition;
    private int rowPosition;

    public WumpusPlayer() {
        direction = NORTH;
        gold = false;
        wumpusCorpse = false;
        arrow = true;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public boolean isArrow() {
        return arrow;
    }

    public void setArrow(boolean arrow) {
        this.arrow = arrow;
    }

    public boolean isGold() {
        return gold;
    }

    public void setGold(boolean gold) {
        this.gold = gold;
    }

    public boolean isWumpusCorpse() {
		return wumpusCorpse;
	}

	public void setWumpusCorpse(boolean wumpusCorpse) {
		this.wumpusCorpse = wumpusCorpse;
	}

	public int getColumnPosition() {
        return columnPosition;
    }

    public void setColumnPosition(int colPosition) {
        this.columnPosition = colPosition;
    }

    public int getRowPosition() {
        return rowPosition;
    }

    public void setRowPosition(int rowPosition) {
        this.rowPosition = rowPosition;
    }

}