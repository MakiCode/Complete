package src.Card;

import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.List;

import org.junit.*;

public class AnimatorTest {

	@Test
	public void testAnimator() {
		List<AffineTransform> result = Animator.getTransform(new Point(1, 1),
				new Point(1, 2), 150, 30);
		List<AffineTransform> expected = new ArrayList<>();
		for (int i = 0; i < 30; i++) {
			expected.add(AffineTransform.getTranslateInstance(0, -5));
		}
		Assert.assertTrue(expected.equals(result));
	}

}
