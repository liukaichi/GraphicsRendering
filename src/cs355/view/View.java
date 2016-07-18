package cs355.view;

import cs355.GUIFunctions;
import cs355.controller.Controller;
import cs355.model.drawing.Line;
import cs355.model.Model;
import cs355.model.drawing.Shape;
import cs355.model.drawing.Transform;
import cs355.model.image.*;
import cs355.model.scene.Line3D;
import cs355.model.scene.WireFrame;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.List;

/**
 * Created by liukaichi on 9/2/2015.
 */
public class View implements ViewRefresher
{
    Model model = null;
    public static final int DISPLAY_HEIGHT = 2048;
    public static final int DISPLAY_WIDTH = 2048;
    final double ASPECT_RATIO = DISPLAY_WIDTH / DISPLAY_HEIGHT;
    final double VIEWING_ANGLE = Math.PI / 2 - .1;
    final double FIELD_OF_VIEW = 1.0 / Math.tan(VIEWING_ANGLE / 2.0);
    final double FAR_CLIP = 50;
    final double NEAR_CLIP = 2;

    public View(Model model)
    {
        this.model = model;
        this.model.addObserver(this);
    }

    @Override public void refreshView(Graphics2D g2d)
    {
        drawBackground(g2d);
        draw2DShapes(g2d);
        draw3DShapes(g2d);
    }

    private void drawBackground(Graphics2D g2d)
    {
        if (Controller.getDrawBackground() && model.getBackgroundImage() != null)
        {
            AffineTransform affineTransform = Transform.worldToView(
                    Controller.getHorizPosition(),
                    Controller.getVertiPosition(),
                    Controller.getScale());

            g2d.setTransform(affineTransform);
            BufferedImage imageToDraw = model.getBackgroundImage();
            g2d.drawImage(imageToDraw, 0,0,imageToDraw.getWidth(), imageToDraw.getHeight(), null);
        }
    }

    @Override public void update(Observable o, Object arg)
    {
        System.out.println("Updating");
        GUIFunctions.refresh();
    }

    private void draw3DShapes(Graphics2D g2d)
    {
        List<java.awt.Shape> generated3DShapes = new ArrayList<>();
        if (Controller.getDisplay3D())
        {
            AffineTransform transform = new AffineTransform();
            Matrix4D worldToCameraMatrix = Matrix4D.generateWorldToCamera(Controller.getWorldToCameraInput());

            for (WireFrame shape : model.getShapes3D())
            {
                Iterator<Line3D> iterator = shape.getLines();
                while (iterator.hasNext())
                {
                    Line3D newLine = iterator.next();
                    Vector4D start4D = worldToCameraMatrix.transformVector(new Vector4D(newLine.start));
                    Vector4D end4D = worldToCameraMatrix.transformVector(new Vector4D(newLine.end));
                    Matrix4D clipMatrix = Matrix4D.generateClipMatrix(FIELD_OF_VIEW, ASPECT_RATIO, NEAR_CLIP, FAR_CLIP);

                    Vector4D newStart = clipMatrix.transformVector(start4D);
                    Vector4D newEnd = clipMatrix.transformVector(end4D);

                    //if one is inside of other ones, draw.
                    if (newStart.isInsideNearClips() && newEnd.isInsideNearClips())
                    {
                        //if one of them is outside near, draw.
                        if (newStart.isInsideOutsideClip() || newEnd.isInsideOutsideClip())
                        {
                            Point2D screenStartPoint = newStart.generateClipToScreen(DISPLAY_WIDTH,DISPLAY_HEIGHT);
                            Point2D screenEndPoint = newEnd.generateClipToScreen(DISPLAY_WIDTH,DISPLAY_HEIGHT);
                            generated3DShapes.add(new Line2D.Double(screenStartPoint, screenEndPoint));
                        }
                    }



                }
            }
            for (java.awt.Shape shape : generated3DShapes)
            {
                AffineTransform affineTransform = new AffineTransform();
                affineTransform = Transform.worldToView(
                        Controller.getHorizPosition(),
                        Controller.getVertiPosition(),
                        Controller.getScale());

                g2d.setTransform(affineTransform);
                g2d.setColor(Color.RED);
                g2d.draw(shape);

            }
        }
    }

    private void draw2DShapes(Graphics2D g2d)
    {
        //System.out.println("View.refreshView");
        List<Shape> shapesReversed = model.getShapesReversed();
        for (Shape shape : shapesReversed)
        {
            AffineTransform transform = new AffineTransform();
            if (shape instanceof Line)
            {
                transform = Transform.worldToView(Controller.getHorizPosition(), Controller.getVertiPosition(),
                        Controller.getScale());
            } else
            {
                transform = Transform.objectToView(shape, Controller.getHorizPosition(), Controller.getVertiPosition(),
                        Controller.getScale());
            }

            g2d.setTransform(transform);
            g2d.setColor(shape.getColor());
            g2d.draw(DrawableShape.GenerateDrawableShape(shape));
            g2d.fill(DrawableShape.GenerateDrawableShape(shape));

        }

        for (java.awt.Shape shape : model.getSelectionShapes())
        {
            AffineTransform transform = new AffineTransform();
            if (shape instanceof Line)
            {
                //no transform for lines
            } else
            {
                transform = Transform.objectToView(model.getSelectedShape(), Controller.getHorizPosition(),
                        Controller.getVertiPosition(), Controller.getScale());
            }
            g2d.setTransform(transform);
            g2d.setColor(Color.RED);
            g2d.draw(shape);

        }
    }
}
