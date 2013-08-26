package src.Card;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.filechooser.FileFilter;

//Ideas to add: A challenge mode where the UI is changed so that you can only scramble 
//and change the pictures (or switch back to normal mode)
//3 levels of difficulty normal: = 3X3 medium: 4X4 hard: 5X5 use
//if(board.hamming() = 0){scramble()} to ensure that everything is difficult very difficult

//Game keeps highscores (in .EightPuzzle/highscores.txt) and after victory displays top 3 high
//scores in a popup

public class EightPuzzleMain extends JFrame {

	// AddPuzzleListener removePuzzleSolvedListener make Interface
	// PuzzleSolvedListener firePuzzleSolvedEvent (iterates over all listeners
	// invoking interface method)

	private static final String SET_TO_CHALLENGE_MODE = "Set to challenge mode";
	// TODO Add completion testing for challenge. Stop making it the
	// EightPuzzles job to display completion

	private int frameWidth = 450;
	private int frameHeight = 535;
	private int numOfTilesOnSide = 3;
	private int minHeight = 42;
	private EightPuzzle puzzle;
	private JPanel buttonPanel;
	private JMenuBar menubar;
	private JCheckBoxMenuItem showWarning;
	private EightPuzzleLoader eightPuzzleLoader;
	private JCheckBoxMenuItem showOpeningMessage;
	private JButton scrambleButton;
	private HighScore highScores;

	private JButton nextButton;
	private JMenuItem changeNumOfTiles;
	private JMenuItem setChallengMode;

	public static void main(final String[] args) {

		EightPuzzleMain epm = new EightPuzzleMain();
		epm.go();
	}

	private void go() {
		eightPuzzleLoader = new EightPuzzleLoader();

		highScores = new HighScore();

		setSize(frameWidth, frameHeight);
		puzzle = new EightPuzzle(frameWidth, numOfTilesOnSide,
				eightPuzzleLoader);
		add(puzzle, BorderLayout.CENTER);

		puzzle.createGame();

		setLocation(EightPuzzle.getCenterPosition(getSize(), Toolkit
				.getDefaultToolkit().getScreenSize()));

		makeButtons();
		makeJMenuBar();

		add(buttonPanel, BorderLayout.SOUTH);
		setJMenuBar(menubar);

		addComponentListener(new ResizeListener());

		if (showOpeningMessage.isSelected()) {
			Object[] options = { "Okay!", "Don't Show this again" };
			int n = JOptionPane
					.showOptionDialog(
							this,
							"Thank you for playing my game!\n"
									+ "This is my version of the classic Sliding Blocks puzzle.\n"
									+ "To play, just click on the blocks to slide them into the\n"
									+ "gray space. You can change the dimensions, image, and\n"
									+ "the default image from the menu named \"puzzle\" at the\n"
									+ "top of the screen. You can also have the computer decide\n"
									+ "the next move to make by using the \"Next Move\" button below.\n"
									+ "If you are fed up with your current game you can scramble\n"
									+ "it again with the \"Scramble\" button.\n\n"
									+ "Just a small note, The language I wrote this in is slightly\n"
									+ "innaccurate when it comes to mouse handling. If you can't\n"
									+ "move a block just wait an instant and then click it again!",
							"Welcome!", JOptionPane.YES_NO_OPTION,
							JOptionPane.INFORMATION_MESSAGE, null, options,
							options[0]);
			if (n == 1) {
				showOpeningMessage.setSelected(false);
				;
			}
		}

		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {

			@Override
			public void run() {
				EightPuzzleLoader.writeMetaData(showWarning.isSelected(),
						showOpeningMessage.isSelected());
				highScores.write();
			}
		}));
		setVisible(true);
	}

	private void makeJMenuBar() {
		menubar = new JMenuBar();

		JMenu menu = new JMenu("Puzzle");

		changeNumOfTiles = new JMenuItem(
				"Change puzzle dimensions (3X3, 4X4, etc.)");
		menu.add(changeNumOfTiles);
		changeNumOfTiles.addActionListener(new ChangeDimensionListener());

		JMenuItem revertPic = new JMenuItem("Revert to default picture");
		menu.add(revertPic);
		revertPic.addActionListener(new revertListener());

		JMenuItem savePic = new JMenuItem("Make current picture default ");
		menu.add(savePic);
		savePic.addActionListener(new saveListener());

		JMenuItem changePic = new JMenuItem("Change picture...");
		menu.add(changePic);
		changePic.addActionListener(new ChangePicListener());

		showWarning = new JCheckBoxMenuItem("Show warning when scrambling");
		menu.add(showWarning);
		showWarning.setSelected(eightPuzzleLoader.showWarning());

		showOpeningMessage = new JCheckBoxMenuItem(
				"Show welcome message on startup");
		menu.add(showOpeningMessage);
		showOpeningMessage.setSelected(eightPuzzleLoader.showOpeningMessage());

		setChallengMode = new JMenuItem(SET_TO_CHALLENGE_MODE);
		menu.add(setChallengMode);
		setChallengMode.addActionListener(new SetModeListener());

		menubar.add(menu);
	}

	private void makeButtons() {
		buttonPanel = new JPanel();

		nextButton = new JButton("Next Move");
		nextButton.setToolTipText("Have the computer choose the next move");
		nextButton.addActionListener(new NextListener());

		scrambleButton = new JButton("Scramble");
		scrambleButton.setToolTipText("Scramble the board");
		scrambleButton.addActionListener(new ScrambleListener());

		buttonPanel.add(nextButton);
		buttonPanel.add(scrambleButton);
	}

	private class SetModeListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (setChallengMode.getText().equals(SET_TO_CHALLENGE_MODE)) {
				makeChallengeUI();
			} else {
				makeNormalUI();
			}
		}
	}

	private class revertListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			puzzle.changePic(eightPuzzleLoader.getDefaultURL());
		}

	}

	private void makeChallengeUI() {
		scrambleButton.setEnabled(false);
		nextButton.setEnabled(false);
		changeNumOfTiles.setEnabled(false);
		setChallengMode.setText("Set to normal mode");
		puzzle.challengeScramble();
	}

	private void makeNormalUI() {
		scrambleButton.setEnabled(true);
		nextButton.setEnabled(true);
		changeNumOfTiles.setEnabled(true);
		setChallengMode.setText(SET_TO_CHALLENGE_MODE);
		puzzle.stopChallenge();
		puzzle.setToSolved();
	}

	private class saveListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			eightPuzzleLoader.saveImageLastImg();
			JOptionPane.showMessageDialog(EightPuzzleMain.this,
					"This image is now to the default image.", "Success!",
					JOptionPane.DEFAULT_OPTION);

		}

	}

	private class ChangePicListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			JFileChooser fc = new JFileChooser();
			fc.setFileFilter(new ImageFilter());
			int returnVal = fc.showOpenDialog(EightPuzzleMain.this);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				URL file = null;
				try {
					file = fc.getSelectedFile().toURI().toURL();
				} catch (MalformedURLException e1) {
					e1.printStackTrace();
				}
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
				if (extension.equals("png") || extension.equals("gif")
						|| extension.equals("jpg")) {
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

	private class ChangeDimensionListener implements ActionListener {

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
								"Too small!", JOptionPane.DEFAULT_OPTION);
					} else if (intInput > 100) {
						JOptionPane
								.showMessageDialog(
										EightPuzzleMain.this,
										"Sorry! Dimensions larger than 100 are too large",
										"Too large!",
										JOptionPane.DEFAULT_OPTION);
					} else {
						numOfTilesOnSide = intInput;
						puzzle.resizeGameDimensions(getWidth(), intInput);
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

		public boolean isInteger(String str) {
			try {
				Integer.parseInt(str);
				return true;
			} catch (NumberFormatException nfe) {
			}
			return false;
		}
	}

	private class ResizeListener extends ComponentAdapter {

		@Override
		public void componentResized(ComponentEvent e) {
			//if (frameHeight - puzzle.getHeight() < minHeight) {
				Component com = (Component) e.getSource();
				puzzle.resizeGameFrame(com.getWidth());
				buttonPanel.setPreferredSize(new Dimension(com.getWidth(), minHeight));
			//}
		}
	}

	private class ScrambleListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (showWarning.isSelected()
					&& scrambleButton.getText().equals("Scramble")
					&& !puzzle.isSolved()) {
				Object[] options = { "Yes", "No", "Yes/Don't show again" };
				int n = JOptionPane.showOptionDialog(EightPuzzleMain.this,
						"Warning! This will reset your progress.", "Positive?",
						JOptionPane.YES_NO_CANCEL_OPTION,
						JOptionPane.INFORMATION_MESSAGE, null, options,
						options[0]);
				switch (n) {
				case 0:
					scramblePuzzle();
					break;
				case 2:
					showWarning.setSelected(false);
					scramblePuzzle();
					break;
				}
			} else {
				scramblePuzzle();
			}
		}

		private void scramblePuzzle() {
			if (scrambleButton.getText().equals("Scramble")) {
				puzzle.scramble();
				scrambleButton.setText("    Stop    "); // Add spaces to make it
														// the same size as the
														// other buttons
			} else {
				puzzle.stopScramble();
				scrambleButton.setText("Scramble");
			}
		}
	}

	private class NextListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			boolean doNextMove = true;
			if (numOfTilesOnSide >= 5) {
				if (puzzle.numWrong() > (double) Math.pow(numOfTilesOnSide, 2)
						* (1d / 2d)
						|| numOfTilesOnSide >= 10) {
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