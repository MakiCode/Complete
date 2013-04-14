package test;

import java.awt.Graphics;

import javax.swing.JPanel;

import com.maki.UnhappyDevil.Screens.ErrorScreen;

public class TestPanel extends JPanel{
  ErrorScreen errorScreen; 
  public TestPanel() {
    try {
      throw new StackOverflowError("Oh no! Stack overflow!");
    } catch (Error e) {
      errorScreen = new ErrorScreen(e, this);
    }
    repaint();
  }
  
  public void paint(Graphics g) {
    errorScreen.paint(g);
  }
}
