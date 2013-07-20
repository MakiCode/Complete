package src.dadsCard;

public class EightPuzzleMain {

	public static void main(final String[] args) {
		EightPuzzle eightPuzzle = new EightPuzzle(450, 450, 3);
		eightPuzzle.createFrame();
	}
}

/**
 * Handle clicking the next move button. Displays warning if needed.
 */
private void handleNext() {
boolean doNextMove = true;
if (numOfTilesOnSide >= 5) {
Board board = new Board(gameBoard);
if (board.hamming() > Math.pow(numOfTilesOnSide, 2) / 3) {
if (!showWarning()) {
doNextMove = false;
}
}
}
if (doNextMove) {
findNextMove();
}
repaint();
}

/**
 * Display a warning saying that the algorithm to solve the puzzle may take
 * a long time, An infeasible long time
 *
 * @return true if the player wants to continue the next move operation
 */
private boolean showWarning() {
int choice = JOptionPane.showConfirmDialog(this,
"WARNING: This puzzle is rather large.\n"
+ "Finding the next move may take\n"
+ "a VERY long time if the puzzle\n"
+ "is heavily scrambled. Do you\n"
+ "want to continue with the next\n"
+ "move operation?");
if (choice == 0) {
return true;
} else {
return false;
}
}

/**
 * Display a JOptionPane and execute the proper action based on the users
 * input
 */
private void showOptions() {
int choice = JOptionPane.showConfirmDialog(this,
"Puzzle complete.\nWould you like to retry the puzzle?");
if (choice == 0) {
scramble();
}
repaint();
}