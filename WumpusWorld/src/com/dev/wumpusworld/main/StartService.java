package com.dev.wumpusworld.main;

import com.dev.wumpusworld.view.WumpusFrame;

public class StartService {

	public static void main(String[] args) {
		System.out.println("Author: Hemerson Carlos");
        System.out.println("Game initialised. Press * for cheat mode, and - for display of map.");
		new WumpusFrame();
	}
}