package cs355.model.drawing;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;

/**
 * Add your ellipse code here. You can add fields, but you cannot
 * change the ones that already exist. This includes the names!
 */
public class Ellipse extends Shape {

	// The width of this shape.
	private double width;

	// The height of this shape.
	private double height;

	/**
	 * Basic constructor that sets all fields.
	 * @param color the color for the new shape.
	 * @param center the center of the new shape.
	 * @param width the width of the new shape.
	 * @param height the height of the new shape.
	 */
	public Ellipse(Color color, Point2D.Double center, double width, double height) {

		// Initialize the superclass.
		super(color, center);

		// Set fields.
		this.width = width;
		this.height = height;
	}
	public Ellipse(java.awt.Shape shape, double rotation){
		super(shape, rotation);
		Ellipse2D newEllipse = (Ellipse2D) shape;
		this.width = newEllipse.getWidth();
		this.height = newEllipse.getHeight();

	}

	/**
	 * Getter for this shape's width.
	 * @return this shape's width as a double.
	 */
	public double getWidth() {
		return width;
	}

	/**
	 * Setter for this shape's width.
	 * @param width the new width.
	 */
	public void setWidth(double width) {
		this.width = width;
	}

	/**
	 * Getter for this shape's height.
	 * @return this shape's height as a double.
	 */
	public double getHeight() {
		return height;
	}

	/**
	 * Setter for this shape's height.
	 * @param height the new height.
	 */
	public void setHeight(double height) {
		this.height = height;
	}

	@Override public void updatePosition(double changeX, double changeY)
	{
		Point2D.Double updatedCenter = new Point2D.Double(center.getX() + changeX, center.getY() + changeY);
		center = updatedCenter;
	}

	/**
	 * Add your code to do an intersection test
	 * here. You shouldn't need the tolerance.
	 * @param worldPoint = the point to test against.
	 * @param tolerance = the allowable tolerance.
	 * @return true if pt is in the shape,
	 *		   false otherwise.
	 */
	@Override
	public boolean pointInShape(Point2D.Double worldPoint, double tolerance) {
		//checks if it's in the bounding box
		if (worldPoint.getX() > -(width / 2) && worldPoint.getX() < (width / 2)){
			if (worldPoint.getY() > -(height / 2) && worldPoint.getY() < (height / 2)){
				System.out.println("IN THE BOX!");
				double a = width/2;
				double b = height/2;
				//uses the equation for an ellipse.
				double distance = Math.sqrt(Math.pow((worldPoint.getX()) / a, 2)
											+ Math.pow((worldPoint.getY()) / b, 2));
				if (distance > 1)
				{
					return false;
				} else
				{
					return true;
				}
			}
		}
		System.out.println("Out of the box!");
		return false;

	}

	@Override public String getType()
	{
		return "Ellipse";
	}
}
