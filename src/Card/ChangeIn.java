package src.Card;

public class ChangeIn {
	private double  dx;
	private double dy;

	/**
	 * Creates a changeIn object. ChangeInObjects are contain two fields: dx and
	 * dy. This class just bundles them together
	 * 
	 * @param dx
	 *            the change in x
	 * @param dy
	 *            the change in y
	 */
	public ChangeIn(double dx, double dy) {
		this.dx = dx;
		this.dy = dy;
	}

	public double getDx() {
		return dx;
	}

	public double getDy() {
		return dy;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(dx);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(dy);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ChangeIn other = (ChangeIn) obj;
		if (Double.doubleToLongBits(dx) != Double.doubleToLongBits(other.dx))
			return false;
		if (Double.doubleToLongBits(dy) != Double.doubleToLongBits(other.dy))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ChangeIn [dx=" + dx + ", dy=" + dy + "]";
	}
}
