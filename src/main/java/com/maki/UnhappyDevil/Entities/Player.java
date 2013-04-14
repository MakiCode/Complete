package com.maki.UnhappyDevil.Entities;

import java.awt.Color;
import java.awt.Graphics;

import com.maki.UnhappyDevil.DevilPanel;
import com.maki.UnhappyDevil.GameMap;
import com.maki.UnhappyDevil.StatsDisplay;
import com.maki.UnhappyDevil.TextDisplay;
import com.maki.UnhappyDevil.Entities.Loot.Armor;
import com.maki.UnhappyDevil.Entities.Loot.Weapon;

//If I can think of how to do it properly I might want to have certain enemies freeze you..........
public class Player extends MovableEntity implements Visitable {
  private int killCount;
  private int health = 12;
  private int maxHealth = 12;
  private int damageDealt = 0;
  private int gold = 0;
  private int levelInDungeon = 1;
  private int playerLevel = 0;
  private int xp = 0;
  private int strength = 10;
  private boolean disabled;
  private Armor armor;
  private Weapon weapon;
  private DevilPanel panel;
  private StatsDisplay statsDisplay;
  private int xpUntilNextLevel;

  // private TextDisplay textDisplay;

  public Player(int x, int y, GameMap g, int multiplier, StatsDisplay statsDisplay,
      TextDisplay textDisplay, DevilPanel panel) {
    super(g, x, y, multiplier, textDisplay);
    this.statsDisplay = statsDisplay;
    // this.textDisplay = textDisplay;
    this.panel = panel;
    health += strength * 0.4;
    damageDealt += strength / 3;
    maxHealth = health;
    disabled = false;
    this.statsDisplay.setHealthPoints(health);
    this.statsDisplay.setMaxHealthPoints(maxHealth);
    this.statsDisplay.setLevel(levelInDungeon);
    this.statsDisplay.setXp(xp);
    this.statsDisplay.setPlayerLevel(playerLevel);
    this.statsDisplay.setArmorClass(0);
    this.statsDisplay.setStrength(strength);
    this.statsDisplay.setDamage(damageDealt);
    this.statsDisplay.setKillcount(killCount);
    this.statsDisplay.setArmor("");
    this.statsDisplay.setWeapon("");
    xpUntilNextLevel = 20;
  }

  @Override
  public void paint(Graphics g, int xOffSet, int yOffSet) {
    g.setColor(Color.WHITE);
    g.drawString("@", x * 17 + xOffSet, y * 17 + yOffSet);
  }

  @Override
  public String toString() {
    return "Player [x=" + x + ", y=" + y + "]";
  }

  @Override
  public boolean hasToDoSomething() {
    // the player always overrides original functionality
    return true;
  }

  public void addKillCount() {
    killCount++;
    statsDisplay.setKillcount(killCount);
  }

  @Override
  public boolean getIsWalkable() {
    return true;
  }

  public int getDamageDealt() {
    if (hasWeapon()) {
      int damage = damageDealt + weapon.getDamage();
      if (weapon.isBroken()) {
        statsDisplay.setDamage(damageDealt);
      } else {
        statsDisplay.setDamage(damage);
      }
      statsDisplay.setWeapon(weapon.getName());
      statsDisplay.setWeaponDurability(weapon.getDurability());
      return damage;
    } else {
      return damageDealt;
    }
  }

  @Override
  public void accept(Visitor visitor) {
    visitor.visitPlayer(this);
  }

  public void addGold(int i) {
    gold += i;
    statsDisplay.setGold(gold);
  }

  public void takeDamage(int damage) {
    int damageTaken = damage;
    // Why do I do all of this? to simulate double or float arithmetic
    if (hasArmor()) {
      if (armor.isBroken()) {
        damageTaken = damageTaken * 10;
        damageTaken = damageTaken / armor.getArmorClass();
        if (damageTaken < 10) {
          damageTaken = 1;
        } else {
          damageTaken = damageTaken / 10;
        }
      }
      statsDisplay.setArmorDurability(armor.getDurability());
      statsDisplay.setArmor(armor.getName());
    }
    if (damageTaken > 0) {
      health -= damageTaken;
      statsDisplay.setHealthPoints(health);
    }
    if (health <= 0) {
      panel.scoreScreen(gold, killCount, levelInDungeon, playerLevel, xp);
    }
  }

  @Override
  public void moveUp() {
    if (!disabled) {
      gameMap.applyMove(x, y - 1, this);
    } else {
      disabled = false;
    }
  }

  @Override
  public void moveDown() {
    if (!disabled) {
      gameMap.applyMove(x, y + 1, this);
    } else {
      disabled = false;
    }
  }

  @Override
  public void moveLeft() {
    if (!disabled) {
      gameMap.applyMove(x - 1, y, this);
    } else {
      disabled = false;
    }
  }

  @Override
  public void moveRight() {
    if (!disabled) {
      gameMap.applyMove(x + 1, y, this);
    } else {
      disabled = false;
    }
  }

  @Override
  public Visitor getVisitor() {
    return new PlayerVisitor(this);
  }

  public boolean hasArmor() {
    return armor != null;
  }

  public void removeArmor() {
    armor = null;
    statsDisplay.setArmor("No Armor");
  }

  public Armor getArmor() {
    return armor;
  }

  public void setArmor(Armor armor) {
    this.armor = armor;
    statsDisplay.setArmor(armor.getName());
    statsDisplay.setArmorClass(armor.getArmorClass());
    statsDisplay.setArmorDurability(armor.getDurability());
  }

  public int getKillCount() {
    return killCount;
  }

  public int getHealth() {
    return health;
  }

  public int getGold() {
    return gold;
  }

  public int getPlayerLevel() {
    return playerLevel;
  }

  public int getXp() {
    return xp;
  }

  public int getStrength() {
    return strength;
  }

  public boolean hasWeapon() {
    return weapon != null;
  }

  public Weapon getWeapon() {
    return weapon;
  }

  public void removeWeapon() {
    weapon = null;
    statsDisplay.setWeapon("No Weapon");
  }

  public void setWeapon(Weapon weapon) {
    this.weapon = weapon;
    statsDisplay.setWeapon(weapon.getName());
    statsDisplay.setDamage(damageDealt + weapon.getDamage());
    statsDisplay.setWeaponDurability(weapon.getDurability());
  }

  // I use xp2 because accidentally using the method argument was an insidous
  // bug
  public void addXp(int xp2) {
    xp += xp2;
    if (xp >= xpUntilNextLevel) {
      xp = xp - xpUntilNextLevel;
      xp = 0;
      addPlayerLevel();
      xpUntilNextLevel += 10;
    }
    statsDisplay.setXp(this.xp);
  }

  public void addPlayerLevel() {
    playerLevel++;
    maxHealth += 2;
    damageDealt += 1;
    health = maxHealth;
    textDisplay
        .displayText("You Leveled Up! +2 Max Health, +1 damage And A Full Refill on Health!");
    statsDisplay.setPlayerLevel(playerLevel);
    statsDisplay.setMaxHealthPoints(maxHealth);
    statsDisplay.setHealthPoints(health);
    if (hasWeapon()) {
      statsDisplay.setDamage(damageDealt + weapon.getDamage());
      weapon.addDuarability(1);
    } else {
      statsDisplay.setDamage(damageDealt);
    }
  }

  public void addHealth(int health) {
    this.health += health;
    if (this.health > maxHealth) {
      this.health = maxHealth;
    }
    statsDisplay.setHealthPoints(this.health);
  }

  public void addMaxHealth(int maxHealth) {
    this.maxHealth += maxHealth;
    statsDisplay.setMaxHealthPoints(this.maxHealth);
  }

  public void goDeeper() {
    levelInDungeon++;
    statsDisplay.setLevel(levelInDungeon);
    panel.displayLevelUpScreen();
  }

  public void addDamage(int damage) {
    damageDealt += damage;
    if (hasWeapon()) {
      statsDisplay.setDamage(damageDealt + weapon.getDamage());
      weapon.addDuarability(1);
    } else {
      statsDisplay.setDamage(damageDealt);
    }
  }

  public boolean isDisabled() {
    return disabled;
  }

  public void disable() {
    disabled = true;
  }

  public void setLevelsDeep(int levelsDeep) {
    levelInDungeon = levelsDeep;
  }

  public void addStrength(int strengthToAdd) {
    health -= Math.ceil(strength * 0.4);
    damageDealt -= Math.ceil(strength / 3);
    strength += strengthToAdd;
    health += Math.ceil(strength * 0.4);
    damageDealt += Math.ceil(strength / 3);
    maxHealth = health;
    if (hasWeapon()) {
      statsDisplay.setDamage(damageDealt + weapon.getDamage());
      weapon.addDuarability(1);
    } else {
      statsDisplay.setDamage(damageDealt);
    }
    statsDisplay.setHealthPoints(health);
    statsDisplay.setStrength(strength);
    statsDisplay.setMaxHealthPoints(maxHealth);
  }

  public void subtractStrength(int strengthToSubtract) {
    health -= Math.ceil(strength * 0.4);
    damageDealt -= Math.ceil(strength / 3);
    strength -= strengthToSubtract;
    if(strength <= 0) {
      strength = 0;
    }
    health += Math.ceil(strength * 0.4);
    damageDealt += Math.ceil(strength / 3);
    maxHealth = health;
    if (hasWeapon()) {
      statsDisplay.setDamage(damageDealt + weapon.getDamage());
      weapon.addDuarability(1);
    } else {
      statsDisplay.setDamage(damageDealt);
    }
    statsDisplay.setHealthPoints(health);
    statsDisplay.setStrength(strength);
    statsDisplay.setMaxHealthPoints(maxHealth);
  }

  public int getDepth() {
    return levelInDungeon;
  }

  public void addDurability(int i) {
    if (hasArmor()) {
      armor.addDuarability(i);
      statsDisplay.setArmorDurability(armor.getDurability());
    }
    if(hasWeapon()) {
      weapon.addDuarability(i);
      statsDisplay.setWeaponDurability(weapon.getDurability());
    }
  }
}
