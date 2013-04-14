package com.maki.UnhappyDevil;

import java.awt.Font;
import java.awt.Graphics;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import com.maki.UnhappyDevil.Entities.MovableEntity;
import com.maki.UnhappyDevil.Entities.Player;
import com.maki.UnhappyDevil.Entities.Enemies.Enemy;
import com.maki.UnhappyDevil.Scenery.Scenery;

public class GameMap implements Drawable {
  private GameMapData data;
  private int mapX, mapY;
  private MapLoader mapLoader;
  private int multiplier;
  private ArrayList<Enemy> enemies = new ArrayList<Enemy>();
  private TextDisplay textDisplayer;
  private Player player;
  private DevilPanel panel;

  public GameMap(int x, int y, int multiplier, TextDisplay textDisplay, DevilPanel panel) {
    mapX = x;
    mapY = y;
    this.multiplier = multiplier;
    this.textDisplayer = textDisplay;
    this.panel = panel;
  }

  public void loadMap(int levelsDeep) throws Exception {
    mapLoader = new MapLoader();
    data = mapLoader.loadMap(this, multiplier, textDisplayer, levelsDeep);
    gameMapInit();
  }

  private void gameMapInit() {
    for (int x = 0; x < data.height(); x++) {
      for (int y = 0; y < data.width(); y++) {
        data.getScenery(x, y).configureScenery();
      }
    }

  }

  // TODO -2 lowers your max health to 1 or so

  public void paint(Graphics g) {
    g.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
    for (int x = 0; x < data.height(); x++) {
      for (int y = 0; y < data.width(); y++) {
        data.getScenery(x, y).paint(g, mapX, mapY);
      }
    }
  }

  public void registerEnemy(Enemy e) {
    enemies.add(e);
  }

  // /**
  // *
  // * @param positionOfPlayer
  // * the players x or y coordinate
  // * @param screenSize
  // * the width or height of the screen (Note: make sure the units match
  // * up width-x height-y)
  // * @param mapSize
  // * the size of the map (not the screen the note above still applies)
  // * @return the x or y coordinate of the camera, will match up with units
  // * passed in if all of the units are the same (e.g. x-width-width
  // * passed in will return x coordinate of the camera, x-width-height is
  // * not guaranteed to be anything)
  // */
  // private int chooseCameraCoordinate(int positionOfPlayer, int screenSize) {
  // return positionOfPlayer-(screenSize/2);
  // }

  public boolean isSceneryAtPoint(int x, int y) {
    if (data == null) {
      throw new RuntimeException("BOOM! data is null");
    }
    if (data.getScenery(x, y) == null) {
      return false;
    } else {
      return true;
    }
  }

  public void addPlayer(Player player) {
    this.player = player;
    data.putPlayer(player);
  }

  public Scenery getSceneryAtPoint(int x, int y) {
    return data.getScenery(x, y);
  }

  public void applyMove(int x, int y, MovableEntity p) {
    data.getScenery(x, y).acceptEntity(p);
  }

  /**
   * Allows the client to get a random number in a range
   */
  public static double getRandomDouble(double min, double max) {
    return min + (int) (Math.random() * ((max - min) + 1));
  }

  public static int getRandomInt(double min, double max) {
    return (int) (getRandomDouble(min, max));
  }

  public void updateEnemies(Player player) {
    Iterator<Enemy> iter = enemies.iterator();
    while (iter.hasNext()) {
      Enemy e = iter.next();
      e.update(player);
      if (e.isDead()) {
        iter.remove();
      }
    }
  }

  public void deleteParents() {
    for (int x = 0; x < data.height(); x++) {
      for (int y = 0; y < data.width(); y++) {
        data.getScenery(x, y).deleteParent();
      }
    }
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((data == null) ? 0 : data.hashCode());
    result = prime * result + ((enemies == null) ? 0 : enemies.hashCode());
    result = prime * result + mapX;
    result = prime * result + mapY;
    result = prime * result + multiplier;
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (!(obj instanceof GameMap))
      return false;
    GameMap other = (GameMap) obj;
    if (data == null) {
      if (other.data != null)
        return false;
    } else if (!data.equals(other.data))
      return false;
    if (enemies == null) {
      if (other.enemies != null)
        return false;
    } else if (!enemies.equals(other.enemies))
      return false;
    if (mapX != other.mapX)
      return false;
    if (mapY != other.mapY)
      return false;
    if (multiplier != other.multiplier)
      return false;
    return true;
  }

  public void loadNextLevel() throws IOException {
    deleteEnemies();
    player.goDeeper();
    try {
      data = mapLoader.loadMap(this, multiplier, textDisplayer, player.getDepth());
    } catch (Exception e) {
      throwException(e);
    }
    gameMapInit();
    addPlayer(player);

  }

  public void deleteEnemies() {
    for (Enemy enemy : enemies) {
      enemy.getTileIAmIn().removeEntity();
    }
    for (Iterator<Enemy> iterator = enemies.iterator(); iterator.hasNext();) {
      iterator.next();
      iterator.remove();
    }
  }

  public void throwException(Throwable e1) {
    panel.throwException(e1);
  }
}