package com.maki.UnhappyDevil;

import java.awt.Point;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import com.maki.UnhappyDevil.Entities.MovableEntity;
import com.maki.UnhappyDevil.Scenery.Scenery;

public class GameMapData {
  int playerSpawnX, playerSpawnY, width, height;
  Scenery data[][];

  public GameMapData(int width, int height) {
    super();
    this.width = width;
    this.height = height;
    data = new Scenery[height][width];
  }
  
  public GameMapData(Scenery[][] data) {
    super();
    height = data.length;
    width = data[0].length;
    this.data = data;
  }

  public int height() {
    return height;
  }

  public int width() {
    return width;
  }

  public Scenery getScenery(int x, int y) {
    if ((x < width && y < height) && (x >= 0 && y >= 0)) {
      return data[y][x];
    } else {
      return null;
    }
  }

  public void putPlayer(MovableEntity player) {
    if ((playerSpawnX < width && playerSpawnY < height) && (playerSpawnX >= 0 && playerSpawnY >= 0)) {
      data[playerSpawnY][playerSpawnX].addEntity(player);
      player.setX(playerSpawnX);
      player.setY(playerSpawnY);
      player.setSecenery(data[playerSpawnY][playerSpawnX]);
    } else {
      throw new AssertionError("Player spawned off the map");
    }
  }

  public void putScenery(Scenery scenery, int x, int y) {
    if ((x < width && y < height) && (x >= 0 && y >= 0)) {
      data[y][x] = scenery;
    } else {
      throw new AssertionError("placed a scenery off the map");
    }
  }

  public void setPlayerSpawnPoint(int x, int y) {
    playerSpawnX = x;
    playerSpawnY = y;
  }

  public void setSpawnPoint(List<Point> possibleSpawnPoints) {
    int index = new Random().nextInt(possibleSpawnPoints.size());
    Point playerSpawnPoint = possibleSpawnPoints.get(index);
    setPlayerSpawnPoint(playerSpawnPoint.x, playerSpawnPoint.y);
  }
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + Arrays.hashCode(data);
    result = prime * result + height;
    result = prime * result + playerSpawnX;
    result = prime * result + playerSpawnY;
    result = prime * result + width;
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (!(obj instanceof GameMapData))
      return false;
    GameMapData other = (GameMapData) obj;
    if (!Arrays.deepEquals(data, other.data))
      return false;
    if (height != other.height)
      return false;
    if (playerSpawnX != other.playerSpawnX)
      return false;
    if (playerSpawnY != other.playerSpawnY)
      return false;
    if (width != other.width)
      return false;
    return true;
  }


}
