package com.maki.UnhappyDevil.Scenery;

import java.awt.Color;
import java.awt.Graphics;
import java.io.IOException;

import com.maki.UnhappyDevil.GameMap;
import com.maki.UnhappyDevil.Entities.MovableEntity;
import com.maki.UnhappyDevil.Entities.Player;

public class NextLevel extends Scenery {

  public NextLevel(GameMap gameMap, int x, int y, int multiplier) {
  super(gameMap, x, y, multiplier);
  }
  
  @Override
  public boolean getIsWalkable() {
    return true;
  }

  @Override
  public void normalPaint(Graphics g, int xOffSet, int yOffSet) {
    g.setColor(Color.WHITE);
    g.drawString("%", getX()*getMultiplier()+xOffSet, getY()*getMultiplier()+yOffSet);
  }

  @Override
  protected void doNormalOperation(MovableEntity e) {
    moveEntity(e);
    if(e instanceof Player) {
      try {
        getGameMap().loadNextLevel();
      } catch (IOException e1) {
        getGameMap().throwException(e1);
      }      
    }
  }

}
