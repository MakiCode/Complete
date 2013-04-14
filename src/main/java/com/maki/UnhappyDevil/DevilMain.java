package com.maki.UnhappyDevil;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;

public class DevilMain {
  public static final int FRAME_WIDTH = 920;
  public static final int FRAME_HEIGHT = 768;
  
  public static void main(String... args) {
    JFrame frame = new JFrame();
    DevilPanel devilPanel = new DevilPanel(FRAME_WIDTH, FRAME_HEIGHT); 
    frame.add(devilPanel);
    frame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
    Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
    // Determine the new location of the window
    int w = frame.getSize().width;
    int h = frame.getSize().height;
    int x = (dim.width-w)/2;
    int y = (dim.height-h)/2;
    // Move the window
    frame.setLocation(x, y);
    

    frame.setResizable(false);
    frame.setFocusable(true);
    frame.setVisible(true);

  }
}
