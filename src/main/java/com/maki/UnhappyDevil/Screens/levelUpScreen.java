package com.maki.UnhappyDevil.Screens;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import com.maki.UnhappyDevil.DevilPanel;
import com.maki.UnhappyDevil.Drawable;
import com.maki.UnhappyDevil.Entities.Player;

public class levelUpScreen implements Drawable {
  Player player;
  List<Drawable> list;
  DevilPanel panel;

  public levelUpScreen(Player player, List<Drawable> list, DevilPanel panel) {
    super();
    this.player = player;
    this.list = list;
    this.panel = panel;
    initKeyBindings();
  }

  private void initKeyBindings() {
    Action addMaxHealth = new AbstractAction() {

      @Override
      public void actionPerformed(ActionEvent arg0) {
        player.addMaxHealth(5);
        panel.setDrawableElements(list);
        panel.getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW).remove(KeyStroke.getKeyStroke('1'));
        panel.getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW).remove(KeyStroke.getKeyStroke('1'));
        panel.getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW).remove(KeyStroke.getKeyStroke('3'));
        panel.repaint();
      }
    };

    Action addDamage = new AbstractAction() {

      @Override
      public void actionPerformed(ActionEvent arg0) {
        player.addDamage(1);
        panel.setDrawableElements(list);
        panel.getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW).remove(KeyStroke.getKeyStroke('1'));
        panel.getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW).remove(KeyStroke.getKeyStroke('2'));
        panel.getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW).remove(KeyStroke.getKeyStroke('3'));
        panel.repaint();
      }
    };
    
    Action addHealth = new AbstractAction() {
      @Override
      public void actionPerformed(ActionEvent arg0) {
        player.addHealth(5);
        panel.setDrawableElements(list);
        panel.getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW).remove(KeyStroke.getKeyStroke('1'));
        panel.getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW).remove(KeyStroke.getKeyStroke('2'));
        panel.getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW).remove(KeyStroke.getKeyStroke('3'));
        panel.repaint();
      }
    };

    panel.getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke('1'), "add max health");
    panel.getActionMap().put("add max health", addMaxHealth);
    
    panel.getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke('2'), "add health");
    panel.getActionMap().put("add health", addHealth);

    panel.getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke('3'), "add damage");
    panel.getActionMap().put("add damage", addDamage);

  }

  @Override
  public void paint(Graphics g) {
    Font tmpFont = g.getFont();
    g.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 30));
    g.setColor(Color.WHITE);
    //TODO calculate this later
    g.drawString("Choose:", 320, 320);
    g.drawString("[1] +5 max health", 320, 370);
    g.drawString("[2] +5 normal health", 320, 420);
    g.drawString("[3] +1 damage", 320, 470);
    g.setFont(tmpFont);
  }

}
