package com.maki.UnhappyDevil.Screens;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import com.maki.UnhappyDevil.DevilPanel;
import com.maki.UnhappyDevil.Drawable;

public class ScoreScreen implements Drawable {
  private int score;
//  private int highScore;
  private DevilPanel panel;

  public ScoreScreen(int gold, int killCount, int levelInDungeon, int playerLevel, int xp,
      DevilPanel devilPanel) {
    score = gold * 10 + killCount * 100 + levelInDungeon * 100 + playerLevel * 100 + xp;
    panel = devilPanel;
    intiKeyBindings();
//    highScore = panel.getHighScore();
  }

  private void intiKeyBindings() {
    Action action = new AbstractAction() {

      @Override
      public void actionPerformed(ActionEvent arg0) {
        panel.loadGame();
        panel.getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW).remove(
            KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0));
      }
    };

    Action menuScreen = new AbstractAction() {

      @Override
      public void actionPerformed(ActionEvent arg0) {
        panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).remove(KeyStroke.getKeyStroke('m'));
        panel.restartGame();
      }
    };
    panel.getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0), "reload game");
    panel.getActionMap().put("reload game", action);

    panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke('m'),"menuScreen");
    panel.getActionMap().put("menuScreen", menuScreen);
  }

  @Override
  public void paint(Graphics g) {
    Font tmpFont = g.getFont();
    g.setColor(Color.WHITE);
    // TODO change this to variables
    g.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 30));
    g.drawString("You Died", 400, 350);
    g.drawString("Score: " + score, 390, 380);
    g.drawString("Press Space To Restart", 305, 410);
    g.drawString("Or Press m To Go To Menu", 270, 440);
//    if(score > highScore) {
//      g.drawString("New Highscore!", 350, 470);
//      panel.writeHighScore(score);
//    } else {
//      g.drawString("Highscore: " + highScore, 350, 470);      
//    }
    g.setFont(tmpFont);
  }

}
