package src.Card;
import java.util.PriorityQueue;
import java.util.Stack;

public class Solver {

  private PriorityQueue<Node> minPQOrig;
  private PriorityQueue<Node> minPQTwin;
  private Board origBoard;
  private Board twin;
  private boolean isSolvable;
  private Node path;
  private Stack<Board> stack;

  public Solver(Board initial) {
    origBoard = initial;
    twin = origBoard.twin();
    minPQOrig = new PriorityQueue<Solver.Node>();
    minPQTwin = new PriorityQueue<Solver.Node>();
    isSolvable = false;
    solve();
  } // find a solution to the initial board (using the A* algorithm)

  // run A* How to use twin: to detect if a puzzle is infeasible minPQ both
  // the main and the twin and then run the algorithm normally. After it has
  // completed chase the pointers to the top of the tree and decide whether
  // you used the twin or the original. If twin then puzzle is unsolvable. If
  // original then keep that as the solution. NOTE: I do not have to worry
  // about whether both puzzles will be solvable. The specifications
  // Specifically say that only the original OR the twin will be solvable
  private void solve() {
    Node node1 = new Node(origBoard, 0, null);
    Node node2 = new Node(twin, 0, null);
    minPQOrig.add(node1);
    minPQTwin.add(node2);
    Node currentNode = node1;
    Node twinNode = node2;
    while (!currentNode.board.isGoal() && !twinNode.board.isGoal()) {
      if (minPQOrig.isEmpty()) {
        isSolvable = false;
        break;
      }
      if (!minPQTwin.isEmpty()) {
        twinNode = solveIteration(twinNode, minPQTwin);
      }
      currentNode = solveIteration(currentNode, minPQOrig);
    }
    if (twinNode.board.isGoal()) {
      isSolvable = false;
    } else {
      isSolvable = true;
      path = currentNode;
      makeStack();
    }
  }

  private Node solveIteration(Node startNode, PriorityQueue<Node> minPQ) {
    for (Board board : startNode.board.neighbors()) {
      Node node = new Node(board, startNode.moves + 1, startNode);
      if (node.previousNode != null) {
        Node node1 = new Node(node);
        boolean addIn = true;
        while (node1.previousNode != null) {
          node1 = node1.previousNode;
          if (node1.board.equals(node.board)) {
            addIn = false;
            break;
          }
        }
        if (addIn) {
          minPQ.add(node);
        }
      }
    }
    return minPQ.remove();
  }

  private void makeStack() {
    stack = new Stack<Board>();
    Node node = path;
    while (node != null) {
      // I use a stack so that I can Reverse the order
      stack.push(node.board);
      node = node.previousNode;
    }
  }

  public boolean isSolvable() {
    return isSolvable; // This will be a variable I set in solve
  } // is the initial board solvable?

  public int moves() {
    if (!isSolvable) {
      return -1;
    }
    return path.moves;
  } // min number of moves to solve initial board; -1 if no solution

  public Stack<Board> solution() {
    return stack;
  } // sequence of boards in a shortest solution; null if no solution

  private class Node implements Comparable<Node> {

    private Board board;
    private int moves;
    private Node previousNode;

    public Node(Board board, int moves, Node previousNode) {
      this.board = board;
      this.moves = moves;
      this.previousNode = previousNode;
    }

    public Node(Node node) {
      this.moves = node.moves;
      this.previousNode = node.previousNode;
      this.board = node.board;
    }

    @Override
    public int compareTo(Node o) {
      if (board.manhattan() + moves < o.board.manhattan() + o.moves) {
        return -1;
      }
      if (board.manhattan() + moves > o.board.manhattan() + o.moves) {
        return 1;
      }
      return 0;
    }
  }
}