package src.Card; 
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.net.URL;
import java.util.ArrayList;
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
	// TODO Make a isSolved listener for EightPuzzleMain
	//need a addIsSolvedListener a fire isSolvedEvent method
	
	private List<IsSolvedListener> isSolvedListeners = new ArrayList<IsSolvedListener>();
	private int currentPanelSize;
	private int sideSize;
	private Map<Integer, Image> imageMap;
	private int[][] gameBoard;
	private int numOfTilesOnSide;
	private Image currentImage;
	private Image bufferImage;
	private EightPuzzleLoader eightPuzzleLoader;

	// Vars pertainging to animation
	private boolean animating;
	private Point toMove;
	private Point toMoveTo;
	private Rectangle rect;
	private Animator animator;
	private int numOfFrames = 10; // at a sleep of 16.667 this will take about a
	private int amountToSleep = 17;
	private boolean scrambling;
	private boolean challenge;
	private int score;
	
	private void animate(int x1, int y1, int x2, int y2) {
		animating = true;
		toMove = new Point(x2, y2);
		toMoveTo = new Point(x1, y1);
		rect = new Rectangle(x1 * sideSize, y1 * sideSize, sideSize, sideSize);
		animator = new Animator(toMove, toMoveTo, sideSize, numOfFrames);
		Thread thread = new Thread(new Runnable() {

			@Override
			public void run() {
				for (int i = 0; i < numOfFrames; i++) {
					repaint();
					try {
						Thread.sleep(amountToSleep);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				deleteVars();
				repaint();
			}

		});
		thread.start();

	}

	private void deleteVars() {
		toMove = null;
		toMoveTo = null;
		animating = false;
		rect = null;
		animator = null;
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
		setPreferredSize(new Dimension(frameSize, frameSize));
		makeVars(frameSize, numOfTilesOnSideVal);
		addMouseListener(new MyMouseListener());
		eightPuzzleLoader = eightPuzzleLoader2;
	}

	public void createGame() {
		generateMap(eightPuzzleLoader.getDefaultURL());
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
			makeSolved(numOfTilesOnSideVal);
		}
		addMouseListener(new MyMouseListener());
		repaint();
	}

	private void makeSolved(int numOfTilesOnSideVal) {
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

	/**
	 * Resizes the dimensions of the game
	 * 
	 * @param numOfTilesOnSideVal
	 *            The number of tiles on a single side of the puzzle
	 */
	public void resizeGameDimensions(final int frameSize,
			int numOfTilesOnSideVal) {
		gameBoard = null;
		makeVars(frameSize, numOfTilesOnSideVal);
		generateMap(eightPuzzleLoader.getLastUrl());
		repaint();
	}

	public void resizeGameFrame(int frameSize) {
		setSize(new Dimension(frameSize, frameSize));
		if(bufferImage == null) {
			bufferImage = createImage(currentPanelSize, currentPanelSize);
		}
		clearScreen(bufferImage.getGraphics());
		makeVars(frameSize, numOfTilesOnSide);
		regenerateMap();
		repaint();
	}

	private void regenerateMap() {
		finishGenerate((BufferedImage) currentImage);
	}

	@Override
	public final void paint(final Graphics g) {
		bufferImage = createImage(currentPanelSize, currentPanelSize);
		Image image = buffer(bufferImage);
		g.drawImage(image, 0, 0, null);
	}

	private Image buffer(final Image img) {
		Graphics g = img.getGraphics();
		Graphics2D g2d = (Graphics2D) g;
		clearScreen(g);
		for (int rows = 0; rows < gameBoard.length; rows++) {
			for (int columns = 0; columns < gameBoard[rows].length; columns++) {
				int x = columns * sideSize;
				int y = rows * sideSize;
				if (animating && new Point(columns, rows).equals(toMove)) {
					animateBlock(g, rows, columns);

				} else {
					if (gameBoard[rows][columns] != 0) {
						g.drawImage(imageMap.get(gameBoard[rows][columns]), x,
								y, null);
					} else {
						if (isSolved() && !scrambling) {
							g.drawImage(imageMap.get(gameBoard[rows][columns]),
									x, y, null);
						}
					}
				}
				if (!isSolved() || scrambling) {
					g2d.setColor(Color.BLACK);
					g2d.drawRect(x, y, sideSize, sideSize);
				}
			}
		}
		return img;
	}

	private void clearScreen(final Graphics g) {
		g.setColor(Color.LIGHT_GRAY);
		g.fillRect(0, 0, getWidth(), getHeight());
	}

	private void animateBlock(final Graphics g, int rows, int columns) {
		g.drawImage(imageMap.get(gameBoard[rows][columns]), (int) rect.getX(),
				(int) rect.getY(), null);

		ChangeIn changeIn = animator.getNextTransform();

		rect.setRect(rect.getX() + changeIn.getDx(),
				rect.getY() + changeIn.getDy(), sideSize, sideSize);
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
		 BufferedImage img = (BufferedImage) eightPuzzleLoader.loadImage(url);
		finishGenerate(img);
	}

	private void finishGenerate(BufferedImage img) {
		currentImage = img;
		imageMap = new HashMap<Integer, Image>();
		img = resizeImage(img, currentPanelSize, currentPanelSize);
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
		Map<Integer, Image> map = new HashMap<Integer, Image>();

		int n = 1;
		for (int i = 0; i < numOfTilesOnSide; i++) {
			for (int j = 0; j < numOfTilesOnSide; j++) {
				if (i == (numOfTilesOnSide - 1) && j == (numOfTilesOnSide - 1)) {
					map.put(0, img.getSubimage(j * sideSize, i * sideSize,
							sideSize, sideSize));
				} else {
					map.put(n, img.getSubimage(j * sideSize, i * sideSize,
							sideSize, sideSize));
				}
				n++;
			}
		}
		return map;
	}

	/**
	 * Scramble the gameboard by making 25 random moves
	 */
	public void scramble() {
		scrambling = true;
		numOfFrames = 5;
		new Thread(new Runnable() {

			@Override
			public void run() {
				Board previousBoard = null;
				while (scrambling) {
					Board board = new Board(gameBoard);
					List<Board> boardNeighbors = board.neighbors();
					int[][] nextBoard = getNewBoard(boardNeighbors,
							previousBoard);
					Point p = getZeroCoords(nextBoard);
					swap(p.x, p.y);
					while (animating) {
						try {
							Thread.sleep(amountToSleep);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					previousBoard = new Board(nextBoard);
				}
				numOfFrames = 10;
				repaint();
				if (isSolved()) {
					scramble();
				}
			}

		}).start();
	}

	private static int[][] getNewBoard(List<Board> boards, Board previousBoard) {
		Random randomGen = new Random();
		Board currentBoard = null;
		while (true) {
			currentBoard = boards.get(randomGen.nextInt(boards.size()));
			if (!currentBoard.equals(previousBoard)) {
				break;
			}
		}
		return currentBoard.getTiles();
	}

	public void stopScramble() {
		scrambling = false;
	}

	/**
	 * Find the proper best next move to make
	 */
	public void findNextMove() {
		if (!scrambling) {
			Solver solver = new Solver(new Board(gameBoard));
			Stack<Board> stack = solver.solution();
			stack.pop();
			int[][] nextGameboard = null;
			if (!stack.isEmpty()) {
				nextGameboard = stack.pop().getTiles();
				Point p = getZeroCoords(nextGameboard);
				swap(p.x, p.y);
			}
			repaint();
			if (isSolved()) {
				showComplete();
			}
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
			Point zeroCoords = getZeroCoords(gameBoard);
			if ((Math.abs(x - zeroCoords.x) == 1 ^ Math.abs(y - zeroCoords.y) == 1)) {
				if (Math.abs(y - zeroCoords.y) == 1
						&& (Math.abs(x - zeroCoords.x) == 0)) {
					animate(x, y, zeroCoords.x, zeroCoords.y);
					swap(x, y, zeroCoords.x, zeroCoords.y);
					return true;
				} else {
					if (Math.abs(x - zeroCoords.x) == 1
							&& (Math.abs(y - zeroCoords.y) == 0)) {
						animate(x, y, zeroCoords.x, zeroCoords.y);
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
	 * @param is
	 * 
	 * @return the coordinates of the zero in the gameBoard, null if it could
	 *         not find the zero
	 */
	private static Point getZeroCoords(int[][] is) {
		for (int x = 0; x < is.length; x++) {
			for (int y = 0; y < is[x].length; y++) {
				if (is[y][x] == 0) {
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
					if (challenge) {
						score++;
					}
					if (isSolved()) {
						showComplete();
					}
				}
			}

		}
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

	public void challengeScramble() {
		challenge = true;
		while (new Board(gameBoard).hamming() != (numOfTilesOnSide * numOfTilesOnSide) - 1) {
			Board previousBoard = new Board(gameBoard);
			List<Board> boardNeighbors = previousBoard.neighbors();
			gameBoard = getNewBoard(boardNeighbors, previousBoard);
		}
		repaint();
	}
	
	

	public void setToSolved() {
		makeSolved(numOfTilesOnSide);
		repaint();
	}

	public void stopChallenge() {
		challenge = false;
		score = 0;
	}

	public int getScore() {
		return score;
	}

	public int sideSize() {
		return imageMap.get(0).getHeight(null);
	}
}
