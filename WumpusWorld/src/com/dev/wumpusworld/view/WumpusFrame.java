package com.dev.wumpusworld.view;

import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Panel;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

public class WumpusFrame extends JFrame {

	public WumpusFrame() {
        super("Wumpus World");

        WumpusPanel wumpusPanel = new WumpusPanel();

        pack();
        Insets insets = getInsets();
        int frameWidth = wumpusPanel.getWidth() + insets.left + insets.right;
        int frameHeight = wumpusPanel.getHeight() + insets.top + insets.bottom;
        add(wumpusPanel);

        Panel panel = new Panel();
        add(panel);

        setPreferredSize(new Dimension(frameWidth, frameHeight));
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);
        setFocusable(true);
        pack();
        setVisible(true);
    }
}