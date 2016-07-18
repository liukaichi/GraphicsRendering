package cs355.view;

import cs355.controller.Controller;
import cs355.model.drawing.*;
import cs355.model.drawing.Rectangle;
import cs355.model.drawing.Shape;

import java.awt.*;
import java.awt.geom.*;
import java.util.ArrayList;

/**
 *
 *
 * atan2(p2 * x, -p2 * y)
 * use bounding boxes.
 *
 * Created by liukaichi on 9/4/2015.
 */
public class DrawableShape
{

    public static java.awt.Shape GenerateDrawableShape(Shape shape)
    {
        if (shape instanceof Square)
        {
            Square square = ((Square) shape);
            Point2D.Double center = new Point2D.Double(0,0);
            double size = square.getSize();
            return new Rectangle2D.Double(center.getX() - size / 2, center.getY() - size / 2, size, size);

        } else if (shape instanceof Circle)
        {
            Circle circle = ((Circle) shape);
            Point2D center = new Point2D.Double(0,0);
            double radius = circle.getRadius();

            return new Ellipse2D.Double(center.getX() - radius, center.getY() - radius, radius * 2, radius * 2);
        } else if (shape instanceof Ellipse)
        {
            Ellipse ellipse = ((Ellipse) shape);
            Point2D center = new Point2D.Double(0,0);
            double height = ellipse.getHeight();
            double width = ellipse.getWidth();

            return new Ellipse2D.Double(center.getX() - width / 2, center.getY() - height / 2, width, height);
        } else if (shape instanceof Line)
        {
            Line line = ((Line) shape);
            Point2D start = line.getCenter();
            Point2D end = line.getEnd();
            System.out.println("Generated Drawable Line!");
            return new Line2D.Double(start, end);
        } else if (shape instanceof Rectangle)
        {
            Rectangle rectangle = ((Rectangle) shape);
            Point2D.Double center = new Point2D.Double(0,0);
            double height = rectangle.getHeight();
            double width = rectangle.getWidth();
            return new Rectangle2D.Double(center.getX() - width / 2, center.getY() - height / 2, width, height);

        } else if (shape instanceof Triangle)
        {
            Triangle triangle = ((Triangle) shape);

            Point2D.Double normalizedA = new Point.Double((triangle.getA().getX() - triangle.getCenter().getX()),
                    (triangle.getA().getY() - triangle.getCenter().getY()));
            Point2D.Double normalizedB = new Point.Double((triangle.getB().getX() - triangle.getCenter().getX()),
                    (triangle.getB().getY() - triangle.getCenter().getY()));
            Point2D.Double normalizedC = new Point.Double((triangle.getC().getX() - triangle.getCenter().getX()),
                    (triangle.getC().getY() - triangle.getCenter().getY()));

            Polygon newTriangle = new Polygon();
            newTriangle.addPoint((int) normalizedA.getX(), ((int) normalizedA.getY()));
            newTriangle.addPoint((int) normalizedB.getX(), ((int) normalizedB.getY()));
            newTriangle.addPoint((int) normalizedC.getX(), ((int) normalizedC.getY()));

            return newTriangle;
        }

        return null;
    }

    public static ArrayList<java.awt.Shape> GenerateSelectionShapes(Shape shape)
    {
        double scale = Controller.getScale();

        ArrayList<java.awt.Shape> selectionShapes = new ArrayList<>();

        if (shape instanceof Square)
        {
            Square square = ((Square) shape);
            Point2D.Double center = new Point2D.Double(0,0);
            double size = square.getSize();

            selectionShapes.add(new Rectangle2D.Double(- size / 2, - size / 2, size, size));
            selectionShapes.add(createEllipseHandle(size / 2, scale));

        } else if (shape instanceof Circle)
        {
            Circle circle = ((Circle) shape);
            Point2D center = new Point2D.Double(0,0);
            double radius = circle.getRadius();

            selectionShapes.add(new Ellipse2D.Double(- radius, - radius, radius * 2, radius * 2));
            selectionShapes.add(createEllipseHandle(radius, scale));
        } else if (shape instanceof Ellipse)
        {
            Ellipse ellipse = ((Ellipse) shape);
            Point2D center = new Point2D.Double(0,0);
            double height = ellipse.getHeight();
            double width = ellipse.getWidth();

            selectionShapes.add(new Ellipse2D.Double(- width / 2, - height / 2, width, height));
            selectionShapes.add(createEllipseHandle(height/2, scale));
        } else if (shape instanceof Line)
        {
            System.out.println("Made a drawing Line:");
            Line line = ((Line) shape);
            Point2D start = line.getCenter();
            System.out.println("start = " + start);
            Point2D end = line.getEnd();
            System.out.println("end = " + end);

            Point2D.Double normalizedStart = new Point.Double((start.getX() - start.getX()),
                    (start.getY() - start.getY()));
            Point2D.Double normalizedEnd = new Point.Double((end.getX() - start.getX()),
                    (end.getY() - start.getY()));

            selectionShapes.add(new Line2D.Double(normalizedStart, normalizedEnd));
            selectionShapes.add(new Ellipse2D.Double(- 5 / scale,- 5 /scale ,10 /scale,10 /scale));
            selectionShapes.add(new Ellipse2D.Double(normalizedEnd.getX() - 5 /scale,normalizedEnd.getY() - 5 / scale,10 / scale,10 / scale));

        } else if (shape instanceof Rectangle)
        {
            Rectangle rectangle = ((Rectangle) shape);
            Point2D.Double center = new Point2D.Double(0,0);
            double height = rectangle.getHeight();
            double width = rectangle.getWidth();
            selectionShapes.add(new Rectangle2D.Double(- width / 2, - height / 2, width, height));
            selectionShapes.add(createEllipseHandle(height/2, scale));

        } else if (shape instanceof Triangle)
        {
            Triangle triangle = ((Triangle) shape);
            Point2D.Double normalizedA = new Point.Double((triangle.getA().getX() - triangle.getCenter().getX()),
                    (triangle.getA().getY() - triangle.getCenter().getY()));
            Point2D.Double normalizedB = new Point.Double((triangle.getB().getX() - triangle.getCenter().getX()),
                (triangle.getB().getY() - triangle.getCenter().getY()));
            Point2D.Double normalizedC = new Point.Double((triangle.getC().getX() - triangle.getCenter().getX()),
                    (triangle.getC().getY() - triangle.getCenter().getY()));

            Polygon newTriangle = new Polygon();
            newTriangle.addPoint((int) normalizedA.getX(), ((int) normalizedA.getY()));
            newTriangle.addPoint((int) normalizedB.getX(), ((int) normalizedB.getY()));
            newTriangle.addPoint((int) normalizedC.getX(), ((int) normalizedC.getY()));

            selectionShapes.add(newTriangle);

            double maxX = Math.max(normalizedA.getX(), normalizedB.getX());
            maxX =  Math.max(maxX, normalizedC.getX());
            double minX = Math.min(normalizedA.getX(), normalizedB.getX());
            minX =  Math.min(minX, normalizedC.getX());
            double maxY = Math.max(normalizedA.getY(), normalizedB.getY());
            maxY =  Math.max(maxY, normalizedC.getY());
            double minY = Math.min(normalizedA.getY(), normalizedB.getY());
            minY =  Math.min(minY, normalizedC.getY());

            selectionShapes.add(createEllipseHandle(Math.max(Math.abs(maxY),Math.abs(minY)), scale));
        }
        return selectionShapes;

    }

    /**
     * Makes a ellipse handle given the height and scale of the object.
     * @param height
     * @param scale
     * @return
     */
    static public Ellipse2D.Double createEllipseHandle (double height, double scale){
        System.out.println("scale = " + scale);
        return new Ellipse2D.Double(-5 / scale, (-height  - 20 / scale) , 10 / scale, 10 / scale);
    }
}

