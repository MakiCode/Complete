package com.maki.UnhappyDevil.Entities.Loot;

import java.awt.Color;
import java.awt.Graphics;

import com.maki.UnhappyDevil.TextDisplay;
import com.maki.UnhappyDevil.Entities.Player;

public class GoldChest extends Loot {

  public GoldChest(int x, int y, int multiplier, TextDisplay textDisplay) {
    super(x, y, multiplier, textDisplay);
    // TODO Auto-generated constructor stub
  }

  @Override
  public void paint(Graphics g, int xOffSet, int yOffSet) {
    g.setColor(new Color(255, 215, 0));
    g.drawString("$", getX() * getMultiplier() + xOffSet, getY() * getMultiplier() + yOffSet);
  }

  @Override
  public void collectLoot(Player player) {
    player.addGold(100);
    getTextDisplay().displayText("You Found A Chest Of Gold!");
    getTileIAmIn().moveEntity(player);
    getTileIAmIn().removeLoot();
  }
}
