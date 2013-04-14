package com.maki.UnhappyDevil.Entities.Loot;

import java.awt.Color;
import java.awt.Graphics;

import com.maki.UnhappyDevil.TextDisplay;
import com.maki.UnhappyDevil.Entities.Player;

public class Gold extends Loot {
  private int amountOfGold = 10;

  public Gold(int x, int y, int multiplier, TextDisplay textDisplay) {
    super(x, y, multiplier, textDisplay);
  }

  @Override
  public void paint(Graphics g, int xOffSet, int yOffSet) {
    g.setColor(new Color(246, 240, 0));
    g.drawString("*", getX() * getMultiplier() + xOffSet, getY() * getMultiplier() + yOffSet);
  }

  public int getGold() {
    return amountOfGold;
  }

  @Override
  public void collectLoot(Player player) {
    player.addGold(amountOfGold);
    getTileIAmIn().moveEntity(player);
    getTileIAmIn().removeLoot();
  }

}
