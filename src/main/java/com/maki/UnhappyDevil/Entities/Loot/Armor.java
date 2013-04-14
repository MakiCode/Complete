package com.maki.UnhappyDevil.Entities.Loot;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Properties;

import com.maki.UnhappyDevil.TextDisplay;
import com.maki.UnhappyDevil.Entities.Player;

public class Armor extends Loot {
  private int armorClass;
  private String name;
  private int durability;

  public Armor(int x, int y, int multiplier, String name, int armorClass, TextDisplay textDisplay) {
    super(x, y, multiplier, textDisplay);
    this.name = name;
    this.armorClass = armorClass;
  }

  public Armor(int x, int y, int multiplier, Properties armorProperties, TextDisplay textDisplay) {
    super(x, y, multiplier, textDisplay);
    name = armorProperties.getProperty("name", "Unknowm");
    armorClass = Integer.parseInt(armorProperties.getProperty("ac", "0"));
    durability = Integer.parseInt(armorProperties.getProperty("durability", "0"));
    
  }

  @Override
  public void paint(Graphics g, int xOffSet, int yOffSet) {
    g.setColor(Color.WHITE);
    g.drawString("]", getX()*getMultiplier()+xOffSet, getY()*getMultiplier()+yOffSet);
  }
  
  //need to figure out where to put this method
  @Override
  public void collectLoot(Player player) {
    Armor tmpArmor = null;
    if(player.hasArmor()) {
      tmpArmor = player.getArmor();
      player.removeArmor();
      tmpArmor.setX(getX());
      tmpArmor.setY(getY());
      tmpArmor.setTileIAmIn(getTileIAmIn());
    }
    player.setArmor(this);
    getTileIAmIn().moveEntity(player);
    getTileIAmIn().removeLoot();
    getTileIAmIn().addLoot(tmpArmor);
    getTextDisplay().displayText("You Picked up " + name);
  }  

  public int getArmorClass() {
    if(!isBroken()) {
      durability--;
      return armorClass;      
    } else {
      return 0;
    }
  }

  
  public int getDurability() {
    return durability;
  }

  public void setDurability(int durability) {
    this.durability = durability;
  }

  public void setArmorClass(int armorClass) {
    this.armorClass = armorClass;
  }

  public String getName() {
    if(isBroken()) {
      return "Broken " + name;
    } else {      
      return name;
    }
  }

  public void setName(String name) {
    this.name = name;
  }

  public boolean isBroken() {
    return durability <= 0;
  }

  public void addDuarability(int addDurability) {
    durability += addDurability;
  }

}
