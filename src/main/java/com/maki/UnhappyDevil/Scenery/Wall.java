package com.maki.UnhappyDevil.Scenery;

import java.awt.Color;
import java.awt.Graphics;

import com.maki.UnhappyDevil.GameMap;
import com.maki.UnhappyDevil.TextDisplay;
import com.maki.UnhappyDevil.Entities.MovableEntity;

public class Wall extends Scenery {

  private String wallGraphic;

  public Wall() {
  }

  public Wall(int x, int y, GameMap gameMap, int multiplier) {
    super(gameMap, x, y, multiplier);
    wallGraphic = "-";
  }

  @Override
  public void initalize(GameMap gameMap, int x, int y, int multiplier, TextDisplay textDisplay) {
    super.initalize(gameMap, x, y, multiplier, textDisplay);
    wallGraphic = "-";
  }

  public void chooseImage() {
    // get the entity from the map that is on either side of this. if one is a
    // wall then we turn the graphic to a "-" if neither then we leave it as a
    // "|"
    if (getGameMap().isSceneryAtPoint(getX(), getY() + 1)) {
      if (getGameMap().getSceneryAtPoint(getX(), getY() + 1) instanceof Wall) {
        // DAD please make a note next time you try to change something (like
        // using character codes instead of actual characters)
        // I had a very weird bug where walls were 2s instead of lines and I had no idea what happened
        wallGraphic = "|";
      }
    }
    if (getGameMap().isSceneryAtPoint(getX(), getY() - 1)) {
      if (getGameMap().getSceneryAtPoint(getX(), getY() - 1) instanceof Wall) {
        wallGraphic = "|";
      }
    }
  }

  @Override
  public void normalPaint(Graphics g, int xOffSet, int yOffset) {
    if (highlighted) {
      g.setColor(Color.RED);
      highlighted = false;
    } else if (highlighted2) {
      g.setColor(Color.GREEN);
      highlighted2 = false;
    } else {
      g.setColor(Color.WHITE);
    }
    g.drawString(wallGraphic, getX() * 17 + xOffSet, getY() * 17 + yOffset);
  }

  @Override
  public void configureScenery() {
    chooseImage();
  }

  @Override
  protected void doNormalOperation(MovableEntity e) {
    // do nothing players and entities can't do anything to walls

  }

  @Override
  public boolean getIsWalkable() {
    return false;
  }

}
