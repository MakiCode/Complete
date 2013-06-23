package dadsCard;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Stack;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class EightPuzzle extends JPanel implements MouseListener {
  private static final int SIDE_IMG_SIZE = 150;
  private final int FRAME_WIDTH = 450;
  private final int FRAME_HEIGHT = 550;

  public static void main(String[] args) {
    JFrame frame = new JFrame();
    frame.setSize(new Dimension(450, 550));
    Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
    frame.setLocation(dim.width / 2 - frame.getSize().width / 2, dim.height / 2
        - frame.getSize().height / 2);
    EightPuzzle eightPuzzle = new EightPuzzle();
    frame.add(eightPuzzle);
    frame.setResizable(false);
    frame.setVisible(true);
  }
  
  private static class ImageRect {
    private BufferedImage img;
    private Rectangle2D rect;
    private Color color;
    
    public ImageRect(Color color, Rectangle2D rect) {
      super();
      this.rect = rect;
      this.color = color;
    }

    public ImageRect(BufferedImage img, Rectangle2D rect) {
      super();
      this.img = img;
      this.rect = rect;
    }

    public BufferedImage getImg() {
      return img;
    }

    public Rectangle2D getRect() {
      return rect;
    }

    public Color getColor() {
      return color;
    }
    
  }

  private Map<Integer, Image> imageMap;
  private Map<Integer, Color> colorMap;
  private Rectangle nextMove = new Rectangle(FRAME_WIDTH / 3, FRAME_HEIGHT - 85,
      (FRAME_WIDTH / 3) / 2, 50);
  private Rectangle scramble = new Rectangle(FRAME_WIDTH / 3 + (FRAME_WIDTH / 3) / 2 + 10,
      FRAME_HEIGHT - 85, (FRAME_WIDTH / 3) / 2, 50);
  private int[][] gameBoard = { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 0 } };
  private ImageRect[][] rectangleBoard = new ImageRect[3][3];
  private boolean clickingNext = false;
  private boolean clickingScramble = false;
  private boolean ioException = false;

  public EightPuzzle() {
    addMouseListener(this);
    generateMap();
    scramble();
  }

  private static BufferedImage resizeImage(BufferedImage originalImage) {
    BufferedImage resizedImage = new BufferedImage(450, 450, originalImage.getType());
    Graphics2D g = resizedImage.createGraphics();
    g.drawImage(originalImage, 0, 0, 450, 450, null);
    g.dispose();
    return resizedImage;
  }

  private void generateMap() {
    imageMap = new HashMap<Integer, Image>();
    colorMap = new HashMap<Integer, Color>();
    BufferedImage img = null;
    try {
      URL url = this.getClass().getResource("image.png");
      if (url == null) {
        throw new IOException("Input was wron");
      }
      img = ImageIO.read(url);
      img = resizeImage(img);
      int n = 1;
      for (int i = 0; i < 3; i++) {
        for (int j = 0; j < 3; j++) {
          if (i == 2 && j == 2) {
            Graphics g = img.getGraphics();
            g.setColor(Color.LIGHT_GRAY);
            g.fillRect(j * SIDE_IMG_SIZE, i * SIDE_IMG_SIZE, SIDE_IMG_SIZE, SIDE_IMG_SIZE);
            
            rectangleBoard[2][2] = new ImageRect(img.getSubimage(j * SIDE_IMG_SIZE, i * SIDE_IMG_SIZE, SIDE_IMG_SIZE,
                    SIDE_IMG_SIZE), new Rectangle2D.Double(j * SIDE_IMG_SIZE, i * SIDE_IMG_SIZE, SIDE_IMG_SIZE,
                    SIDE_IMG_SIZE));
            
            imageMap
                .put(0, img.getSubimage(j * SIDE_IMG_SIZE, i * SIDE_IMG_SIZE, SIDE_IMG_SIZE,
                    SIDE_IMG_SIZE));
          } else {
            imageMap
                .put(n, img.getSubimage(j * SIDE_IMG_SIZE, i * SIDE_IMG_SIZE, SIDE_IMG_SIZE,
                    SIDE_IMG_SIZE));
          }
          n++;
        }
      }
    } catch (IOException e) {
      System.err.println("There was an error while loading the file. Moving to color version");
      ioException = true;
      Stack<Color> stack = new Stack<Color>();
      stack.add(Color.WHITE);
      stack.add(Color.BLUE);
      stack.add(Color.CYAN);
      stack.add(Color.GREEN);
      stack.add(Color.MAGENTA);
      stack.add(Color.ORANGE);
      stack.add(Color.PINK);
      stack.add(Color.YELLOW);
      stack.add(Color.RED);
      for (int i = 0; i < 3; i++) {
        for (int j = 0; j < 3; j++) {
          rectangleBoard[i][i] = new ImageRect(stack.pop(), new Rectangle2D.Double(j * SIDE_IMG_SIZE, i * SIDE_IMG_SIZE, SIDE_IMG_SIZE,
              SIDE_IMG_SIZE));
        }
        
      }
      colorMap.put(0, Color.WHITE);
      colorMap.put(1, Color.BLUE);
      colorMap.put(2, Color.CYAN);
      colorMap.put(3, Color.GREEN);
      colorMap.put(4, Color.MAGENTA);
      colorMap.put(5, Color.ORANGE);
      colorMap.put(6, Color.PINK);
      colorMap.put(7, Color.YELLOW);
      colorMap.put(8, Color.RED);
    }
  }

  private void scramble() {
    Random randomGen = new Random();
    int loopVar = 25;
    for (int i = 0; i < loopVar; i++) {
      Board board = new Board(gameBoard);
      List<Board> boardNeighbors = board.neighbors();
      gameBoard = boardNeighbors.get(randomGen.nextInt(boardNeighbors.size())).getTiles();
    }
  }

  private void findNextMove() {
    Solver solver = new Solver(new Board(gameBoard));
    Stack<Board> stack = solver.solution();
    stack.pop();
    if (!stack.isEmpty()) {
      gameBoard = stack.pop().getTiles();
    }
  }

  private void checkState() {
    int n = 1;
    boolean broken = false;
    for (int i = 0; i < gameBoard.length; i++) {
      for (int j = 0; j < gameBoard[i].length; j++) {
        // if this is the last part
        if (i == (gameBoard.length - 1) && j == (gameBoard[(gameBoard.length - 1)].length - 1)) {
          if (gameBoard[i][j] == 0) {
            continue;
          }
        } else {
          if (gameBoard[i][j] == n) {
            n++;
            continue;
          } else {
            broken = true;
            break;
          }
        }

      }
      if (broken) {
        break;
      }
    }
    if (!broken) {
      int choice = JOptionPane.showConfirmDialog(this,
          "Puzzle complete.\nWould you like to retry the puzzle?");
      if (choice == 0) {
        scramble();
      }
      repaint();
    }
  }

  @Override
  public void paint(Graphics g) {
    Graphics2D g2d = (Graphics2D) g;
    if (ioException == true) {
      for (int rows = 0; rows < gameBoard.length; rows++) {
        for (int columns = 0; columns < gameBoard[rows].length; columns++) {
          g2d.setColor(colorMap.get(gameBoard[rows][columns]));
          g2d.fillRect(columns * SIDE_IMG_SIZE, rows * SIDE_IMG_SIZE, SIDE_IMG_SIZE, SIDE_IMG_SIZE);
          g2d.setColor(Color.BLACK);
          g2d.drawRect(columns * SIDE_IMG_SIZE, rows * SIDE_IMG_SIZE, SIDE_IMG_SIZE, SIDE_IMG_SIZE);
          if (gameBoard[rows][columns] != 0) {
            Font font = g2d.getFont();
            Font font2 = font.deriveFont(60f);
            g2d.setFont(font2);
            g2d.drawString(gameBoard[rows][columns] + "", (columns * SIDE_IMG_SIZE) + SIDE_IMG_SIZE
                / 2 - 20, (rows * SIDE_IMG_SIZE) + SIDE_IMG_SIZE / 2 + 20);
            g2d.setFont(font);
          }
        }
      }
    } else {
      for (int rows = 0; rows < gameBoard.length; rows++) {
        for (int columns = 0; columns < gameBoard[rows].length; columns++) {
          // potential spot for animation
          g.drawImage(imageMap.get(gameBoard[rows][columns]), columns * SIDE_IMG_SIZE, rows
              * SIDE_IMG_SIZE, SIDE_IMG_SIZE, SIDE_IMG_SIZE, null);
          g2d.setColor(Color.BLACK);
          g2d.drawRect(columns * SIDE_IMG_SIZE, rows * SIDE_IMG_SIZE, SIDE_IMG_SIZE, SIDE_IMG_SIZE);
        }
      }
    }
    g2d.setColor(Color.LIGHT_GRAY);
    g2d.fillRect(0, FRAME_HEIGHT - (FRAME_HEIGHT - FRAME_WIDTH), FRAME_WIDTH, FRAME_HEIGHT
        - FRAME_WIDTH);
    g2d.setColor(Color.BLACK);
    g2d.drawRect(0, FRAME_HEIGHT - (FRAME_HEIGHT - FRAME_WIDTH), FRAME_WIDTH, FRAME_HEIGHT
        - FRAME_WIDTH);
    g2d.setColor(Color.CYAN);
    g2d.fill3DRect(nextMove.x, nextMove.y, nextMove.width, nextMove.height, !clickingNext);
    g2d.fill3DRect(scramble.x, scramble.y, scramble.width, scramble.height, !clickingScramble);
    g2d.setColor(Color.BLACK);
    g2d.drawString("Next Move", nextMove.x + 4, nextMove.y + 28);
    g2d.drawString("Scramble", scramble.x + 9, scramble.y + 28);

  }

  private boolean swap(int x, int y) {
    if (!(x >= 3 || y >= 3 || x < 0 || y < 0)) {
      Point zeroCoords = getZeroCoords();
      if ((Math.abs(x - zeroCoords.x) == 1 ^ Math.abs(y - zeroCoords.y) == 1)) {
        if (Math.abs(y - zeroCoords.y) == 1 && (Math.abs(x - zeroCoords.x) == 0)) {
          swap(x, y, zeroCoords);
          return true;
        } else {
          if (Math.abs(x - zeroCoords.x) == 1 && (Math.abs(y - zeroCoords.y) == 0)) {
            swap(x, y, zeroCoords);
            return true;
          }
        }
      }
    }
    return false;
  }

  private void swap(int x, int y, Point zeroCoords) {
    boolean animating = true;
    int frame = 0;
    int temp = gameBoard[y][x];
    gameBoard[y][x] = gameBoard[zeroCoords.y][zeroCoords.x];
    gameBoard[zeroCoords.y][zeroCoords.x] = temp;
    while(animating) {
      System.out.println("Frame: " + frame);
      try {
        Thread.sleep(10);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      if(frame == 30) {
        animating = false;
      }
      frame++;
      repaint();
    }
  }

  private Point getZeroCoords() {
    for (int x = 0; x < gameBoard.length; x++) {
      for (int y = 0; y < gameBoard[x].length; y++) {
        if (gameBoard[y][x] == 0) {
          return new Point(x, y);
        }
      }
    }
    return null;
  }

  @Override
  public void mouseClicked(MouseEvent e) {
    if (swap(e.getX() / SIDE_IMG_SIZE, e.getY() / SIDE_IMG_SIZE)) {
      repaint();
    }
    checkState();
  }

  @Override
  public void mouseEntered(MouseEvent arg0) {

  }

  @Override
  public void mouseExited(MouseEvent arg0) {

  }

  @Override
  public void mousePressed(MouseEvent e) {
    if (nextMove.contains(new Point(e.getX(), e.getY()))) {
      clickingNext = true;
      repaint();
    }
    if (scramble.contains(new Point(e.getX(), e.getY()))) {
      clickingScramble = true;
      repaint();
    }
  }

  @Override
  public void mouseReleased(MouseEvent arg0) {
    if (clickingNext) {
      findNextMove();
      repaint();
      checkState();
    } else if (clickingScramble) {
      scramble();
      repaint();
      checkState();
    }
    clickingNext = false;
    clickingScramble = false;
  }

}
