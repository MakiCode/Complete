package com.maki.UnhappyDevil.Entities.Enemies;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.maki.UnhappyDevil.GameMap;
import com.maki.UnhappyDevil.TextDisplay;
import com.maki.UnhappyDevil.Entities.MovableEntity;
import com.maki.UnhappyDevil.Entities.Player;
import com.maki.UnhappyDevil.Entities.Visitable;
import com.maki.UnhappyDevil.Entities.Visitor;
import com.maki.UnhappyDevil.Scenery.EmptySpace;
import com.maki.UnhappyDevil.Scenery.Scenery;

public class Enemy extends MovableEntity implements Visitable {

  private String name;
  private int xpWorthOfEnemy = 10;
  private int health = 10;
  private int damage = 5;
  private boolean hostile;
  private String graphic;
  private Color color;

  public Enemy(GameMap gameMap, int x, int y, int multiplier, String name, TextDisplay textDisplayer) {
    super(gameMap, x, y, multiplier, textDisplayer);
    this.name = name;
  }

  /**
   * damage = 5 health = 10 hostile = true xp = 10 graphic = G color = 0, 128, 0
   * name = goblin
   * 
   * @param map
   * @param x
   * @param y
   * @param multiplier
   * @param data
   */
  public Enemy(GameMap map, int x, int y, int multiplier, Properties data, TextDisplay textDisplay,
      int playerLevel) {
    super(map, x, y, multiplier, textDisplay);
    damage = Integer.parseInt(data.getProperty("damage", "5")) + (playerLevel * 3);
    health = Integer.parseInt(data.getProperty("health", "10")) + (playerLevel * 4);
    hostile = Boolean.parseBoolean(data.getProperty("hostile", "true"));
    xpWorthOfEnemy = Integer.parseInt(data.getProperty("xp", "10")) + playerLevel;
    graphic = data.getProperty("graphic", "??");
    color = parseColor(data.getProperty("color", "(238, 128, 196)"));
    name = data.getProperty("name", "unknown");
  }

  // got this off stack overflow. Your one stop shop for all convenience corner
  // case methods!
  public static Color parseColor(String input) {
    Pattern c = Pattern.compile("\\( *([0-9]+), *([0-9]+), *([0-9]+) *\\)");
    Matcher m = c.matcher(input);

    if (m.matches()) {
      return new Color(Integer.valueOf(m.group(1)), // r
          Integer.valueOf(m.group(2)), // g
          Integer.valueOf(m.group(3))); // b
    }

    return null;
  }

  public String getName() {
    return name;
  }

  protected void die() {
    getTextDisplay().displayText("You have killed a " + name);
    getTileIAmIn().removeEntity();
    setDead(true);
  }

  public void takeDamage(int damageDealt) {
    hostile = true;
    health -= damageDealt;
    if (health <= 0) {
      die();
    }
  }

  @Override
  public void registerWithGameMap() {
    gameMap.registerEnemy(this);
  }

  public int getXp() {
    return xpWorthOfEnemy;
  }

  @Override
  public void paint(Graphics g, int xOffSet, int yOffSet) {
    g.setColor(color);
    g.drawString(graphic, x * multiplier + xOffSet, y * multiplier + yOffSet);
  }

  public void update(Player player) {
    if (!isDead()) {
      if (hostile) {
        Scenery tileToMoveTo = findBestStart(player);
        if (tileToMoveTo != null) {
          gameMap.applyMove(tileToMoveTo.getX(), tileToMoveTo.getY(), this);
        }
        gameMap.deleteParents();
      } else {
        int index = new Random().nextInt(4);
        switch (index) {
        case 0:
          moveUp();
          break;
        case 1:
          moveDown();
          break;
        case 2:
          moveLeft();
          break;
        case 3:
          moveRight();
          break;
        }
      }
    }
  }

  @Override
  public boolean hasToDoSomething() {
    return true;
  }

  @Override
  public boolean getIsWalkable() {
    return false;
  }

  public int getDamage() {
    return damage;
  }

  @Override
  public void accept(Visitor visitor) {
    visitor.visitEnemy(this);
  }

  @Override
  public Visitor getVisitor() {
    return new EnemyVisitor(this);
  }

  // A******************************************************************************************
  // A* algorithm
  // make the algoithm more effecient by changing only the end of the algorithm
  // that has changed

  /**
   * this methods one goal is to use the A* algorithim to populate the path list
   * 
   * @param goal
   * @return
   */
  protected Scenery findBestStart(MovableEntity goal) {
    List<Scenery> opened = new ArrayList<Scenery>();
    List<Scenery> closed = new ArrayList<Scenery>();

    initAStar(opened, getTileIAmIn());
    return AStarAlgorithm(goal, opened, closed);
  }

  private Scenery AStarAlgorithm(MovableEntity goal, List<Scenery> opened, List<Scenery> closed) {
    while (opened.size() > 0) {
      for (Scenery scenery : closed) {
        scenery.highLight();
      }
      for (Scenery scenery : opened) {
        scenery.highLight2();
      }
      // choose the best Scenery (based on F score) in opened and add it to
      // the
      // closed list and remove it from the list
      Scenery best = findBestFScore(goal, opened);
      opened.remove(best);
      closed.add(best);
      // if the one we choose is our goal then be and populate best list
      if (best.equals(goal.getTileIAmIn())) {
        if (closed.size() <= 0) {
          return null;
        } else {
          return closed.get(0);
        }
      } else {
        // else we get the neighbors of the best choice and iterate through
        // them
        Set<Scenery> neighbors = getAdjacencies(best);
        for (Scenery neighbor : neighbors) {
          // if our choice (a specific neighor chosen by the iterator) was one
          // we previously marked as "open" or "closed" (by putting it in the
          // respective list) then check if changing the parent of our choice
          // to
          // this path is better than the path it was on. if it is better then
          // let it be. if it is not do the ordinary operations of adding it
          // to
          // the list

          // check suceeds skip everything
          if (opened.contains(neighbor) || closed.contains(neighbor)) {
            // no need to check its type or anything because only walkable
            // tiles are allowed in to the opened list.
            Scenery tmpSquare = new EmptySpace(neighbor);
            tmpSquare.setParent(best);
            if (tmpSquare.getFCost(goal.getTileIAmIn()) >= neighbor.getFCost(goal.getTileIAmIn())) {
              continue;
            }
          }
          // if it fails continue and place the neighbor at the beginning of
          // the
          // opened list and remove it from the closed list
          neighbor.setParent(best);
          opened.remove(neighbor);
          closed.remove(neighbor);
          opened.add(0, neighbor);
        }
      }
    }
    return null;
  }

  /**
   * collect all of the tiles around us (including us) add them to the list of
   * possibilities and set their parent
   */
  private void initAStar(List<Scenery> opened, Scenery startingScenery) {
    Set<Scenery> adjacencies = new HashSet<Scenery>();
    adjacencies = getAdjacencies(startingScenery);

    for (Scenery adjacency : adjacencies) {
      adjacency.setParent(startingScenery);
      if (!adjacency.equals(startingScenery) && adjacency.isWalkable() && adjacency != null) {
        opened.add(adjacency);
      }
    }
  }

  /**
   * we iterate through a list and find the "best" tile by comparing f scores
   * (called pass through cost) and returning the one that is the best
   * 
   * @param goal
   * @return
   */
  private Scenery findBestFScore(MovableEntity goal, List<Scenery> opened) {
    Scenery best = null;
    for (Scenery square : opened) {
      if (best == null || square.getFCost(goal.getTileIAmIn()) < best.getFCost(goal.getTileIAmIn())) {
        best = square;
      }
    }
    return best;

  }

  /**
   * returns a set of all adjacent sceneries to the scenery we are in
   * 
   * @param startingPoint
   *          the center that we will examine everything around
   * @return
   */
  private Set<Scenery> getAdjacencies(Scenery startingPoint) {
    Set<Scenery> tmpSet = new HashSet<Scenery>();
    Set<Scenery> adjacencies = new HashSet<Scenery>();
    tmpSet.add(gameMap.getSceneryAtPoint(startingPoint.getX(), startingPoint.getY()));
    tmpSet.add(gameMap.getSceneryAtPoint(startingPoint.getX() + 1, startingPoint.getY()));
    tmpSet.add(gameMap.getSceneryAtPoint(startingPoint.getX() - 1, startingPoint.getY()));
    tmpSet.add(gameMap.getSceneryAtPoint(startingPoint.getX(), startingPoint.getY() - 1));
    tmpSet.add(gameMap.getSceneryAtPoint(startingPoint.getX(), startingPoint.getY() + 1));
    for (Scenery scenery : tmpSet) {
      if (scenery.isWalkable()) {
        adjacencies.add(scenery);
      }
    }
    return adjacencies;
  }

}
