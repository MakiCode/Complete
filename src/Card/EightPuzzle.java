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
	private boolean challenge;
	private int score;

	// Vars pertainging to animation
	// The reason all of these variables are at the top level is that
	// They will be accessed by multiple threads and methods, TODO: 
	// Try to find a way to not have these so high level 
	private boolean animating;
	private Point toMove;
	private Point toMoveTo;
	private Rectangle rect;
	private Animator animator;
	private int numOfFrames = 10; // at a sleep of 16.667 this will take about a
	private int amountToSleep = 17;
	private boolean scrambling;
	
	/**
	 * 
	 * Method     : animate
	 *
	 * Purpose    : TODO
	 *
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 *
	 *
	 */
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
				resetAnimatingVars();
				repaint();
			}

		});
		thread.start();

	}

	/**
	 *	Used for animating, resets the values to default
	 */
	private void resetAnimatingVars() {
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
		sideSize = currentPanelSize / numOfTilesOnSide;
		if (gameBoard == null) {
			makeSolved();
		}
		addMouseListener(new MyMouseListener());
		repaint();
	}

	/**
	 * Recreate the gameBoard with the specified dimensions and set it to the solved state 
	 * 
	 * @param numOfTilesOnSide the dimensions of the gameBoard
	 *
	 */
	private void makeSolved() {
		gameBoard = new int[numOfTilesOnSide][numOfTilesOnSide];
		int n = 1;
		for (int i = 0; i < numOfTilesOnSide; i++) {
			for (int j = 0; j < numOfTilesOnSide; j++) {
				if (i == (numOfTilesOnSide - 1)
						&& j == (numOfTilesOnSide - 1)) {
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

	/**
	 * resize the game
	 * 
	 * @param frameSize resizes the game to be square and have side length = framesize
	 *
	 */
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
	
	/**
	 * 
	 * Recreates the ImageMap without loading a new image 
	 *
	 */
	private void regenerateMap() {
		finishGenerate((BufferedImage) currentImage);
	}

	
	/** (non-Javadoc)
	 * @see javax.swing.JComponent#paint(java.awt.Graphics)
	 */
	@Override
	public final void paint(final Graphics g) {
		bufferImage = createImage(currentPanelSize, currentPanelSize);
		Image image = buffer(bufferImage);
		g.drawImage(image, 0, 0, null);
	}

	/**
	 * 
	 * A buffer for the paint method, this does all of the pre-rendering so that the painting is smooth
	 *
	 * @param img the buffer to paint to
	 * @return the image passed to us but it has been repainted with our buffer
	 *
	 *
	 */
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
						if (checkIfSolved() && !scrambling) {
							g.drawImage(imageMap.get(gameBoard[rows][columns]),
									x, y, null);
						}
					}
				}
				if (!checkIfSolved() || scrambling) {
					g2d.setColor(Color.BLACK);
					g2d.drawRect(x, y, sideSize, sideSize);
				}
			}
		}
		return img;
	}

	/**
	 * Clears the screen of any lingering colors
	 *  
	 * @param g the graphics context to clear
	 *
	 */
	private void clearScreen(final Graphics g) {
		g.setColor(Color.LIGHT_GRAY);
		g.fillRect(0, 0, getWidth(), getHeight());
	}

	/**
	 * Animates a single tile of the image map ODD CODING HERE MUST INVESTIGATE
	 * @param g 
	 * @param rows
	 * @param columns
	 *
	 */
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
	 * @param url the URL to load the image from
	 */
	private void generateMap(URL url) {
		BufferedImage img = (BufferedImage) eightPuzzleLoader.loadImage(url);
		finishGenerate(img);
	}

	/**
	 * The final step when generating an imageMap, splits the image up into pieces
	 * and populates the imageMap  
	 */
	private void finishGenerate(BufferedImage img) {
		currentImage = img;
		imageMap = new HashMap<Integer, Image>();
		img = resizeImage(img, currentPanelSize, currentPanelSize);
		imageMap = splitImg(img, numOfTilesOnSide);
	}

	/**
	 * Create a new instance of a buffered image that is a copy of the parameter 
	 * @param bi the image to copy
	 * @return a new instance of the buffered image
	 *
	 */
	static BufferedImage copyImg(BufferedImage bi) {
		ColorModel cm = bi.getColorModel();
		boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
		WritableRaster raster = bi.copyData(null);
		return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
	}

	/**
	 * Get the current image on the board
	 */
	public Image getCurrentImage() {
		return currentImage;
	}

	/**
	 * Split up the image into tiles numbered 1 to (numOfTilesOnSide^2 - 1) with
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
	 * Scramble the gameboard and animate it. Continue until the user tells us to stop
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
							e.printStackTrace();
						}
					}
					previousBoard = board;
				}
				numOfFrames = 10;
				repaint();
				if (checkIfSolved()) {
					scramble();
				}
			}

		}).start();
	}

	/**
	 * Used for scrambling, it is supposed to ensure that the next
	 * board chosen from the list does not match the board that is 
	 * passed as a second argument
	 *  
	 * @param boards The possible boards to generate
	 * @param previousBoard the board that we will ignore
	 * @return a board, chosen at random, from the list that is ensured 
	 * not to match the second argument
	 *
	 */
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
			checkIfSolved();
		}
	}

	/**
	 * Check that the puzzle in its "Completed" state
	 * 
	 * @returns true if the puzzle is completed false otherwise
	 */
	public boolean checkIfSolved() {
		Board board = new Board(gameBoard);
		if (board.hamming() == 0) {
			fireSolvedEvents();
			return true;
		}
		return false;
	}


	/**
	 * Fires the isSolved events for the listeners
	 */
	private void fireSolvedEvents()
	{
		for (IsSolvedListener isSolvedListener : isSolvedListeners)
		{
			isSolvedListener.handleIsSolvedEvent();
		}
		
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


	/**
	 * Get the number of tiles that are out of place
	 */
	public double numWrong() {
		return new Board(gameBoard).hamming();
	}

	/**
	 * Get the current size of the panel
	 */
	public int getSizeOfPanel() {
		return currentPanelSize;
	}

	/**
	 * Change the picture currently on the map, does not reset progress
	 * @param url
	 *
	 */
	public void changePic(URL url) {
		generateMap(url);
		repaint();
	}

	/**
	 * A basic Scramble that has no animation so 
	 * that the user can't see what is going on,
	 * also sets puzzle to challenge mode
	 */
	public void challengeScramble() {
		challenge = true;
		while (new Board(gameBoard).hamming() != (numOfTilesOnSide * numOfTilesOnSide) - 1) {
			Board previousBoard = new Board(gameBoard);
			List<Board> boardNeighbors = previousBoard.neighbors();
			gameBoard = getNewBoard(boardNeighbors, previousBoard);
		}
		repaint();
	}
	
	
	/**
	 * makes the puzzle into it's solved state 
	 */
	public void setToSolved() {
		makeSolved();
		repaint();
	}

	/**
	 * Sets the challenge to false and resets score
	 */
	public void stopChallenge() {
		challenge = false;
		score = 0;
	}

	/**
	 * Get the current score (Number of clicks user has made if during challenge, 0 otherwise)
	 */
	public int getScore() {
		return score;
	}

	/**
	 * Adds IsSolvedListeners to the puzzle
	 */
	public void addIsSolvedListener(IsSolvedListener listener) {
		isSolvedListeners.add(listener);
	}
	
	private class MyMouseListener extends MouseAdapter {
		@Override
		public final void mouseClicked(final MouseEvent e) {
			if (e.getButton() == MouseEvent.BUTTON1) {
				
				if (swap(e.getX() / sideSize, e.getY() / sideSize)) {
					if (challenge) {
						score++;
					}
					checkIfSolved();//Need to change this...
				}
			}
			
		}
	}
	
}
