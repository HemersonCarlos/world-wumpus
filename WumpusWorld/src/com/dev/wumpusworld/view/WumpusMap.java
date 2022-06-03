package com.dev.wumpusworld.view;

import com.dev.wumpusworld.model.Point;

public class WumpusMap {

    public static final int NUM_ROWS = 10;
    public static final int NUM_COLUMNS = 10; 
    public static final int NUM_PITS = 10;
    private int ladderC;
    private int ladderR;
    private WumpusSquare[][] wumpusSquares;
    private Point currentPoint;
    
    
    public WumpusMap() {
        this.createMap();
    }

    public int getLadderColumn() {
        return ladderC;
    }

    public int getLadderRow() {
        return ladderR;
    }

    public Point getCurrentPoint() {
    	return currentPoint;
    }
    
    public WumpusSquare getSquare(int row, int column) {
        if (row < 0 || row >= NUM_ROWS || column < 0 || column >= NUM_COLUMNS) {
            return null;
        }
        return wumpusSquares[row][column];
    }
    
    private void createMap() {
    	wumpusSquares = new WumpusSquare[NUM_ROWS][NUM_COLUMNS];

        for (int rowsCount = 0; rowsCount < NUM_ROWS; rowsCount++) {
            for (int columnsCount = 0; columnsCount < NUM_COLUMNS; columnsCount++) {
            	wumpusSquares[rowsCount][columnsCount] = new WumpusSquare();
            }
        }

        for (int pitsCount = 0; pitsCount < NUM_PITS; pitsCount++) {
            boolean success = false;
            
            while (!success) {
                int randomRow = (int) (Math.random() * NUM_ROWS);
                int randomColumn = (int) (Math.random() * NUM_COLUMNS);
                
                if (!wumpusSquares[randomRow][randomColumn].isPit()) {
                	wumpusSquares[randomRow][randomColumn].setPit(true);
                	
                    if (randomRow - 1 >= 0) { //left
                    	wumpusSquares[randomRow - 1][randomColumn].setBreeze(true);
                        success = true;
                    }
                    if (randomRow + 1 < 10) { //right
                    	wumpusSquares[randomRow + 1][randomColumn].setBreeze(true);
                        success = true;
                    }
                    if (randomColumn - 1 >= 0) { //up
                    	wumpusSquares[randomRow][randomColumn - 1].setBreeze(true);
                        success = true;
                    }
                    if (randomColumn + 1 < 10) { //down
                    	wumpusSquares[randomRow][randomColumn + 1].setBreeze(true);
                        success = true;
                    }
                }
            }
        }

        while (true) {
        	int randomRow = (int) (Math.random() * NUM_ROWS);
        	int randomColumn = (int) (Math.random() * NUM_COLUMNS);

            if (!wumpusSquares[randomRow][randomColumn].isPit())
            {
            	wumpusSquares[randomRow][randomColumn].setGold(true);
                System.out.println("Gold is at: " + randomColumn + "," + randomRow);
                break;
            }
        }

        while (true) {
            int randomRow = (int) (Math.random() * NUM_ROWS);
            int randomColumn = (int) (Math.random() * NUM_COLUMNS);
            
            if (!wumpusSquares[randomRow][randomColumn].isPit() && !wumpusSquares[randomRow][randomColumn].isGold()) {
            	wumpusSquares[randomRow][randomColumn].setWumpus(true);
                System.out.println("Wumpus is at: " + randomColumn + "," + randomRow);
                
                if (randomRow - 1 >= 0) { //left
                	wumpusSquares[randomRow - 1][randomColumn].setStench(true);
                }
                if (randomRow + 1 < 10) { //right
                	wumpusSquares[randomRow + 1][randomColumn].setStench(true);
                }
                if (randomColumn - 1 >= 0) { //up
                	wumpusSquares[randomRow][randomColumn - 1].setStench(true);
                }
                if (randomColumn + 1 < 10) { //down
                	wumpusSquares[randomRow][randomColumn + 1].setStench(true); 
                }
                break;
            }
        }

        while (true) {
            int randomRow = (int) (Math.random() * NUM_ROWS);
            int randomColumn = (int) (Math.random() * NUM_COLUMNS);
            
            if (!wumpusSquares[randomRow][randomColumn].isPit() && !wumpusSquares[randomRow][randomColumn].isWumpus() && !wumpusSquares[randomRow][randomColumn].isGold()) {
            	wumpusSquares[randomRow][randomColumn].setLadder(true);
                ladderC = randomColumn;
                ladderR = randomRow;
                System.out.println("Ladder is at: " + randomColumn + "," + randomRow);
                currentPoint = new Point(randomColumn, randomRow);
                break;
            }
        }

    }

    public String toString() {
        String finalString = "";
        for (int countColumn = 0; countColumn < NUM_COLUMNS; countColumn++) {
            finalString += "\n";
            for (int countRow = 0; countRow < NUM_ROWS; countRow++) {
                finalString += wumpusSquares[countColumn][countRow].toString() + " ";
            }
        }
        return finalString;
    }
}