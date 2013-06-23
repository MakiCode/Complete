package dadsCard;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Stack;

public class Board {
  private int[][] tiles;
  private int N;
  private boolean hammed;
  private int hamming;
  private boolean manhattened;
  private int manhattan;

  public Board(int[][] blocks) {
    tiles = copyTiles(blocks);
    N = tiles.length;
    manhattan();
    hamming();
  }

  // (where blocks[i][j] = block in row i, column j)
  public int dimension() {
    return N;
  } // board dimension N

  public int hamming() {
    int compare = 1;
    if (!hammed) {
      for (int i = 0; i < tiles.length; i++) {
        for (int j = 0; j < tiles[i].length; j++) {
          if (tiles[i][j] != 0) {
            if (tiles[i][j] != compare) {
              hamming++;
            }
          }
          compare++;
        }
      }
      hammed = true;
      return hamming;
    }
    return hamming;
  } // number of blocks out of place

  public int manhattan() {
    if (!manhattened) {
      int manhattanDistanceSum = 0;
      for (int x = 0; x < N; x++)
        // x-dimension, traversing rows (i)
        for (int y = 0; y < N; y++) { // y-dimension, traversing cols (j)
          int value = tiles[x][y]; // tiles array contains board elements
          if (value != 0) { // we don't compute MD for element 0
            int targetX = (value - 1) / N; // expected x-coordinate (row)
            int targetY = (value - 1) % N; // expected y-coordinate (col)
            int dx = x - targetX; // x-distance to expected coordinate
            int dy = y - targetY; // y-distance to expected coordinate
            manhattanDistanceSum += Math.abs(dx) + Math.abs(dy);
          }
        }
      manhattan = manhattanDistanceSum;
    }
    return manhattan;
  } // sum of Manhattan distances between blocks and goal

  public boolean isGoal() {
    return hamming == 0;
  } // is this board the goal board?

  public Board twin() {
    Random random = new Random();
    int randRow = random.nextInt(N);
    int randColumn = random.nextInt(N - 1);
    while (tiles[randRow][randColumn] == 0 || tiles[randRow][randColumn + 1] == 0) {
      randColumn = random.nextInt(N - 1);
      randRow = random.nextInt(N);
    }
    Board twin = swap(randRow, randColumn, randRow, randColumn + 1);
    return twin;
  } // a board obtained by exchanging two adjacent blocks in the same row

  private int[][] copyTiles() {
    int[][] copy = new int[N][N];
    for (int i = 0; i < tiles.length; i++) {
      for (int j = 0; j < tiles[i].length; j++) {
        copy[i][j] = tiles[i][j];
      }
    }
    return copy;
  }

  private int[][] copyTiles(int[][] blocks) {
    int[][] copy = new int[blocks.length][blocks.length];
    for (int i = 0; i < blocks.length; i++) {
      for (int j = 0; j < blocks[i].length; j++) {
        copy[i][j] = blocks[i][j];
      }
    }
    return copy;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + N;
    result = prime * result + hamming;
    result = prime * result + manhattan;
    result = prime * result + Arrays.hashCode(tiles);
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (!(obj instanceof Board))
      return false;
    Board other = (Board) obj;
    if (N != other.N)
      return false;
    if (hamming != other.hamming)
      return false;
    if (manhattan != other.manhattan)
      return false;
    if (!Arrays.deepEquals(tiles, other.tiles))
      return false;
    return true;
  }

  public List<Board> neighbors() {
    int zeroRow = 0;
    int zeroColumn = 0;
    boolean condition = false;
    for (int rows = 0; rows < tiles.length; rows++) {
      if (condition) {
        break;
      }
      for (int columns = 0; columns < tiles[rows].length; columns++) {
        if (condition) {
          break;
        }
        if (tiles[rows][columns] == 0) {
          zeroRow = rows;
          zeroColumn = columns;
          condition = true;
        }
      }
    }
    boolean upPossible = false;
    boolean downPossible = false;
    boolean leftPossible = false;
    boolean rightPossible = false;
    // discover which sides we can go to.
    if (zeroRow + 1 < N) {
      downPossible = true;
    }
    if (zeroRow - 1 >= 0) {
      upPossible = true;
    }
    if (zeroColumn + 1 < N) {
      rightPossible = true;
    }
    if (zeroColumn - 1 >= 0) {
      leftPossible = true;
    }

    Stack<Board> stack = new Stack<Board>();

    if (upPossible) {
      stack.push(swap(zeroRow, zeroColumn, zeroRow - 1, zeroColumn));
    }
    if (downPossible) {
      stack.push(swap(zeroRow, zeroColumn, zeroRow + 1, zeroColumn));
    }
    if (leftPossible) {
      stack.push(swap(zeroRow, zeroColumn, zeroRow, zeroColumn - 1));
    }
    if (rightPossible) {
      stack.push(swap(zeroRow, zeroColumn, zeroRow, zeroColumn + 1));
    }
    return stack;
  } // all neighboring boards. Neighboring boards are defined as all boards that
    // can be made moving the empty space left, right, up, or down.

  // Returns a new board with the specified elements swapped
  private Board swap(int intialRow, int initialColumn, int goalRow, int goalColumn) {
    int[][] copyOftiles = copyTiles();
    int origAtRowColumn = copyOftiles[intialRow][initialColumn];
    copyOftiles[intialRow][initialColumn] = copyOftiles[goalRow][goalColumn];
    copyOftiles[goalRow][goalColumn] = origAtRowColumn;
    return new Board(copyOftiles);
  }

  public String toString() {
    StringBuilder s = new StringBuilder();
    s.append(N + "\n");
    for (int i = 0; i < N; i++) {
      for (int j = 0; j < N; j++) {
        s.append(String.format("%2d ", tiles[i][j]));
      }
      s.append("\n");
    }
    return s.toString();
  } // string representation of the board (in the output format specified below)

  public int[][] getTiles() {
    return tiles;
  }

}
