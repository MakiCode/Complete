package com.maki.UnhappyDevil.Scenery;

import java.awt.Graphics;

import com.maki.UnhappyDevil.Entities.MovableEntity;
import com.maki.UnhappyDevil.Entities.Player;

public class SecretPassage extends Scenery{

  @Override
  public boolean getIsWalkable() {
    return false;
  }

  @Override
  public void normalPaint(Graphics g, int xOffSet, int yOffSet) {
    g.drawString(":", getX()*getMultiplier()+xOffSet, getY()*getMultiplier()+yOffSet);
    
  }

  @Override
  protected void doNormalOperation(MovableEntity e) {
    if(e instanceof Player) {
      moveEntity(e);      
    }
  }

}
