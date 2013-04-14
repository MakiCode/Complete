package com.maki.UnhappyDevil.Entities;

import com.maki.UnhappyDevil.TextDisplay;
import com.maki.UnhappyDevil.Scenery.Scenery;

public abstract class Entity implements Visitable{
  

  TextDisplay textDisplay;
  
  public Entity(TextDisplay textDisplay) {
    super();
    this.textDisplay = textDisplay;
  }
  
  public abstract void setSecenery(Scenery scenery);

  public void registerWithGameMap() {
  }

  public TextDisplay getTextDisplay() {
    return textDisplay;
  }

  public void setTextDisplay(TextDisplay textDisplay) {
    this.textDisplay = textDisplay;
  }

}
