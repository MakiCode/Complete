package com.maki.UnhappyDevil;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

public class StatsDisplay implements Drawable {
  private int displayWidth;
  private int strength;
  private int level;
  private int gold;
  private int healthPoints;
  private int maxHealthPoints;
  private int armorClass;
  private int xp;
  private int playerLevel;
  private int damage;
  private int killcount;
  private int weaponDurability;
  private int armorDurability;
  private String weapon = "";
  private String armor = "";

  public StatsDisplay(int displayWidth, int displayHeight) {
    super();
    this.displayWidth = displayWidth;
  }

  public void paint(Graphics g) {
    ArrayList<String> strings = new ArrayList<String>();

    strings.add(String.format("Level Of Dungeon: %d", level));
    strings.add(String.format("Gold: %d", gold));
    strings.add(String.format("HP: %d(%d)", healthPoints, maxHealthPoints));
    strings.add(String.format("Str %d", strength));
    strings.add(String.format("Ac: %d", armorClass));
    strings.add(String.format("xp: %d/%d", xp, playerLevel));
    strings.add(String.format("Damage: %d", damage));
    strings.add(String.format("Kill Count: %d", killcount));
    strings.add(String.format("Weapon: %s", weapon));
    strings.add(String.format("Weapon Durability: %d", weaponDurability));
    strings.add(String.format("Armor: %s", armor));
    strings.add(String.format("Armor: Durability: %d", armorDurability));

    g.setColor(Color.WHITE);

    int y = 50;
    for (String string : strings) {
      g.drawString(string, displayWidth - 275, y);
      y += 20;
    }

  }

  public int getDisplayWidth() {
    return displayWidth;
  }

  public void setDisplayWidth(int displayWidth) {
    this.displayWidth = displayWidth;
  }

  public int getStrength() {
    return strength;
  }

  public void setStrength(int strength) {
    this.strength = strength;
  }

  public int getLevel() {
    return level;
  }

  public void setLevel(int level) {
    this.level = level;
  }

  public int getGold() {
    return gold;
  }

  public void setGold(int gold) {
    this.gold = gold;
  }

  public int getHealthPoints() {
    return healthPoints;
  }

  public void setHealthPoints(int healthPoints) {
    this.healthPoints = healthPoints;
  }

  public int getMaxHealthPoints() {
    return maxHealthPoints;
  }

  public void setMaxHealthPoints(int maxHealthPoints) {
    this.maxHealthPoints = maxHealthPoints;
  }

  public int getArmorClass() {
    return armorClass;
  }

  public void setArmorClass(int armorClass) {
    this.armorClass = armorClass;
  }

  public int getXp() {
    return xp;
  }

  public void setXp(int xp) {
    this.xp = xp;
  }

  public int getPlayerLevel() {
    return playerLevel;
  }

  public void setPlayerLevel(int playerLevel) {
    this.playerLevel = playerLevel;
  }

  public int getDamage() {
    return damage;
  }

  public void setDamage(int damage) {
    this.damage = damage;
  }

  public int getKillcount() {
    return killcount;
  }

  public void setKillcount(int killcount) {
    this.killcount = killcount;
  }

  public String getWeapon() {
    return weapon;
  }

  public void setWeapon(String weapon) {
    if(weapon.isEmpty() || weapon == null) {
      this.weapon = "No Weapon";
    } else {
      this.weapon = weapon;            
    }
  }

  public String getArmor() {
    return armor;
  }

  public void setArmor(String armor) {
    if(armor.isEmpty() || armor == null) {
      this.armor = "No Armor";
    } else {
      this.armor = armor;
    }
  }

  public int getWeaponDurability() {
    return weaponDurability;
  }

  public void setWeaponDurability(int weaponDurability) {
    this.weaponDurability = weaponDurability;
  }

  public int getArmorDurability() {
    return armorDurability;
  }

  public void setArmorDurability(int armorDurability) {
    this.armorDurability = armorDurability;
  }
}
