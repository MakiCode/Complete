package com.maki.UnhappyDevil;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import com.maki.UnhappyDevil.Entities.Player;
import com.maki.UnhappyDevil.Screens.ErrorScreen;
import com.maki.UnhappyDevil.Screens.MenuScreen;
import com.maki.UnhappyDevil.Screens.ScoreScreen;
import com.maki.UnhappyDevil.Screens.StartScreen;
import com.maki.UnhappyDevil.Screens.levelUpScreen;

public class DevilPanel extends JPanel {
  //TODO add an error display screen instead of just catching and ignoring problems
  private GameMap gameMap;
  private Player player;
  private TextDisplay textDisplayer;
  private StatsDisplay StatsDisplayer;
  private Properties userKeyBindings;
  private Map<String, Action> actionBindings;
  private List<Drawable> drawableElements = new ArrayList<Drawable>();

  public DevilPanel(int width, int height) {
    setSize(width, height);
    setFocusable(true);
    setVisible(true);
    drawableElements.add(new MenuScreen(this));
  }

  public void loadGame() {
    addKeyBindings();
    drawableElements.clear();
    initTextDisplay();
    initStatsDisplay();
    initGameMap();
    repaint();
  }

  private void initTextDisplay() {
    // TODO Change these to variables
    textDisplayer = new TextDisplay(1000);
    drawableElements.add(textDisplayer);
  }

  private void initStatsDisplay() {
    // TODO change these to variables
    StatsDisplayer = new StatsDisplay(1000, 1000);
    drawableElements.add(StatsDisplayer);
  }

  private void initGameMap() {
    gameMap = new GameMap(0, 30, 17, textDisplayer, this);
    player = new Player(0, 0, gameMap, 17, StatsDisplayer, textDisplayer, this);
    try {
      gameMap.loadMap(player.getDepth());
    } catch (Exception e) {
      throwException(e);
    }
    gameMap.addPlayer(player);
    drawableElements.add(gameMap);
  }

  private void addKeyBindings() {
    actionBindings = new HashMap<String, Action>();
    actionBindings.put("w", new AbstractAction() {

      @Override
      public void actionPerformed(ActionEvent arg0) {
        player.moveUp();
        gameMap.updateEnemies(player);
        repaint();
      }
    });

    actionBindings.put("a", new AbstractAction() {

      @Override
      public void actionPerformed(ActionEvent arg0) {
        player.moveLeft();
        gameMap.updateEnemies(player);
        repaint();
      }
    });

    actionBindings.put("s", new AbstractAction() {

      @Override
      public void actionPerformed(ActionEvent arg0) {
        player.moveDown();
        gameMap.updateEnemies(player);
        repaint();
      }
    });

    actionBindings.put("d", new AbstractAction() {

      @Override
      public void actionPerformed(ActionEvent arg0) {
        player.moveRight();
        gameMap.updateEnemies(player);
        repaint();
      }
    });
    try {
      loadKeybindings();
    } catch (Exception e) {
      drawableElements.clear();
      drawableElements.add(new ErrorScreen(e, this));
    }
    bindKeys();
  }

  private void loadKeybindings() throws Exception {
    userKeyBindings = new Properties();
    InputStream is = getClass().getClassLoader().getResourceAsStream("keybindings.properties");
    userKeyBindings.load(is);
  }

  public void bindKeys() {
    for (String key : userKeyBindings.stringPropertyNames()) {
      // this gets the action defined above
      Action anAction = actionBindings.get(key);
      // this get the value of the property, e.g. moveUp
      String actionName = userKeyBindings.getProperty(key);
      String keyTyped = String.format("typed %s", key);
      KeyStroke keyStroke = KeyStroke.getKeyStroke(keyTyped);
      getInputMap(WHEN_IN_FOCUSED_WINDOW).put(keyStroke, actionName);
      getActionMap().put(actionName, anAction);
    }

  }

  @Override
  public void paint(Graphics g) {
    g.setColor(Color.BLACK);
    // TODO change these to variables
    g.fillRect(0, 0, 1000, 1000);
    for (Drawable drawable : drawableElements) {
      drawable.paint(g);
    }
  }

  public void scoreScreen(int gold, int killCount, int levelInDungeon, int playerLevel, int xp) {
    drawableElements.clear();
    drawableElements.add(new ScoreScreen(gold, killCount, levelInDungeon, playerLevel, xp, this));
  }

  public void displayLevelUpScreen() {
    List<Drawable> tmpList = new ArrayList<Drawable>(drawableElements);
    drawableElements.clear();
    drawableElements.add(new levelUpScreen(player, tmpList, this));
  }

  public void setDrawableElements(List<Drawable> drawable) {
    drawableElements = drawable;
  }

  public void restartGame() {
    drawableElements.clear();
    getInputMap().clear();
    drawableElements.add(new MenuScreen(this));
    repaint();
  }

//  public int getHighScore() {
//    InputStream is = getClass().getClassLoader().getResourceAsStream("highscore.properties");
//    Properties highscore = new Properties();
//    try {
//      highscore.load(is);
//    } catch (IOException e) {
//      e.printStackTrace();
//    }
//    return Integer.parseInt(highscore.getProperty("highscore"));
//  }

//  public void writeHighScore(int highScore) {
//    Properties properties = new Properties();
//    properties.setProperty("highscore", highScore + "");
//    URL url = getClass().getClassLoader().getResource("highscore.properties");
//    System.out.println(url);
//    OutputStream out;
//    try {
//      out = new FileOutputStream(url.getPath());
//      properties.store(out, null);
//    } catch (IOException e) {
//      e.printStackTrace();
//    }
//  }

  public void displayStartScreen() {
    drawableElements.clear();
    drawableElements.add(new StartScreen(this));
    repaint();
  }

  public void throwException(Throwable e1) {
    drawableElements.clear();
    drawableElements.add(new ErrorScreen(e1, this));
  }

}
