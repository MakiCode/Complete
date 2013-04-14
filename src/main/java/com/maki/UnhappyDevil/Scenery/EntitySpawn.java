package com.maki.UnhappyDevil.Scenery;

import java.awt.Color;
import java.awt.Graphics;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.maki.UnhappyDevil.GameMap;
import com.maki.UnhappyDevil.TextDisplay;
import com.maki.UnhappyDevil.Entities.Entity;
import com.maki.UnhappyDevil.Entities.MovableEntity;
import com.maki.UnhappyDevil.Entities.Enemies.Enemy;
import com.maki.UnhappyDevil.Entities.Loot.Armor;
import com.maki.UnhappyDevil.Entities.Loot.Gold;
import com.maki.UnhappyDevil.Entities.Loot.GoldChest;
import com.maki.UnhappyDevil.Entities.Loot.Loot;
import com.maki.UnhappyDevil.Entities.Loot.Potion;
import com.maki.UnhappyDevil.Entities.Loot.Weapon;

public class EntitySpawn extends Scenery {
  Entity entity;
  public Map<String, Entity> mapOfEntities;

  public EntitySpawn() {
  }

  public EntitySpawn(GameMap gameMap, int x, int y, int multiplier) {
    super(gameMap, y, y, multiplier);
  }

  @Override
  public void initalize(GameMap gameMap, int x, int y, int multiplier, TextDisplay textDisplay) {
    super.initalize(gameMap, x, y, multiplier, textDisplay);
  }

  private void setUpHashMap(int levelsDeep) throws IOException {
    mapOfEntities = new HashMap<String, Entity>();
    mapOfEntities.put("*", new Gold(getX(), getY(), getMultiplier(), getTextDisplay()));
    mapOfEntities.put("$", new GoldChest(getX(), getY(), getMultiplier(), getTextDisplay()));
    mapOfEntities
        .put("E", new Enemy(getGameMap(), getX(), getY(), getMultiplier(),
            CharacterDataManager.INSTANCE.getEnemyProperties(levelsDeep), getTextDisplay(),
            levelsDeep));
    mapOfEntities.put(
        "]",
        new Armor(getX(), getY(), getMultiplier(), LootDataManager.INSTANCE
            .getArmorProperties(levelsDeep), getTextDisplay()));
    mapOfEntities.put(
        ")",
        new Weapon(getX(), getY(), getMultiplier(), LootDataManager.INSTANCE
            .getWeaponProperties(levelsDeep), getTextDisplay()));
    mapOfEntities.put("!", new Potion(getX(), getY(), getMultiplier(), getTextDisplay()));
  }

  private void spawnEntity() {
    // List<Enemy> enemies = new ArrayList<Enemy>();
    // enemies.add(new Goblin(gameMap, x, y, multiplier));
    // int index = new Random().nextInt(enemies.size());
    entity.setSecenery(this);
    if (entity instanceof MovableEntity) {
      addEntity((MovableEntity) entity);
    } else {
      addLoot((Loot) entity);
    }
  }

  // A single character string that we use to decide what sort of entity to
  // spawn currently only accepts:
  public void setEnemyToSpawn(String s, int levelsDeep) throws IOException {
    setUpHashMap(levelsDeep);
    entity = mapOfEntities.get(s);
  }

  @Override
  public void normalPaint(Graphics g, int xOffSet, int yOffSet) {
    if (highlighted) {
      g.setColor(Color.RED);
      highlighted = false;
    } else if (highlighted2) {
      g.setColor(Color.GREEN);
      highlighted2 = false;
    } else {
      g.setColor(Color.WHITE);
    }
    g.drawString(".", getX() * 17 + xOffSet, getY() * 17 + yOffSet);
  }

  @Override
  protected void doNormalOperation(MovableEntity e) {
    super.moveEntity(e);
  }

  @Override
  public void configureScenery() {
    spawnEntity();
    entity.registerWithGameMap();
  }

  @Override
  public boolean getIsWalkable() {
    return true;
  }
}
