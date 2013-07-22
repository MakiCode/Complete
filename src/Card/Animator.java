package src.Card;

import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

public class Animator {
	
	/**
	 * Get a series of affineTransforms to move a rectangle smoothly from one point ot another
	 * 
	 * @param p1 the point of the first object
	 * @param p2 the point of the second object
	 * @param distanceMultiplier the distance between points on and two
	 * @param frames the number of frames this will go across
	 * @return a list of transformations to apply to the rectangles
	 */
	public static List<AffineTransform> getTransform(Point2D p1, Point2D p2, int distanceMultiplier, int frames) {
		if(frames < 1) {
			throw new IllegalArgumentException("Must have a positive number of frames");
		}
		ArrayList<AffineTransform> list = new ArrayList<>();
		double dX = ((p1.getX() - p2.getX()) * distanceMultiplier)/frames;
		double dY = ((p1.getY() - p2.getY()) * distanceMultiplier)/frames;
		
		for(int i = 0; i < frames; i++) {
			list.add(AffineTransform.getTranslateInstance(dX, dY));			
		}
		return list;
	}
}
