package src.Card;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Stack;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * An Eight Puzzle game where you can click on blocks and they slide into an
 * empty slot if it is next to them. The goal is to organize all of the blocks.
 * 
 * @author Huulktya
 * 
 */
@SuppressWarnings("serial")
public class EightPuzzle extends JPanel {
	// TODO: Work on animator

	private int currentPanelSize;
	private int sideSize;
	private Map<Integer, Image> imageMap;
	private int[][] gameBoard;
	private int numOfTilesOnSide;
	private int numOfFrames = 30;
	private Image currentImage;
	private EightPuzzleLoader eightPuzzleLoader;

	/**
	 * Activate the animation loop. This Method runs forever so be careful.
	 */
	public void animate() {
		for (int i = 0; i < numOfFrames; i++) {
			repaint();
			System.out.println("Animating");
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Create an Instance of EightPuzzle in a JPanel.
	 * 
	 * @param frameSize
	 *            The Size (width and height) of the Frame this game will be put
	 *            into
	 * @param numOfTilesOnSideVal
	 *            The number of tiles on one side of the game
	 * @param eightPuzzleLoader2
	 */
	public EightPuzzle(final int frameSize, final int numOfTilesOnSideVal,
			EightPuzzleLoader eightPuzzleLoader2) {
		makeVars(frameSize, numOfTilesOnSideVal);
		addMouseListener(new MyMouseListener());
		eightPuzzleLoader = eightPuzzleLoader2;
	}

	public void createGame() {
		generateMap(eightPuzzleLoader.getDefaultURL());
		scramble();
	}

	/**
	 * Redefine the variables pertaining to the screen dimensions
	 * 
	 * @param frameSize
	 *            the size of both sides of the frame
	 * @param numOfTilesOnSideVal
	 *            The number of tiles on a single side of the puzzle
	 */
	public void makeVars(final int frameSize, int numOfTilesOnSideVal) {
		numOfTilesOnSide = numOfTilesOnSideVal;
		currentPanelSize = frameSize;
		sideSize = currentPanelSize / numOfTilesOnSideVal;

		if (gameBoard == null) {
			gameBoard = new int[numOfTilesOnSideVal][numOfTilesOnSideVal];
			int n = 1;
			for (int i = 0; i < numOfTilesOnSideVal; i++) {
				for (int j = 0; j < numOfTilesOnSideVal; j++) {
					if (i == (numOfTilesOnSideVal - 1)
							&& j == (numOfTilesOnSideVal - 1)) {
						gameBoard[i][j] = 0;
					} else {
						gameBoard[i][j] = n;
					}
					n++;
				}
			}
		}
		addMouseListener(new MyMouseListener());
		repaint();
	}

	/**
	 * Resizes the dimensions of the game
	 * 
	 * @param numOfTilesOnSideVal
	 *            The number of tiles on a single side of the puzzle
	 */
	public void resizeGame(final int frameSize, int numOfTilesOnSideVal) {
		gameBoard = null;
		makeVars(frameSize, numOfTilesOnSideVal);
		generateMap(eightPuzzleLoader.getLastUrl());
		scramble();
		repaint();
	}

	@Override
	public final void paint(final Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		for (int rows = 0; rows < gameBoard.length; rows++) {
			for (int columns = 0; columns < gameBoard[rows].length; columns++) {
				// potential spot for animation
				int x = columns * sideSize;
				int y = rows * sideSize;
				g.drawImage(
						resizeImage((BufferedImage) imageMap
								.get(gameBoard[rows][columns]), sideSize,
								sideSize), x, y, null);
				g2d.setColor(Color.BLACK);
				g2d.drawRect(x, y, sideSize, sideSize);
			}
		}
	}

	/**
	 * Calculate the proper position (x,y) on the screen
	 * 
	 * @param rect
	 *            The rectangle that will be placed in the center of the
	 *            backgorund
	 * @param background
	 *            the rectangle that the rect will be placed onto
	 * @return the correct x,y position on the screen
	 */
	static Point getCenterPosition(final Dimension rect,
			final Dimension background) {
		if (rect.getWidth() > background.getWidth()
				|| rect.getHeight() > background.getHeight()) {
			throw new ArithmeticException(
					"background was too small for the rect!");
		} else {
			int x = background.width / 2 - rect.getSize().width / 2;
			int y = background.height / 2 - rect.getSize().height / 2;
			return new Point(x, y);
		}
	}

	/**
	 * Resize the image to a given width and height
	 * 
	 * @param originalImage
	 *            the image to resize
	 * @param height
	 *            the height of the resized image
	 * @param width
	 *            the width of the resized image
	 * @return the original image resized to the given dimensions
	 */
	/**
	 * Split up the image into tiles numbered 1-(numOfTilesOnSide^2 - 1) with
	 * the bottom right tile being numbered 0
	 * 
	 * @param img
	 *            The image to split up
	 * @param numOfTilesOnSide
	 *            the number of tiles on one side of the image
	 * @return a map of the tiles of the image to the numbers as specified above
	 */
	private Map<Integer, Image> splitImg(BufferedImage img, int numOfTilesOnSide) {
		Map<Integer, Image> map = new HashMap<>();
		
		int sideWidth2 = img.getWidth() / numOfTilesOnSide;
		int sideHeight2 = img.getHeight() / numOfTilesOnSide;
		
		int n = 1;
		for (int i = 0; i < numOfTilesOnSide; i++) {
			for (int j = 0; j < numOfTilesOnSide; j++) {
				if (i == (numOfTilesOnSide - 1) && j == (numOfTilesOnSide - 1)) {
					map.put(0, img.getSubimage(j * sideWidth2, i * sideHeight2,
							sideWidth2, sideHeight2));
				} else {
					map.put(n, img.getSubimage(j * sideWidth2, i * sideHeight2,
							sideWidth2, sideHeight2));
				}
				n++;
			}
		}
		return map;
	}
	
	private static BufferedImage resizeImage(final BufferedImage originalImage,
			int width, int height) {
		BufferedImage resizedImage = new BufferedImage(width, height,
				originalImage.getType());
		Graphics2D g = resizedImage.createGraphics();
		g.drawImage(originalImage, 0, 0, width, height, null);
		g.dispose();
		return resizedImage;
	}

	/**
	 * 
	 * Generate the hashmap that represents the connection between the 2d array
	 * and the images that data refers too
	 * 
	 * @param url
	 */
	private void generateMap(URL url) {
		BufferedImage img = null;
		img = (BufferedImage) eightPuzzleLoader.loadImage(url);
		finishGenerate(img);
	}

	private void finishGenerate(BufferedImage img) {
		imageMap = new HashMap<Integer, Image>();
		img = resizeImage(img, currentPanelSize, currentPanelSize);
		currentImage = copyImg(img);

		Graphics g = img.getGraphics();
		g.setColor(Color.LIGHT_GRAY);
		g.fillRect((numOfTilesOnSide - 1) * sideSize, (numOfTilesOnSide - 1)
				* sideSize, sideSize, sideSize);
		g.dispose();

		imageMap = splitImg(img, numOfTilesOnSide);
	}

	static BufferedImage copyImg(BufferedImage bi) {
		ColorModel cm = bi.getColorModel();
		boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
		WritableRaster raster = bi.copyData(null);
		return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
	}

	public Image getCurrentImage() {
		return currentImage;
	}


	/**
	 * Scramble the gameboard by making 25 random moves
	 */
	public void scramble() {
		Random randomGen = new Random();
		int loopVar = (int) (25 + Math.pow(numOfTilesOnSide, 2));
		for (int i = 0; i < loopVar; i++) {
			Board board = new Board(gameBoard);
			List<Board> boardNeighbors = board.neighbors();
			gameBoard = boardNeighbors.get(
					randomGen.nextInt(boardNeighbors.size())).getTiles();
		}
		repaint();
		if (isSolved()) {
			scramble();
		}
	}

	/**
	 * Find the proper best next move to make
	 */
	public void findNextMove() {
		Solver solver = new Solver(new Board(gameBoard));
		Stack<Board> stack = solver.solution();
		stack.pop();
		if (!stack.isEmpty()) {
			gameBoard = stack.pop().getTiles();
		}
		repaint();
		if (isSolved()) {
			showComplete();
		}
	}

	/**
	 * Check that the puzzle in its "Completed" state
	 * 
	 * @returns true if the puzzle is completed false otherwise
	 */
	public boolean isSolved() {
		Board board = new Board(gameBoard);
		if (board.hamming() == 0) {
			return true;
		}
		return false;
	}

	public double numWrong() {
		return new Board(gameBoard).hamming();
	}
	
	public int getSizeOfPanel() {
		return currentPanelSize;
	}
	
	public void changePic(URL url) {
		generateMap(url);
		repaint();
	}
	
	
	/**
	 * Display a JOptionPane and execute the proper action based on the users
	 * input
	 */
	private void showComplete() {
		int choice = JOptionPane.showConfirmDialog(this,
				"Puzzle complete!\nWould you like to retry the puzzle?",
				"Completed!", JOptionPane.YES_NO_OPTION);
		if (choice == 0) {
			scramble();
		}
		repaint();
	}

	
	/**
	 * Swap a position on the board with the coordinates of the zero value in
	 * the gameboard. All values are allowed but some are ignored.
	 * 
	 * @param x
	 *            the x coordinate of the tile that will be swapped
	 * @param y
	 *            the y coordinate of the tile that will be swapped
	 * @return true if swap succeeded
	 */
	private boolean swap(final int x, final int y) {
		if (!(x >= numOfTilesOnSide || y >= numOfTilesOnSide || x < 0 || y < 0)) {
			Point zeroCoords = getZeroCoords();
			if ((Math.abs(x - zeroCoords.x) == 1 ^ Math.abs(y - zeroCoords.y) == 1)) {
				if (Math.abs(y - zeroCoords.y) == 1
						&& (Math.abs(x - zeroCoords.x) == 0)) {
					swap(x, y, zeroCoords.x, zeroCoords.y);
					return true;
				} else {
					if (Math.abs(x - zeroCoords.x) == 1
							&& (Math.abs(y - zeroCoords.y) == 0)) {
						swap(x, y, zeroCoords.x, zeroCoords.y);
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * 
	 * A helper function that manages 2d array swapping with two arbitrary
	 * points
	 * 
	 * @param x1
	 *            the first x coordinate
	 * @param y1
	 *            the first y coordinate
	 * @param x2
	 *            the second x coordinate
	 * @param y2
	 *            the second y coordinate
	 */
	private void swap(final int x1, final int y1, final int x2, final int y2) {
		int temp = gameBoard[y1][x1];
		gameBoard[y1][x1] = gameBoard[y2][x2];
		gameBoard[y2][x2] = temp;
	}

	/**
	 * get the coordinates of the zero value in the gameBoard
	 * 
	 * @return the coordinates of the zero in the gameBoard, null if it could
	 *         not find the zero
	 */
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

	private class MyMouseListener extends MouseAdapter {
		@Override
		public final void mouseClicked(final MouseEvent e) {
			if (e.getButton() == MouseEvent.BUTTON1) {

				if (swap(e.getX() / sideSize, e.getY() / sideSize)) {
					repaint();
					if (isSolved()) {
						showComplete();
					}
				}
			}
		}
	}
}
