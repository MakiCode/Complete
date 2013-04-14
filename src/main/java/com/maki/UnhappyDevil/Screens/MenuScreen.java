package com.maki.UnhappyDevil.Screens;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import com.maki.UnhappyDevil.DevilPanel;
import com.maki.UnhappyDevil.Drawable;

public class MenuScreen implements Drawable {
  DevilPanel panel;

  public MenuScreen(DevilPanel panel) {
    this.panel = panel;
    initKeyBindings();
  }

  private void initKeyBindings() {
    Action loadGame = new AbstractAction() {
      
      @Override
      public void actionPerformed(ActionEvent arg0) {
        panel.getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW).remove(KeyStroke.getKeyStroke('a'));
        panel.displayStartScreen();
      }
    };
    
    panel.getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke('a'),"loadGame");
    panel.getActionMap().put("loadGame", loadGame);
  }

  @Override
  public void paint(Graphics g) {
    Font tmpFont = g.getFont();
    g.setColor(Color.WHITE);
    g.setFont(new Font(Font.SANS_SERIF, Font.ITALIC, 50));
    //TODO decide these variables using math 
    g.drawString("UNHAPPY DEVIL", 250, 300);
    g.setFont(new Font(Font.SANS_SERIF, Font.ITALIC, 30));
    g.drawString("[a] Start New Game", 300, 400);
//    g.drawString("Highscore: " + panel.getHighScore(), 300, 500);
    g.setFont(tmpFont);
  }

}
