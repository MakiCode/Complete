package com.maki.UnhappyDevil.Scenery;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Random;

public enum CharacterDataManager {
  // this class will manage loading enemies. it's job would include managing the
  // level of enemies that spawn (goblins on the first level hobgoblins on the
  // next etc.) and the specifics of randomly choosing an enemy to load

  INSTANCE;

  public static final String PATH = "Enemies" + File.separator;
  private Properties goblinProperties;
  private List<String> enemiesL1 = new ArrayList<String>(10);
  private List<String> enemiesL2 = new ArrayList<String>(10);
  private List<String> enemiesL3 = new ArrayList<String>(10);
  private List<String> enemiesL4 = new ArrayList<String>(10);
  private List<String> enemiesL5 = new ArrayList<String>(10);
  private List<List<String>> enemiesList = new ArrayList<List<String>>(10);

  private CharacterDataManager() {
    // TODO read this in from the enemies_meta.properties
    enemiesL1.add("snake.properties");
    enemiesL1.add("hobgoblin.properties");
    enemiesL1.add("bat.properties");
    enemiesL1.add("kestrel.properties");
    enemiesL1.add("emu.properties");
    enemiesL2.add("Level2" + File.separator +"icemonster.properties");
    enemiesL3.add("Level3" + File.separator +"rattlesnake.properties");
    enemiesL4.add("Level4" + File.separator +"orc.properties");
    enemiesL5.add("Level5" + File.separator +"zombie.properties");
    enemiesL2.addAll(enemiesL1);
    enemiesL3.addAll(enemiesL2);
    enemiesL4.addAll(enemiesL3);
    enemiesL5.addAll(enemiesL4);
    enemiesList.add(0, enemiesL1);
    enemiesList.add(1, enemiesL2);
    enemiesList.add(2, enemiesL3);
    enemiesList.add(3, enemiesL4);
    enemiesList.add(4, enemiesL5);

  }

  public Properties getEnemyProperties(int levelsDeep) throws IOException {
    String enemy = null;
    int depth = levelsDeep;
    if(depth > 5) {
      depth = 5;
    }
    List<String> enemies = enemiesList.get(depth-1);
    int index = new Random().nextInt(enemies.size());
    enemy = enemies.get(index);
    goblinProperties = new Properties();
    String path = PATH + enemy;
    InputStream is = getClass().getClassLoader().getResourceAsStream(path);
    if (is == null) {
      System.out.printf("could not load properties file: %s\n", path);
    } else {
      goblinProperties.load(is);
    }
    return goblinProperties;
  }
}
