package com.maki.UnhappyDevil.Entities.Loot;

import java.awt.Graphics;

import com.maki.UnhappyDevil.TextDisplay;
import com.maki.UnhappyDevil.Entities.Entity;
import com.maki.UnhappyDevil.Entities.Player;
import com.maki.UnhappyDevil.Entities.Visitor;
import com.maki.UnhappyDevil.Scenery.Scenery;

public abstract class Loot extends Entity {

  private Scenery tileIAmIn;
  public void setTileIAmIn(Scenery tileIAmIn) {
    this.tileIAmIn = tileIAmIn;
  }

  private int x;
  private int y;
  private int multiplier;

  public Loot(int x, int y, int multiplier, TextDisplay textDisplay) {
    super(textDisplay);
    this.x = x;
    this.y = y;
    this.multiplier = multiplier;
  }

  public abstract void collectLoot(Player player);

  public abstract void paint(Graphics g, int xOffSet, int yOffSet);

  public Visitor getVisitor(){
    //return nothing. visitors don't and won't have visitors
    return null;
  }

  public void registerWithGameMap() {
  }

  public Scenery getTileIAmIn() {
    return tileIAmIn;
  }

  public int getX() {
    return x;
  }

  public void setX(int x) {
    this.x = x;
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

  @Override
  public void setSecenery(Scenery scenery) {
    tileIAmIn = scenery;
  }
  
  @Override
  public void accept(Visitor visitor) {
    visitor.visitLoot(this);
  }

}
