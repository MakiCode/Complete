package src.dadsCard;

import static org.junit.Assert.*;

import java.awt.Dimension;
import java.awt.Point;

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
	
	@Test(expected=ArithmeticException.class)
	public void testFramePosLargeFrame() {
		Dimension frame = new Dimension(30,30);
		Dimension dim = new Dimension(20, 20);
		Point expected = new Point(
				(int) (dim.getWidth() / 2 - frame.getWidth() / 2),
				(int) (dim.getHeight() / 2 - frame.getHeight() / 2));
		Point result = EightPuzzle.getCenterPosition(frame, dim);
		assertTrue(expected.equals(result));
	}

}
