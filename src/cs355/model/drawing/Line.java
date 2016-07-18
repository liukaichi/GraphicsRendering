package cs355.model.drawing;

import java.awt.*;
import java.awt.geom.Point2D;

/**
 * Add your line code here. You can add fields, but you cannot
 * change the ones that already exist. This includes the names!
 */
public class Line extends Shape {

	// The ending point of the line.
	private Point2D.Double end;

	/**
	 * Basic constructor that sets all fields.
	 * @param color the color for the new shape.
	 * @param start the starting point.
	 * @param end the ending point.
	 */
	public Line(Color color, Point2D.Double start, Point2D.Double end) {

		// Initialize the superclass.
		super(color, start);

		// Set the field.
		this.end = end;
	}

	/**
	 * Getter for this Line's ending point.
	 * @return the ending point as a Java point.
	 */
	public Point2D.Double getEnd() {
		return end;
	}

	/**
	 * Setter for this Line's ending point.
	 * @param end the new ending point for the Line.
	 */
	public void setEnd(Point2D.Double end) {
		this.end = end;
	}

	/**
	 *
	 * @param changeX
	 * @param changeY
	 */
	@Override public void updatePosition(double changeX, double changeY)
	{
		Point2D.Double updatedCenter = new Point2D.Double(center.getX() + changeX, center.getY() + changeY);
		center = updatedCenter;
		Point2D.Double updatedEnd = new Point2D.Double(end.getX() + changeX, end.getY() + changeY);
		end = updatedEnd;
	}

	/**
	 * Add your code to do an intersection test
	 * here. You <i>will</i> need the tolerance.
	 * @param pt = the point to test against.
	 * @param tolerance = the allowable tolerance.
	 * @return true if pt is in the shape,
	 *		   false otherwise.
	 */
	@Override
	public boolean pointInShape(Point2D.Double pt, double tolerance) {
		double distance = pointToLineDistance(center,end,pt);
		if (distance > tolerance){
			return false;
		}
		double xDistance = (end.getX()-center.getX()) / (Math.sqrt(Math.pow(end.getX() - center.getY() , 2) +
																	Math.pow(end.getY() - center.getY(), 2)));
		if (center.getX() < end.getX()){
			if (center.getX() <= pt.getX() + xDistance && pt.getX() + xDistance <= end.getX() ){
				System.out.println("center x = " + center.getX());
				System.out.println("end x = " + end.getX());
				System.out.println("pt.getX + xDistance = " + pt.getX() + xDistance);
				return true;
			}
		}
		else{
			if (center.getX() >= pt.getX() + xDistance && pt.getX() + xDistance >= end.getX() ){
				System.out.println("center x = " + center.getX());
				System.out.println("end x = " + end.getX());
				System.out.println("pt.getX + xDistance = " + pt.getX() + xDistance);
				return true;
			}
		}

		return false;
	}
	public double pointToLineDistance(Point2D.Double A, Point2D.Double B, Point2D.Double P) {
		double normalLength = Math.sqrt(Math.pow((B.x - A.x), 2) + Math.pow((B.y - A.y), 2));
		return Math.abs((P.x-A.x)*(B.y-A.y)-(P.y-A.y)*(B.x-A.x))/normalLength;
	}


	@Override public String getType()
	{
		return "Line";
	}

}
