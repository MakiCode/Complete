package com.maki.UnhappyDevil.Entities.Loot;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

import com.maki.UnhappyDevil.TextDisplay;
import com.maki.UnhappyDevil.Entities.Player;

public class Potion extends Loot {

  public Potion(int x, int y, int multiplier, TextDisplay textDisplay) {
    super(x, y, multiplier, textDisplay);
  }

  // TODO add more potion effects

  @Override
  public void collectLoot(Player player) {
    
    int index = new Random().nextInt(14);

    if (index == 14 || index == 13) {
      player.addPlayerLevel();
      getTextDisplay()
          .displayText(
              "As You Drink The Potion You Suddenly Understand How To Best Your Foes And You Feel Stronger! +1 Level");
    } else if (Math.floor(index / 3) == 0) {
      player.addHealth(10);
      getTextDisplay().displayText(
          "As You Drink The Potion You Feel Your Wounds Mending. It Was A Potion Of Health!");
    } else if (Math.floor(index / 3) == 1) {
      player.addStrength(2);
      getTextDisplay().displayText(
          "The Potion Imbues You With Strength! You Feel Like You Could Lift A House! +2 Strength");
    } else if (Math.floor(index / 3) == 2) {
      player.subtractStrength(2);
      getTextDisplay()
          .displayText(
              "As You Drink The Potion Your Arms Feel Heavy And Sluggish. The Potion Weakended you! -2 Strength");
    } else if (Math.floor(index / 3) == 3) {
      player.addDurability(5);
      getTextDisplay()
          .displayText(
              "You Don't Feel Anything When You Drink The Potion But When You Look Down At Your Weapon You See That It Has Been Repaired! +5 Durability");
    }
    getTileIAmIn().moveEntity(player);
    getTileIAmIn().removeLoot();
  }

  @Override
  public void paint(Graphics g, int xOffSet, int yOffSet) {
    g.setColor(Color.WHITE);
    g.drawString("!", getX() * getMultiplier() + xOffSet, getY() * getMultiplier() + yOffSet);
  }

}
