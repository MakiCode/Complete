package com.maki.UnhappyDevil;

import java.util.HashMap;
import java.util.Map;

import com.maki.UnhappyDevil.Scenery.EmptySpace;
import com.maki.UnhappyDevil.Scenery.EntitySpawn;
import com.maki.UnhappyDevil.Scenery.Scenery;

public class SceneryFactory {
  private Map<String, Class<? extends Scenery>> sceneryTypeMap = new HashMap<String, Class<? extends Scenery>>();

  public void configure(String s, Class<? extends Scenery> clazz) {
    sceneryTypeMap.put(s, clazz);
  }

  public Scenery getScenery(String s, int x, int y, GameMap g, int multiplier, int levelsDeep,
      TextDisplay textDisplay) throws Exception {
    Class<? extends Scenery> clazz = sceneryTypeMap.get(s);
    Scenery result = null;
    if (clazz == null) {
      clazz = EmptySpace.class;
    }
    result = clazz.newInstance();
    result.initalize(g, x, y, multiplier, textDisplay);
    if (result instanceof EntitySpawn) {
      // DAD I am trying to call a method that is only used in a specific subset
      // of objects (EntitySpawn classes to be specific) and that method
      // requires information that is available primarily in this method (the
      // single character string read in from the map file. It could also be
      // accessed by map loader but it would be more difficult) to do its job
      // (choose an entity to spawn) this method (getScenery) has easier access
      // to both the type and the data (there is already a hashMap defined that
      // ensures we will only pass the proper values to entitySpawn. we would
      // have to redefine it in map loader) but we only use Super Classes
      // (Sceneries) so I have to down cast the object (result) to call the
      // method that is only relevant in the objects specific case (that is,
      // setEntityToSpawn only being relevant to entitySpawn objects)
      ((EntitySpawn) result).setEnemyToSpawn(s, levelsDeep);
    }
    return result;
  }
}
