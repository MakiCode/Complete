package com.maki.UnhappyDevil.Scenery;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Random;

public enum LootDataManager {
  INSTANCE;
  public static final String PATH = "Loot" + File.separator;
  private List<String> weapons = new ArrayList<String>(56);
  private List<String> armor = new ArrayList<String>(56);

  private LootDataManager() {
    setUpArmor();
    setUpWeapons();
  }

  private void setUpArmor() {
    for (int i = 0; i < 25; i++) {
      armor.add("Armor" + File.separator + "leatherarmor.properties");
    }
    for (int i = 0; i < 15; i++) {
      armor.add("Armor" + File.separator + "chainmail.properties");
    }
    for (int i = 0; i < 10; i++) {
      armor.add("Armor" + File.separator + "bandedmail.properties");
    }
    for (int i = 0; i < 5; i++) {
      armor.add("Armor" + File.separator + "platemail.properties");
    }
    armor.add("Armor" + File.separator + "diamondarmor.properties");
    // add others
  }

  private void setUpWeapons() {
    for (int i = 0; i < 25; i++) {
      weapons.add("Weapons" + File.separator + "dagger.properties");
    }
    for (int i = 0; i < 15; i++) {
      weapons.add("Weapons" + File.separator + "shortsword.properties");
    }
    for (int i = 0; i < 10; i++) {
      weapons.add("Weapons" + File.separator + "longsword.properties");
    }
    for (int i = 0; i < 5; i++) {
      weapons.add("Weapons" + File.separator + "scimitar.properties");
    }
    weapons.add("Weapons" + File.separator + "diamondsword.properties");
    // add others

  }

  public Properties getArmorProperties(int levelsDeep) throws IOException {
    int index = new Random().nextInt(armor.size());
    String armorString = armor.get(index);
    Properties armorProperties = new Properties();
    InputStream is = getClass().getClassLoader().getResourceAsStream(PATH + armorString);
    armorProperties.load(is);
    return armorProperties;
  }

  public Properties getWeaponProperties(int levelsDeep) throws IOException {
    int index = new Random().nextInt(weapons.size());
    String weaponString = weapons.get(index);
    Properties weaponProperties = new Properties();
    InputStream is = getClass().getClassLoader().getResourceAsStream(PATH + weaponString);
    weaponProperties.load(is);
    return weaponProperties;
  }
}
