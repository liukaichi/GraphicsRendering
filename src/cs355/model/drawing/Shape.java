package cs355.model.drawing;

import java.awt.Color;
import java.awt.geom.Point2D;

/**
 * This is the base class for all of your shapes.
 * Make sure they all extend this class.
 */
public abstract class Shape {

	// The color of this shape.
	protected Color color;

	// The center of this shape.
	protected Point2D.Double center;

	// The rotation of this shape.
	protected double rotation;


	/**
	 * Basic constructor that sets fields.
	 * It initializes rotation to 0.
	 * @param color the color for the new shape.
	 * @param center the center point of the new shape.
	 */
	public Shape(Color color, Point2D.Double center) {
		this.color = color;
		this.center = center;
		rotation = 0.0;
	}
	public Shape(java.awt.Shape drawingShape, double rotation){
		color = null;
		center = new Point2D.Double(drawingShape.getBounds2D().getCenterX(), drawingShape.getBounds2D().getCenterY());
		this.rotation = rotation;
	}

	/**
	 * Getter for this shape's color.
	 * @return the color of this shape.
	 */
	public Color getColor() {
		return color;
	}

	/**
	 * Setter for this shape's color
	 * @param color the new color for the shape.
	 */
	public void setColor(Color color) {
		this.color = color;
	}

	/**
	 * Getter for this shape's center.
	 * @return this shape's center as a Java point.
	 */
	public Point2D.Double getCenter() {
		return center;
	}

	/**
	 * Setter for this shape's center.
	 * @param center the new center as a Java point.
	 */
	public void setCenter(Point2D.Double center) {
		this.center = center;
	}

	public abstract void updatePosition(double changeX, double changeY);
	/**
	 * Getter for this shape's rotation.
	 * @return the rotation as a double.
	 */
	public double getRotation() {
		return rotation;
	}

	/**
	 * Setter for this shape's rotation.
	 * @param rotation the new rotation.
	 */
	public void setRotation(double rotation) {
		this.rotation = rotation;
	}

	/**
	 * Used to test for whether the user clicked inside a shape or not.
	 * @param pt = the point to test whether it's in the shape or not.
	 * @param tolerance = the tolerance for testing. Mostly used for lines.
	 * @return true if pt is in the shape, false otherwise.
	 */

	public abstract boolean pointInShape(Point2D.Double pt, double tolerance);

	public abstract String getType();

	public double calculateRotation(Point2D.Double mouse){
		double angle1 = Math.atan2(center.getY() - 0,
				center.getX() - center.getX());
		double angle2 = Math.atan2(center.getY() - mouse.getY(),
				center.getX() - mouse.getX());
		return angle2-angle1;
	}

}
