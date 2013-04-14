package com.maki.UnhappyDevil.Entities.Loot;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Properties;

import com.maki.UnhappyDevil.TextDisplay;
import com.maki.UnhappyDevil.Entities.Player;

public class Weapon extends Loot {
  private String name;
  private int damage;
  private int durability;
  
  public Weapon(int x, int y, int multiplier, String name, int damage, TextDisplay textDisplay) {
    super(x, y, multiplier, textDisplay);
    this.name = name;
    this.damage = damage;
  }

  public Weapon(int x, int y, int multiplier, Properties weaponProperties, TextDisplay textDisplay) {
    super(x, y, multiplier, textDisplay);
    name = weaponProperties.getProperty("name", "Unknown");
    damage = Integer.parseInt(weaponProperties.getProperty("damage","0"));
    durability = Integer.parseInt(weaponProperties.getProperty("durability", "0"));
  }

  @Override
  public void collectLoot(Player player) {
    Weapon tmpWeapon = null;
    if(player.hasWeapon()) {
      tmpWeapon = player.getWeapon();
      player.removeWeapon();
      tmpWeapon.setX(getX());
      tmpWeapon.setY(getY());
      tmpWeapon.setTileIAmIn(getTileIAmIn());
    }
    player.setWeapon(this);
    getTileIAmIn().moveEntity(player);
    getTileIAmIn().removeLoot();
    getTileIAmIn().addLoot(tmpWeapon);
    getTextDisplay().displayText("You Picked up " + name);
  }  
  

  @Override
  public void paint(Graphics g, int xOffSet, int yOffSet) {
    g.setColor(Color.WHITE);
    g.drawString(")", getX()*getMultiplier()+xOffSet, getY()*getMultiplier()+yOffSet);
  }

  public String getName() {
    if(isBroken()) {
      return "Broken " + name;      
    } else {
      return name;
    }
  }

  public boolean isBroken() {
    return durability <= 0;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getDamage() {
    if(!isBroken()) {      
      durability--;
      return damage;
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

  public void setDamage(int damage) {
    this.damage = damage;
  }

  public void addDuarability(int addDurability) {
    durability += addDurability;
  }

  public int getUnBrokenDamage() {
    return damage;
  }

}
