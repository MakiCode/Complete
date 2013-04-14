package test;

import javax.swing.JFrame;

public class Main {

  /**
   * @param args
   */
  public static void main(String[] args) {
    JFrame frame = new JFrame();
    frame.setSize(1000, 1000);
    frame.setResizable(false);
    frame.setFocusable(true);
    frame.add(new TestPanel());
    frame.setVisible(true);
    
  }

}
