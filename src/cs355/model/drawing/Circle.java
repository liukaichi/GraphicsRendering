package cs355.model.drawing;

import java.awt.*;
import java.awt.geom.Point2D;

/**
 * Add your circle code here. You can add fields, but you cannot
 * change the ones that already exist. This includes the names!
 */
public class Circle extends Shape
{

    // The radius.
    private double radius;

    /**
     * Basic constructor that sets all fields.
     *
     * @param color  the color for the new shape.
     * @param center the center of the new shape.
     * @param radius the radius of the new shape.
     */
    public Circle(Color color, Point2D.Double center, double radius)
    {

        // Initialize the superclass.
        super(color, center);

        // Set the field.
        this.radius = radius;
    }

    /**
     * Getter for this Circle's radius.
     *
     * @return the radius of this Circle as a double.
     */
    public double getRadius()
    {
        return radius;
    }

    /**
     * Setter for this Circle's radius.
     *
     * @param radius the new radius of this Circle.
     */
    public void setRadius(double radius)
    {
        this.radius = radius;
    }

    @Override public void updatePosition(double changeX, double changeY)
    {
        Point2D.Double updatedCenter = new Point2D.Double(center.getX() + changeX, center.getY() + changeY);
        center = updatedCenter;
    }

    /**
     * Add your code to do an intersection test
     * here. You shouldn't need the tolerance.
     *
     * @param pt        = the point to test against.
     * @param tolerance = the allowable tolerance.
     * @return true if pt is in the shape,
     * false otherwise.
     */
    @Override public boolean pointInShape(Point2D.Double pt, double tolerance)
    {
        //sets the bounding box
        if (pt.getX() > - radius && pt.getX() <  radius)
        {
            if (pt.getY() > - radius && pt.getX() < radius)
            {
                //detailed check
                double distance = Math
                        .sqrt(Math.pow(pt.getX(), 2) + Math.pow(pt.getY(), 2));
                if (distance > this.getRadius())
                {
                    return false;
                } else
                {
                    return true;
                }
            }
        }
        System.out.println("Outside box");
        return false;
    }

    @Override public String getType()
    {
        return "Circle";
    }

}
