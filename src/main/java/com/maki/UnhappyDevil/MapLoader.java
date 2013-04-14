package com.maki.UnhappyDevil;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;
import java.util.Random;

import com.maki.UnhappyDevil.Scenery.NextLevel;
import com.maki.UnhappyDevil.Scenery.Scenery;
import com.maki.UnhappyDevil.Scenery.Wall;

public class MapLoader {
  private static final String PATH_TO_MAPS = "Maps" + File.separator;
//  private static final String PATH_TO_MAPS = "Maps" + ":";
  private String mapFileNames[];
  private int cellWidth;
  private int cellHeight;
  private int actualWidth;
  private int actualHeight;
  private SceneryFactory sceneryFactory;
  private int numOfFiles;
  private List<Point> possibleSpawnPoints = new ArrayList<Point>();
  private boolean spawnedPlayer = false;
  private int notSpawnedPlayerCount = 0;
  private boolean initPlayerMap = false;

  public MapLoader() throws Exception {
    initMapLoader();
    configureMapLoader();
  }

  // I am the only one who can access the encoding_meta_data file so I must do
  // this correctly. every class must be a subclass of scenery
  @SuppressWarnings("unchecked")
  private void configureMapLoader() throws Exception {
    sceneryFactory = new SceneryFactory();
    InputStream is = getClass().getClassLoader().getResourceAsStream(
        PATH_TO_MAPS + "encoding_meta_data.properties");
    Properties encoding = new Properties();
    encoding.load(is);
    // this is a safe cast because propertyNames() will throw a cast class
    // exception if any of the keys are not strings
    Enumeration<String> keys = (Enumeration<String>) encoding.propertyNames();
    while (keys.hasMoreElements()) {
      String key = keys.nextElement();
      // this is also a safe cast because I will ensure that only subclasses
      // of Scenery are able to be loaded
      Class<Scenery> clazz = (Class<Scenery>) Class.forName(encoding.getProperty(key));
      sceneryFactory.configure(key, clazz);
    }

  }

  /**
   * @return the number of files. this is zero based
   */
  public int getNumOfFiles() {
    return numOfFiles;
  }

  private void initMapLoader()  {
    if (!initPlayerMap) {

      Properties properties = new Properties();
      String path = PATH_TO_MAPS + "map_meta.properties";
      InputStream is = getClass().getClassLoader().getResourceAsStream(path);

      System.out.println(is);
      System.out.println(path);

      try {
        properties.load(is);
      } catch (Exception e) {
        System.out.println("error trying to load properties file, execption is: " + e.getMessage());
        e.printStackTrace();
      }
      int min = Integer.parseInt(properties.getProperty("minFiles", "1"));
      int max = Integer.parseInt(properties.getProperty("maxFiles", "3"));
      mapFileNames = new String[max - min + 1];
      for (int j = min; j <= max; j++) {
        mapFileNames[j - min] = "Map" + j + ".DevilMap";
      }
      cellWidth = Integer.parseInt(properties.getProperty("dimensions", "20"));
      cellHeight = cellWidth;
      // I do the *2 because the map is not actually those dimensions. the map
      // puts 4 maps of those dimensions together in a square.
      actualHeight = cellHeight * 2;
      actualWidth = cellWidth * 2;

      numOfFiles = mapFileNames.length;
      initPlayerMap = true;
    }
  }

  private void initPlayerMaps() throws IOException {
    if (initPlayerMap) {
      Properties properties = new Properties();
        InputStream is = getClass().getClassLoader().getResourceAsStream(
            PATH_TO_MAPS + "PlayerMaps" + File.separator + "map_meta.properties");
        properties.load(is);
      int min = Integer.parseInt(properties.getProperty("minFiles", "1"));
      int max = Integer.parseInt(properties.getProperty("maxFiles", "3"));
      mapFileNames = new String[max - min + 1];
      for (int j = min; j <= max; j++) {
        mapFileNames[j - min] = "Map" + j + ".DevilMap";
      }
      cellWidth = Integer.parseInt(properties.getProperty("dimensions", "20"));
      cellHeight = cellWidth;
      // I do the *2 because the map is not actually those dimensions. the map
      // puts 4 maps of those dimensions together in a square.
      actualHeight = cellHeight * 2;
      actualWidth = cellWidth * 2;

      numOfFiles = mapFileNames.length;
      initPlayerMap = false;
    }
  }

  /**
   * 
   * @param g
   *          the gameMap with which to initialize the creature
   * @param mapId
   *          the map you wish to load. Note this is zero based
   * @return the GameMapData that was loaded from the file
   * @throws Exception if something goes wrong
   */
  public GameMapData loadMap(GameMap g, int multiplier, TextDisplay textDisplay, int levelsDeep)
      throws Exception {
    spawnedPlayer = false;
    notSpawnedPlayerCount = 0;
    possibleSpawnPoints.clear();
    // this method is going to manage appending and extending an array that is
    // going to be built by loading 4 different files
    Scenery[][] completeMap;
    Scenery[][] randomMap2 = loadRandomMap(g, multiplier, 0, 1, levelsDeep, textDisplay);
    Scenery[][] randomMap3 = loadRandomMap(g, multiplier, 1, 0, levelsDeep, textDisplay);
    Scenery[][] randomMap4 = loadRandomMap(g, multiplier, 1, 1, levelsDeep, textDisplay);
    Scenery[][] randomMap1 = loadRandomMap(g, multiplier, 0, 0, levelsDeep, textDisplay);
    // I do it in this order so that I can say
    // "if I haven't loaded  a map with a player then I should do so on the fourth time!"

    // these were built using he arraymerge function of eclipse

    // randomMapOneTwo is randomMap3 stacked on top of randomMap2
    Scenery[][] randomMapOneTwo = new Scenery[randomMap1.length + randomMap2.length][];
    System.arraycopy(randomMap1, 0, randomMapOneTwo, 0, randomMap1.length);
    System.arraycopy(randomMap2, 0, randomMapOneTwo, randomMap1.length, randomMap2.length);

    // randomMapThreeFour is randomMap3 stacked on top of randomMap4
    Scenery[][] randomMapThreeFour = new Scenery[randomMap3.length + randomMap4.length][];
    System.arraycopy(randomMap3, 0, randomMapThreeFour, 0, randomMap3.length);
    System.arraycopy(randomMap4, 0, randomMapThreeFour, randomMap3.length, randomMap4.length);

    completeMap = new Scenery[randomMapOneTwo.length][];

    // this iterates all of through all of the contents of mapOneTwo and
    // mapThreeFour and uses the array merge on them
    for (int i = 0; i < randomMapOneTwo.length; i++) {
      Scenery[] tmpArray = new Scenery[randomMapOneTwo[i].length + randomMapThreeFour[i].length];
      System.arraycopy(randomMapOneTwo[i], 0, tmpArray, 0, randomMapOneTwo[i].length);
      System.arraycopy(randomMapThreeFour[i], 0, tmpArray, randomMapOneTwo[i].length,
          randomMapThreeFour[i].length);
      completeMap[i] = tmpArray;
    }

    for (int y = 0; y < completeMap.length; y++) {
      for (int x = 0; x < completeMap[y].length; x++) {
        // if x or y equals 0 Or if x or y = edge of the array (arrays are 0
        // based so I had to put that -1 in) place a wall
        if (x == 0 || x == actualWidth - 1 || y == 0 || y == actualHeight - 1) {
          completeMap[y][x] = new Wall(x, y, g, multiplier);
        }
      }
    }

    completeMap[completeMap.length - 2][completeMap.length - 2] = new NextLevel(g,
        completeMap.length - 2, completeMap.length - 2, multiplier);
    GameMapData gameMapData = new GameMapData(completeMap);
    gameMapData.setSpawnPoint(possibleSpawnPoints);
    return gameMapData;
  }

  private Scenery[][] loadRandomMap(GameMap g, int multiplier, int xOfMap, int yOfMap,
      int levelsDeep, TextDisplay textDisplay) throws Exception {
    String path = PATH_TO_MAPS;
    if (notSpawnedPlayerCount > 2) {
      path += "PlayerMaps" + File.separator;
      initPlayerMaps();
    } else {
      initMapLoader();
    }
    int index = new Random().nextInt(numOfFiles);

    // create an input stream from the class loader and make an input stream
    // reader from that and make a buffered reader from the input stream reader
    BufferedReader br = new BufferedReader(new InputStreamReader(getClass().getClassLoader()
        .getResourceAsStream(path + mapFileNames[index])));

    Scenery[][] data = new Scenery[cellHeight][cellWidth];
    String currentLine;
    int y = 0;
    while ((currentLine = br.readLine()) != null) {
      String sceneryChars[] = currentLine.split(" ");

      for (int x = 0; x < sceneryChars.length; x++) {
        String sceneryChar = sceneryChars[x];
        Scenery scenery = sceneryFactory.getScenery(sceneryChar, x + xOfMap * cellWidth, y + yOfMap
            * cellHeight, g, multiplier, levelsDeep, textDisplay);
        data[y][x] = scenery;

        if (sceneryChar.equals("P")) {
          possibleSpawnPoints.add(new Point(x + xOfMap * cellWidth, y + yOfMap * cellHeight));
          spawnedPlayer = true;
        }
      }
      y++;
    }
    if (!spawnedPlayer) {
      notSpawnedPlayerCount++;
    }
    br.close();
    return data;
  }
}
