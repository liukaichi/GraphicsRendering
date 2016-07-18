package cs355.model.drawing;

import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;

/**
 * Created by liukaichi on 10/15/2015.
 */
public class Transform
{

    static public AffineTransform objectToWorld(Shape shape)
    {
        AffineTransform objectToWorldTransform = new AffineTransform();
        //matrices for the rotation transformation
        double rotation[] = { Math.cos(-shape.getRotation()), -Math.sin(-shape.getRotation()),
                Math.sin(-shape.getRotation()), Math.cos(-shape.getRotation()), 0, 0};
        double translation[] = { 1, 0, 0, 1, shape.getCenter().getX(), shape.getCenter().getY() };
        AffineTransform rotate = new AffineTransform(rotation);
        AffineTransform translate = new AffineTransform(translation);

        objectToWorldTransform.concatenate(translate);
        objectToWorldTransform.concatenate(rotate);
        return objectToWorldTransform;
    }

    static public AffineTransform worldToView(int horizPosition, int vertiPosition, double scale)
    {
        AffineTransform worldToViewTransform = new AffineTransform();

        //scrollbar translation
        double newScrollTranslation[] = { 1, 0, 0, 1, -horizPosition, -vertiPosition };
        AffineTransform scrollTranslate = new AffineTransform(newScrollTranslation);

        //zoom

        double newZoomScale[] = { scale, 0, 0, scale, 0, 0 };
        AffineTransform zoomScale = new AffineTransform(newZoomScale);

        worldToViewTransform.concatenate(zoomScale);
        worldToViewTransform.concatenate(scrollTranslate);


            return worldToViewTransform;
    }

    static public AffineTransform objectToView(Shape shape, int horizPosition, int vertiPosition, double scale)
    {
        AffineTransform objectToViewTransform = new AffineTransform();
        objectToViewTransform.concatenate(worldToView(horizPosition, vertiPosition, scale));
        objectToViewTransform.concatenate(objectToWorld(shape));
        return objectToViewTransform;
    }

    //************INVERSES*************//
    static public AffineTransform worldToObject(Shape shape)
    {

        try
        {
            return objectToWorld(shape).createInverse();
        } catch (NoninvertibleTransformException e)
        {
            e.printStackTrace();
        }
        return null;
    }



    static public AffineTransform viewToWorld(int horizPosition, int vertiPosition, double scale){
        try
        {
            return worldToView(horizPosition, vertiPosition, scale).createInverse();
        } catch (NoninvertibleTransformException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    static public AffineTransform viewToObject(Shape selectedShape, int horizPosition, int vertiPosition, double scale)
    {
        try
        {
            return objectToView(selectedShape, horizPosition, vertiPosition, scale).createInverse();
        } catch (NoninvertibleTransformException e)
        {
            e.printStackTrace();
        }
        return null;
    }

}
