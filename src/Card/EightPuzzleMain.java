package src.Card;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.filechooser.FileFilter;

public class EightPuzzleMain extends JFrame {

	// TODO: Make a save current image as default menu option
	private int frameWidth = 450;
	private int frameHeight = 525;
	private int numOfTilesOnSide = 3;
	private EightPuzzle puzzle;
	private JPanel buttonPanel;
	private JMenuBar menubar;
	private String defaultFileName = "image.png";
	private String defaultFile;

	public static void main(final String[] args) {
		EightPuzzleMain epm = new EightPuzzleMain();
		epm.go();
	}

	private void go() {
		setSize(frameWidth, frameHeight);
		puzzle = new EightPuzzle(frameWidth, numOfTilesOnSide);
		add(puzzle, BorderLayout.CENTER);

		defaultFile = getClass().getResource(defaultFileName).getFile();

		puzzle.createGame(defaultFileName);

		setLocation(EightPuzzle.getCenterPosition(getSize(), Toolkit
				.getDefaultToolkit().getScreenSize()));

		makeButtons();
		makeJMenuBar();

		add(buttonPanel, BorderLayout.SOUTH);
		setJMenuBar(menubar);

		addComponentListener(new ResizeListener());
		setVisible(true);

	}

	private void makeJMenuBar() {
		menubar = new JMenuBar();

		JMenu menu = new JMenu("Puzzle");
		JMenuItem changeNumOfTiles = new JMenuItem(
				"Change puzzle dimensions (3X3, 4X4, etc.)");
		menu.add(changeNumOfTiles);
		changeNumOfTiles.addActionListener(new ChangeNumListener());

		JMenuItem savePic = new JMenuItem("Make default picture");
		menu.add(savePic);
		savePic.addActionListener(new saveListener());

		JMenuItem changePic = new JMenuItem("Change picture...");
		menu.add(changePic);
		changePic.addActionListener(new ChangePicListener());

		menubar.add(menu);
	}

	private void makeButtons() {
		buttonPanel = new JPanel();

		JButton nextButton = new JButton("Next Move");
		nextButton.setToolTipText("Have the computer choose the next move");
		nextButton.addActionListener(new NextListener());

		JButton scrambleButton = new JButton("Scramble");
		scrambleButton.setToolTipText("Scramble the board");
		scrambleButton.addActionListener(new ScrambleListener());

		buttonPanel.add(nextButton);
		buttonPanel.add(scrambleButton);
	}

	private class saveListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			Image image = puzzle.getCurrentImage();
			try {
				ImageIO.write((BufferedImage) image, "png", new File(
						defaultFile));
			} catch (IOException e1) {
				JOptionPane.showMessageDialog(EightPuzzleMain.this,
						"Failed to write to file, Error ID;" + e.getID(), "Failed to write", JOptionPane.ERROR_MESSAGE);
			}
		}

	}

	// /**
	// *
	// * @param in
	// * @return A buffered image
	// *
	// * @author https://forums.oracle.com/thread/1290824 Response 2
	// */
	// BufferedImage getRenderedImage(Image in) {
	// int w = in.getWidth(null);
	// int h = in.getHeight(null);
	// int type = BufferedImage.TYPE_INT_RGB;
	// BufferedImage out = new BufferedImage(w, h, type);
	// Graphics2D g2 = out.createGraphics();
	// out.drawImage(in, 0, 0, null);
	// g2.dispose();
	// return out;
	// }

	private class ChangePicListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			JFileChooser fc = new JFileChooser();
			fc.setFileFilter(new ImageFilter());
			int returnVal = fc.showOpenDialog(EightPuzzleMain.this);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				File file = fc.getSelectedFile();
				puzzle.changePic(file);
			}
		}
	}

	/**
	 * I took some of the classes in the JFileChooser and rearranged them
	 * 
	 * @author Java JFileChooser Tutorial
	 * 
	 */
	private class ImageFilter extends FileFilter {

		public final static String png = "png";

		/**
		 * Get the extension of a file.
		 * 
		 * @param f
		 *            the file to get the extension from
		 */
		private String getExtension(File f) {
			String ext = null;
			String s = f.getName();
			int i = s.lastIndexOf('.');

			if (i > 0 && i < s.length() - 1) {
				ext = s.substring(i + 1).toLowerCase();
			}
			return ext;
		}

		// Accept all directories and all gif, jpg, tiff, or png files.
		public boolean accept(File f) {
			if (f.isDirectory()) {
				return true;
			}
			String extension = getExtension(f);
			if (extension != null) {
				if (extension.equals(png)) {
					return true;
				} else {
					return false;
				}
			}

			return false;
		}

		@Override
		public String getDescription() {
			return "Only images";
		}
	}

	// The description of this filter
	public String getDescription() {
		return "Just Images";
	}

	private class ChangeNumListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			while (true) {
				String input = JOptionPane
						.showInputDialog("Type in the new Dimension of the puzzle. The puzzle\n"
								+ "must be square so please enter one number that\n"
								+ "is greater than one.\n"
								+ "WARNING: This will reset your progress.");

				if (isInteger(input)) {
					int intInput = Integer.parseInt(input);
					if (intInput <= 1) {
						JOptionPane.showMessageDialog(EightPuzzleMain.this,
								"Please enter a number greater than one.",
								"Too small!", JOptionPane.INFORMATION_MESSAGE);
					} else {
						numOfTilesOnSide = intInput;
						puzzle.resizeGame(getWidth(), intInput);
						break;
					}
				} else if (input == null) {
					break;
				} else {
					JOptionPane.showMessageDialog(EightPuzzleMain.this,
							"You didn't enter a number!", "Incorrect input",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		}
	}

	public boolean isInteger(String str) {
		try {
			Integer.parseInt(str);
			return true;
		} catch (NumberFormatException nfe) {
		}
		return false;
	}

	private class ResizeListener extends ComponentAdapter {

		@Override
		public void componentResized(ComponentEvent e) {
			Component com = (Component) e.getSource();
			puzzle.makeVars(com.getWidth(), numOfTilesOnSide);
		}
	}

	private class ScrambleListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			puzzle.scramble();
		}

	}

	private class NextListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			boolean doNextMove = true;
			if (numOfTilesOnSide >= 5) {
				if (puzzle.numWrong() > (double) Math.pow(numOfTilesOnSide, 2)
						* (2d / 3d)
						|| numOfTilesOnSide > 10) {
					int choice = JOptionPane
							.showConfirmDialog(
									EightPuzzleMain.this,
									"WARNING! This puzzle is very large and very scrambled!\n"
											+ "It might take sometime to figure out the right answer.\n"
											+ "Do you want to continue?",
									"Too many scrambled!",
									JOptionPane.YES_NO_OPTION);
					if (choice != 0) {
						doNextMove = false;
					}
				}
			}
			if (doNextMove) {
				puzzle.findNextMove();
			}
		}

	}
}