package cs355.model.drawing;

import java.awt.*;
import java.awt.geom.Point2D;

/**
 * Add your triangle code here. You can add fields, but you cannot
 * change the ones that already exist. This includes the names!
 */
public class Triangle extends Shape
{

    // The three points of the triangle.
    private Point2D.Double a;
    private Point2D.Double b;
    private Point2D.Double c;
    private Point2D.Double normalizedA;
    private Point2D.Double normalizedB;
    private Point2D.Double normalizedC;

    /**
     * Basic constructor that sets all fields.
     *
     * @param color  the color for the new shape.
     * @param center the center of the new shape.
     * @param a      the first point, relative to the center.
     * @param b      the second point, relative to the center.
     * @param c      the third point, relative to the center.
     */
    public Triangle(Color color, Point2D.Double center, Point2D.Double a, Point2D.Double b, Point2D.Double c)
    {

        // Initialize the superclass.
        super(color, center);

        // Set fields.
        this.a = a;
        this.b = b;
        this.c = c;
        updateNormalizedPositions();
    }

    public static Point2D.Double findCenter(Point2D.Double a, Point2D.Double b, Point2D.Double c)
    {
        double centerX = (a.getX() + b.getX() + c.getX()) / 3;
        double centerY = (a.getY() + b.getY() + c.getY()) / 3;
        return new Point2D.Double(centerX, centerY);
    }



    @Override public void updatePosition(double changeX, double changeY)
    {
        a = new Point2D.Double(a.getX() + changeX, a.getY() + changeY);
        b = new Point2D.Double(b.getX() + changeX, b.getY() + changeY);
        c = new Point2D.Double(c.getX() + changeX, c.getY() + changeY);
        center = findCenter(a,b,c);
        updateNormalizedPositions();

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
        double firstCross = cross(normalizedA, normalizedB, pt);
        double secondCross = cross(normalizedB, normalizedC, pt);
        double thirdCross = cross(normalizedC, normalizedA, pt);

        if (firstCross <=0 && secondCross <= 0 && thirdCross <= 0 ||
                firstCross >=0 && secondCross >= 0 && thirdCross >= 0){
            return true;
        }
        else{
            return false;
        }

    }
    private double cross(Point2D.Double a, Point2D.Double b, Point2D.Double p)
    {
        Point2D.Double uVector = new Point2D.Double(b.getX()-a.getX(), b.getY()-a.getY());
        Point2D.Double yVector = new Point2D.Double(p.getX()-a.getX(), p.getY()-a.getY());

        return uVector.getX()*yVector.getY() - uVector.getY()*yVector.getX();
    }

    @Override public String getType()
    {
        return "Triangle";
    }

    /**
     * Gets positions in an object coordinate field
     */
    private void updateNormalizedPositions(){
        normalizedA = new Point.Double((a.getX() - center.getX()),
                (a.getY() - center.getY()));
        normalizedB = new Point.Double((b.getX() - center.getX()),
                (b.getY() - center.getY()));
        normalizedC = new Point.Double((c.getX() - center.getX()),
                (c.getY() - center.getY()));
    }



    /**
     * Getter for the first point.
     *
     * @return the first point as a Java point.
     */
    public Point2D.Double getA()
    {
        return a;
    }

    /**
     * Setter for the first point.
     *
     * @param a the new first point.
     */
    public void setA(Point2D.Double a)
    {
        this.a = a;
    }

    /**
     * Getter for the second point.
     *
     * @return the second point as a Java point.
     */
    public Point2D.Double getB()
    {
        return b;
    }

    /**
     * Setter for the second point.
     *
     * @param b the new second point.
     */
    public void setB(Point2D.Double b)
    {
        this.b = b;
    }

    /**
     * Getter for the third point.
     *
     * @return the third point as a Java point.
     */
    public Point2D.Double getC()
    {
        return c;
    }

    /**
     * Setter for the third point.
     *
     * @param c the new third point.
     */
    public void setC(Point2D.Double c)
    {
        this.c = c;
    }
}
