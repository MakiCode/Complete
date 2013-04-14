package com.maki.UnhappyDevil;

import java.awt.Color;
import java.awt.Graphics;

public class TextDisplay implements Drawable {

  private int displayWidth;
  private String textToDisplay = "Welcome to Hell! Your job is to fight your way out of the endless levels of hell. good luck!";
  private boolean firstPaint;

  public TextDisplay(int displayWidth) {
    super();
    this.displayWidth = displayWidth;
  }

  public void displayText(String text) {
    textToDisplay = text;
  }

  // TODO change this to make it a stack so that the first message runs to
  // completion then the next then the next ect.
  public void paint(Graphics g) {
    if (firstPaint) {
      String tmpString = "";
      if (textToDisplay.length() * 10 > displayWidth) {
        int index = textToDisplay.length() / 2;
        tmpString = textToDisplay.substring(index, index + 1);
        while (!tmpString.equals(" ")) {
          index--;
          tmpString = textToDisplay.substring(index, index + 1);
        }
        tmpString = textToDisplay.substring(index);
        textToDisplay = textToDisplay.substring(0, index) + " --more--";
      }
      g.setColor(Color.WHITE);
      g.drawString(textToDisplay, 0, 15);
      textToDisplay = tmpString;
    } else {
      firstPaint = true;
    }
  }
}
