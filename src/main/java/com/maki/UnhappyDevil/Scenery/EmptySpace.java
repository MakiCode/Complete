package com.maki.UnhappyDevil.Scenery;

import java.awt.Color;
import java.awt.Graphics;

import com.maki.UnhappyDevil.GameMap;
import com.maki.UnhappyDevil.Entities.MovableEntity;

public class EmptySpace extends Scenery {

  public EmptySpace() {
  }

  // we require this copy constructor because of the AStar algorithm requiring
  // that we have the ability to make a temporary copy
  public EmptySpace(Scenery s) {
    super(s.getGameMap(), s.getX(), s.getY(), s.getMultiplier());
  }

  public EmptySpace(int x, int y, GameMap g, int multiplier) {
    super(g, x, y, multiplier);
  }

  @Override
  public void normalPaint(Graphics g, int xOffSet, int yOffSet) {
    if (highlighted) {
      g.setColor(Color.RED);
      highlighted = false;
    } else if (highlighted2) {
      highlighted2 = false;
      g.setColor(Color.GREEN);
    } else {
      g.setColor(Color.WHITE);
    }
    g.drawString(".", getX() * getMultiplier() + xOffSet, getY() * getMultiplier() + yOffSet);
  }

  protected void doNormalOperation(MovableEntity e) {
    super.moveEntity(e);
  }

  @Override
  public boolean getIsWalkable() {
    return true;
  }

}
