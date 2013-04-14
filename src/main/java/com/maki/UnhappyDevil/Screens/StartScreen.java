package com.maki.UnhappyDevil.Screens;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.KeyStroke;

import com.maki.UnhappyDevil.DevilPanel;
import com.maki.UnhappyDevil.Drawable;

public class StartScreen implements Drawable {
  DevilPanel panel;
  
  
  public StartScreen(DevilPanel panel) {
    this.panel = panel;
    initKeyBindings();
  }


  private void initKeyBindings() {
    Action contine = new AbstractAction() {
      
      @Override
      public void actionPerformed(ActionEvent arg0) {
        panel.getInputMap().clear();
        panel.loadGame();
      }
    };
    
    panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke('a'), "continue");
    panel.getActionMap().put("continue", contine);
  }


  @Override
  public void paint(Graphics g) {
    List<String> strings = new ArrayList<String>();
    strings.add("Welcome to Unhappy devil! You are a demon locked in the 'rogue simulator' section of hell. You are obviously extremly annoyed");
    strings.add("about being locked in here but satan has promised that, if you can beat the game, he will let you free to wreak havoc on earth");
    strings.add("");
    strings.add("Sadly this game never ends.");    
    strings.add("");
    strings.add("The only thing you can do is keep trying to get as deep into the dungeons of hell as you can.");
    strings.add("");
    strings.add("A quick glossary:");
    strings.add("A-Z: These are enemies. The aggressive ones broadcast their planned route to you with a red line. The non aggressives ones will");
    strings.add("just wander around randomly");
    strings.add("*: Gold! This will improve your score. It has no other purpose");
    strings.add("): A weapon of some sort. Weapons improve your damage and have a durability. When you attack the enemies it");
    strings.add("lowers the weapons durability; if the durability reaches 0 then the weapon is broken and useless. You can only have one weapon");
    strings.add("at a time.");    
    strings.add("]: A piece of armor. Armor works almost exactly like weapons except it decreases the damage you take instead");
    strings.add("of improve your damage");
    strings.add(".(Period): an empty space. all enemies and the player can move onto these");
    strings.add("- or |: These are walls. no one can move into these. they are the opposite of a .");
    strings.add("+: A Closed door. An open door is: /. it takes one move to open a door but it it is then treated like a normal empty space.");
    strings.add("Doors close behind you");
    strings.add("@: You. The Player. You move with WASD in the standard way. To attack move into enemies. to pick up items move into them.");
    strings.add("You should get a message. in the upper left of the screen If you successfully picked up the item.");
    strings.add("$: A chest of gold. This is worth 100 gold");
    strings.add("!: A potion. Potions have randomized effects and are mostly helpful");
    strings.add(":(Colon): This is a 'secret' passage; only players can move through these. If you are in a secret passage way enemies");
    strings.add("can still attack you though they cannot move into the space after you exit it");
    strings.add("%: The next level of the game. Enemies get stronger the deeper into the dungeons you go but you also");
    strings.add("get a chance to permanently improve either health, max health, or damage");
    strings.add("");
    strings.add("Thats it good luck!");
    strings.add("                                                                                                   [a] continue");
    
    Font tmpFont = g.getFont();
    g.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
    g.setColor(Color.WHITE);
    int y = 0;
    for (String string : strings) {
      g.drawString(string, 20, 20+y);
      y += 20;
    }
    g.setFont(tmpFont);
  }
}
