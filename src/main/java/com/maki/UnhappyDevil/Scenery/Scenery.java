package com.maki.UnhappyDevil.Scenery;

import java.awt.Graphics;

import com.maki.UnhappyDevil.GameMap;
import com.maki.UnhappyDevil.TextDisplay;
import com.maki.UnhappyDevil.Entities.MovableEntity;
import com.maki.UnhappyDevil.Entities.Loot.Loot;

public abstract class Scenery {

  private Scenery parent;
  private MovableEntity entity;
  private Loot loot;
  private GameMap gameMap;
  private TextDisplay textDisplay;
  private int x;
  private int y;
  private int multiplier;
  protected boolean highlighted;
  protected boolean highlighted2;

  public Scenery() {
  }

  public Scenery(GameMap gameMap, int x, int y, int multiplier) {
    super();
    this.gameMap = gameMap;
    this.x = x;
    this.y = y;
    this.multiplier = multiplier;
  }

  public void initalize(GameMap gameMap, int x, int y, int multiplier, TextDisplay textDisplay) {
    this.gameMap = gameMap;
    this.x = x;
    this.y = y;
    this.multiplier = multiplier;
    this.textDisplay = textDisplay;
  }

  public void initalize(Scenery s) {
    this.gameMap = s.getGameMap();
    this.x = s.getX();
    this.y = s.getY();
    this.multiplier = s.getMultiplier();
    this.textDisplay = s.getTextDisplay();
  }

  public abstract boolean getIsWalkable();

  public void paint(Graphics g, int xOffSet, int yOffSet) {
    if (hasEntity()) {
      getEntity().paint(g, xOffSet, yOffSet);
    } else if (hasLoot()) {
      getLoot().paint(g, xOffSet, yOffSet);
    } else {
      normalPaint(g, xOffSet, yOffSet);
    }
  }

  public abstract void normalPaint(Graphics g, int xOffSet, int yOffSet);

  protected abstract void doNormalOperation(MovableEntity e);

  // this method is here to be called for classes that need to do some state
  // initialization when the gameMap is fully initalized. it is not abstract
  // because many spaces do not need to be configured only the ones that need to
  // be configured will override this
  public void configureScenery() {
  }

  public boolean hasEntity() {
    return entity != null;
  }

  public MovableEntity getEntity() {
    return entity;
  }

  public void setEntity(MovableEntity entity) {
    this.entity = entity;
  }

  public void removeEntity() {
    // think up better way than null.
    entity = null;
  }

  public void addEntity(MovableEntity e) {
    this.entity = e;
  }

  public boolean hasLoot() {
    return loot != null;
  }

  public Loot getLoot() {
    return loot;
  }

  public void setLoot(Loot loot) {
    this.loot = loot;
  }

  public void removeLoot() {
    // think up better way than null.
    loot = null;
  }

  public void addLoot(Loot e) {
    this.loot = e;
  }

  /**
   * 
   * call this when the entity wants to move here
   * 
   */
  public void acceptEntity(MovableEntity e) {
    if (getEntity() != null) {
      if (getEntity().hasToDoSomething()) {
        getEntity().accept(e.getVisitor());
      } else {
        doNormalOperation(e);
      }
    } else if (hasLoot()) {
      getLoot().accept(e.getVisitor());
    } else {
      doNormalOperation(e);
    }
  }

  public boolean isWalkable() {
    if (getEntity() != null) {
      return getEntity().getIsWalkable();
    } else {
      return getIsWalkable();
    }
  }

  public void moveEntity(MovableEntity e) {
    e.setX(getX());
    e.setY(getY());
    e.exitTile();
    addEntity(e);
    e.setSecenery(this);
  }

  public GameMap getGameMap() {
    return gameMap;
  }

  public int getX() {
    return x;
  }

  public int getY() {
    return y;
  }

  public void setY(int y) {
    this.y = y;
  }

  public int getMultiplier() {
    return multiplier;
  }

  public void setMultiplier(int multiplier) {
    this.multiplier = multiplier;
  }

  public int getHCost(Scenery goal) {
    return 10 * (Math.abs(x - goal.getX()) + Math.abs(y - goal.getY()));
  }

  public int getGCost() {
    if (parent == null) {
      return 0;
    }
    if (parent.equals(this)) {
      return 0;
    }
    return 10 + parent.getGCost();
  }

  public int getFCost(Scenery goal) {
    return getHCost(goal) + getGCost();
  }

  public Scenery getParent() {
    return parent;
  }

  public void setParent(Scenery parent) {
    this.parent = parent;
  }

  public void deleteParent() {
    parent = null;
  }

  public void highLight() {
    highlighted = true;
  }

  public void highLight2() {
    highlighted2 = true;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((entity == null) ? 0 : entity.hashCode());
    result = prime * result + ((gameMap == null) ? 0 : gameMap.hashCode());
    result = prime * result + ((loot == null) ? 0 : loot.hashCode());
    result = prime * result + multiplier;
    result = prime * result + ((parent == null) ? 0 : parent.hashCode());
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
    if (!(obj instanceof Scenery))
      return false;
    Scenery other = (Scenery) obj;
    if (entity == null) {
      if (other.entity != null)
        return false;
    } else if (!entity.equals(other.entity))
      return false;
    if (gameMap == null) {
      if (other.gameMap != null)
        return false;
    } else if (!gameMap.equals(other.gameMap))
      return false;
    if (loot == null) {
      if (other.loot != null)
        return false;
    } else if (!loot.equals(other.loot))
      return false;
    if (multiplier != other.multiplier)
      return false;
    if (parent == null) {
      if (other.parent != null)
        return false;
    } else if (!parent.equals(other.parent))
      return false;
    if (x != other.x)
      return false;
    if (y != other.y)
      return false;
    return true;
  }

  @Override
  public String toString() {
    return "Scenery [parent=" + parent + ", entity=" + entity + ", loot=" + loot + ", gameMap="
        + gameMap + ", x=" + x + ", y=" + y + ", multiplier=" + multiplier + "]";
  }

  public TextDisplay getTextDisplay() {
    return textDisplay;
  }

  public void setTextDisplay(TextDisplay textDisplay) {
    this.textDisplay = textDisplay;
  }

}
