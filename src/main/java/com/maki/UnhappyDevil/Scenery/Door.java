package com.maki.UnhappyDevil.Scenery;

import java.awt.Color;
import java.awt.Graphics;

import com.maki.UnhappyDevil.GameMap;
import com.maki.UnhappyDevil.TextDisplay;
import com.maki.UnhappyDevil.Entities.MovableEntity;

public class Door extends Scenery {
  private boolean opened;

  public Door() {
  }

  public Door(int x, int y, GameMap g, int multiplier) {
    super(g, y, y, multiplier);
    opened = false;
  }

  @Override
  public void initalize(GameMap gameMap, int x, int y, int multiplier, TextDisplay textDisplay) {
    super.initalize(gameMap, x, y, multiplier, textDisplay);
    opened = false;
  }

  @Override
  public void normalPaint(Graphics g, int xOffSet, int yOffSet) {
    if (highlighted) {
      g.setColor(Color.RED);
      highlighted = false;
    } else if (highlighted2) {
      g.setColor(Color.GREEN);
      highlighted2 = false;
    } else {
      g.setColor(new Color(212, 179, 3));
    }
    if (opened) {
      g.drawString("/", getX() * 17 + xOffSet, getY() * 17 + yOffSet);
    } else {
      g.drawString("+", getX() * 17 + xOffSet, getY() * 17 + yOffSet);
    }
  }

  protected void doNormalOperation(MovableEntity e) {
    if (opened) {
      super.moveEntity(e);
      opened = false;
    } else {
      opened = true;
    }
  }

  public boolean getIsWalkable() {
    return true;
  }
}
