package com.maki.UnhappyDevil.Entities;

import java.awt.Graphics;

import com.maki.UnhappyDevil.GameMap;
import com.maki.UnhappyDevil.TextDisplay;
import com.maki.UnhappyDevil.Scenery.Scenery;

public abstract class MovableEntity extends Entity implements Visitable {
  protected GameMap gameMap;
  private Scenery tileIAmIn;
  protected int x;
  protected int y;
  protected int multiplier;
  private boolean dead = false;

  public MovableEntity(GameMap gameMap, int x, int y, int multiplier, TextDisplay textDisplay) {
    super(textDisplay);
    this.gameMap = gameMap;
    this.x = x;
    this.y = y;
    this.multiplier = multiplier;
  }
  
  public abstract boolean getIsWalkable();
  
  public abstract boolean hasToDoSomething();
  
  public abstract void paint(Graphics g, int xOffSet, int yOffSet);
  
  public abstract Visitor getVisitor();
  
  public void registerWithGameMap() {
  }
  
  public void exitTile() {
    getTileIAmIn().removeEntity(); 
  }

  public int getX() {
    return x;
  }

  public int getY() {
    return y;
  }

  public void setX(int x) {
    this.x = x;
  }

  public void setY(int y) {
    this.y = y;
  }

  public void setSecenery(Scenery s) {
    setTileIAmIn(s);
  }

  public void moveUp() {
    gameMap.applyMove(x, y - 1, this);
  }

  public void moveDown() {
    gameMap.applyMove(x, y + 1, this);
  }

  public void moveLeft() {
    gameMap.applyMove(x - 1, y, this);
  }

  public void moveRight() {
    gameMap.applyMove(x + 1, y, this);
  }

  public boolean isDead() {
    return dead;
  }

  public void setDead(boolean dead) {
    this.dead = dead;
  }


  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + (dead ? 1231 : 1237);
    result = prime * result + multiplier;
    result = prime * result + x;
    result = prime * result + y;
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (!(obj instanceof MovableEntity))
      return false;
    MovableEntity other = (MovableEntity) obj;
    if (dead != other.dead)
      return false;
    if (multiplier != other.multiplier)
      return false;
    if (x != other.x)
      return false;
    if (y != other.y)
      return false;
    return true;
  }

  public Scenery getTileIAmIn() {
    return tileIAmIn;
  }

  public void setTileIAmIn(Scenery tileIAmIn) {
    this.tileIAmIn = tileIAmIn;
  }
}
