package src.Card;

import static org.junit.Assert.*;

import java.awt.Dimension;
import java.awt.Point;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import javax.swing.JFrame;

import org.junit.Test;

public class EightPuzzleTest {

	@Test
	public void testGetFramePosition() {
		JFrame frame = new JFrame();
		frame.setSize(10, 10);
		Dimension dim = new Dimension(20, 20);
		Point expected = new Point(
				(int) (dim.getWidth() / 2 - frame.getWidth() / 2),
				(int) (dim.getHeight() / 2 - frame.getHeight() / 2));
		Point result = EightPuzzle.getCenterPosition(frame.getSize(), dim);
		assertTrue(expected.equals(result));
	}

	@Test(expected = ArithmeticException.class)
	public void testFramePosLargeFrame() {
		Dimension frame = new Dimension(30, 30);
		Dimension dim = new Dimension(20, 20);
		Point expected = new Point(
				(int) (dim.getWidth() / 2 - frame.getWidth() / 2),
				(int) (dim.getHeight() / 2 - frame.getHeight() / 2));
		Point result = EightPuzzle.getCenterPosition(frame, dim);
		assertTrue(expected.equals(result));
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

	@Test
	public void getNewBoard() {
		List<Board> boards = new Board(new int[][] { { 1, 2, 3 }, { 4, 0, 5 },
				{ 6, 7, 8 } }).neighbors();
		Board previousBoard = new Board(new int[][] { { 1, 0, 3 }, { 4, 2, 5 },
				{ 6, 7, 8 } });
		for (int i = 0; i < 100000; i++) {
			assertTrue(!Arrays.equals(previousBoard.getTiles(),
					getNewBoard(boards, previousBoard)));
		}
	}
}
