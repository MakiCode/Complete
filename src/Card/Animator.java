package src.Card;

import java.awt.geom.Point2D;

public class Animator {

	ChangeIn changeIn;
	/**
	 * Get a series of affineTransforms to move a rectangle smoothly from one
	 * point to another
	 * 
	 * @param p1
	 *            the point of the first object
	 * @param p2
	 *            the point of the second object
	 * @param distanceMultiplier
	 *            the distance between points one and two
	 * @param frames
	 *            the number of frames this will go across
	 * @return a list of transformations to apply to the rectangles
	 */
	public Animator(Point2D p1, Point2D p2, int distanceMultiplier, int frames) {
		if (frames < 1) {
			throw new IllegalArgumentException(
					"Must have a positive number of frames");
		}

		double dX = ((p1.getX() - p2.getX()) * distanceMultiplier) / frames;
		double dY = ((p1.getY() - p2.getY()) * distanceMultiplier) / frames;
		changeIn = new ChangeIn(dX, dY);
	}

	public ChangeIn getNextTransform() {
		return changeIn;
	}

}
