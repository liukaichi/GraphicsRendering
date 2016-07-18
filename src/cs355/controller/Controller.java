package cs355.controller;

import cs355.GUIFunctions;
import cs355.model.Model;
import cs355.model.drawing.*;
import cs355.model.drawing.Rectangle;
import cs355.model.drawing.Shape;
import cs355.model.scene.CS355Scene;
import cs355.model.scene.Instance;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.io.File;
import java.util.Iterator;

/**
 * Created by liukaichi on 9/2/2015.
 */
public class Controller implements CS355Controller {
    Color currentColor = null;
    Model model;
    ButtonState buttonState = ButtonState.SELECT;
    double firstX;
    double firstY;
    static double scale = 1;
    //static double scale = .25;
    static int horizPosition = 0;
    static int vertiPosition = 0;
    final int RESOLUTION = 512;
    //final int RESOLUTION = 2048;
    final int FULL_RESOLUTION = 2048;
    final double DEFAULT_TOLERANCE = 4;
    final int DEFAULT_HANDLE_SIZE = 10;

    //for storing new shapes
    double cornerX;
    double cornerY;
    double height;
    double width;
    Point2D.Double center;
    Point2D.Double triFirstPoint = null;
    Point2D.Double triSecondPoint = null;
    Point2D.Double triThirdPoint = null;
    Shape selectedShape = null;
    Point2D selectedPoint = null;

    //3D additions
    static private double totalX = 0;
    static private double totalY = 2;
    static private double totalZ = 20;
    static private float totalRotation = 0; // in degrees
    private static boolean display3D = true;
    private static boolean drawBackground = true;

    public static boolean getDrawBackground()
    {
        return drawBackground;
    }

    public enum ButtonState {
        LINE, SQUARE, RECTANGLE, CIRCLE, ELLIPSE, TRIANGLE, SELECT, ROTATION
    }
    static public WorldToCameraInput getWorldToCameraInput()
    {
        return new WorldToCameraInput(totalX, totalY, totalZ, totalRotation);
    }

    static public boolean getDisplay3D(){return display3D;}

    static public double getScale() {
        return scale;
    }

    static public int getHorizPosition() {

        return horizPosition;
    }

    public static int getVertiPosition() {
        return vertiPosition;
    }

    public Controller(Model model) {
        this.model = model;
    }

    @Override
    public void colorButtonHit(Color c) {
        GUIFunctions.changeSelectedColor(c);
        currentColor = c;
        if (selectedShape != null) {
            selectedShape.setColor(c);
            //This gets the index of the shape that is selected.
            int index = 0;
            for (Shape shape : model.getShapes()) {
                if (shape.equals(selectedShape)) {
                    break;
                }
                index++;
            }
            model.updateShape(selectedShape, index);
            model.setSelectedShape(selectedShape);
        }
    }

    @Override
    public void lineButtonHit() {
        buttonState = ButtonState.LINE;
    }

    @Override
    public void squareButtonHit() {
        buttonState = ButtonState.SQUARE;
    }

    @Override
    public void rectangleButtonHit() {
        buttonState = ButtonState.RECTANGLE;
    }

    @Override
    public void circleButtonHit() {
        buttonState = ButtonState.CIRCLE;
    }

    @Override
    public void ellipseButtonHit() {
        buttonState = ButtonState.ELLIPSE;
    }

    @Override
    public void triangleButtonHit() {
        buttonState = ButtonState.TRIANGLE;
    }

    @Override
    public void selectButtonHit() {
        buttonState = ButtonState.SELECT;
    }

    @Override
    public void zoomInButtonHit() {
        scale *= 2;
        if (scale > 4) {
            scale = 4;
        }
        else {
            System.out.println("scale = " + scale);

            //adjusting the first point after a zoom.
            AffineTransform viewToWorldTransform = Transform.viewToWorld(horizPosition, vertiPosition, scale);
            Point2D.Double firstPoint = new Point2D.Double();
            viewToWorldTransform.transform(new Point2D.Double(firstX, firstY), firstPoint);
            firstX = firstPoint.getX();
            firstY = firstPoint.getY();

            //resetting bars
            double newBarLength = RESOLUTION / scale;
            System.out.println("newBarLength = " + newBarLength);
            GUIFunctions.setHScrollBarKnob((int) (newBarLength));
            GUIFunctions.setVScrollBarKnob((int) (newBarLength));
            GUIFunctions.setVScrollBarPosit((int) (vertiPosition + newBarLength / 2));
            GUIFunctions.setHScrollBarPosit((int) (horizPosition + newBarLength / 2));

            //redraw selected shapes
            model.setSelectedShape(selectedShape);

            GUIFunctions.refresh();
        }
    }

    @Override
    public void zoomOutButtonHit() {
        scale /= 2;
        if (scale < .25) {
            scale = .25;
        }
        else {
            System.out.println("scale = " + scale);

            //adjusting the first point after a zoom.
            AffineTransform viewToWorldTransform = Transform.viewToWorld(horizPosition, vertiPosition, scale);
            Point2D.Double firstPoint = new Point2D.Double();
            viewToWorldTransform.transform(new Point2D.Double(firstX, firstY), firstPoint);
            firstX = firstPoint.getX();
            firstY = firstPoint.getY();

            //resetting bars
            double newBarLength = RESOLUTION / scale;
            System.out.println("newBarLength = " + newBarLength);
            if (vertiPosition + newBarLength >= FULL_RESOLUTION) {
                GUIFunctions.setVScrollBarPosit((int) (FULL_RESOLUTION - newBarLength));
            }
            else {
                GUIFunctions.setVScrollBarPosit((int) (vertiPosition - newBarLength / 4));
            }
            if (horizPosition + newBarLength >= FULL_RESOLUTION) {
                GUIFunctions.setHScrollBarPosit((int) (FULL_RESOLUTION - newBarLength));
            }
            else {
                GUIFunctions.setHScrollBarPosit((int) (horizPosition - newBarLength / 4));
            }
            GUIFunctions.setHScrollBarKnob((int) (newBarLength));
            GUIFunctions.setVScrollBarKnob((int) (newBarLength));

            //redraw selectedShapes
            model.setSelectedShape(selectedShape);

            GUIFunctions.refresh();
        }
    }

    @Override
    public void hScrollbarChanged(int value) {
        horizPosition = value;
        System.out.println("horizPosition = " + horizPosition);
        GUIFunctions.refresh();
    }

    @Override
    public void vScrollbarChanged(int value) {
        System.out.println("vertiPosition = " + vertiPosition);
        vertiPosition = value;
        GUIFunctions.refresh();
    }

    /**
     * The shell will call this method when the user tries to open a scene. It should simply pass the File parameter
     * to CS355Scene.open(File). The CS355Scene class will open and parse the *.scn and *.obj files
     * and build a scene for you.
     *
     * @param file = the image file to load.
     */
    @Override
    public void openScene(File file) {
        CS355Scene scene = new CS355Scene();
        if (scene.open(file)){
            for (Instance instance : scene.instances()){
                model.addInstance(instance);
            }
        }
        else{
            GUIFunctions.printf("That was a bad file you got there.");
        }
    }


    /**
     * This method should toggle your internal state for whether to draw the 3D model. It should initiate a screen
     * refresh to update with the model appropriately drawn or not drawn.
     */
    @Override
    public void toggle3DModelDisplay() {
        display3D = !display3D;
        GUIFunctions.printf("Display 3D: " + display3D);
        GUIFunctions.refresh();
    }

    /**
     * The main shell will call this method at regular intervals whenever a key is pressed or held down. A list of
     * the pressed keys will be passed to it, and you should respond and update your camera state like you did in
     * Lab #4. It should also initiate a screen refresh to update the screen with the rendered model after all of the
     * keys have been processed. If the 3D model is not currently being shown, any key presses should be ignored
     *
     * @param iterator = the iterator over the keys.
     */
    @Override
    public void keyPressed(Iterator<Integer> iterator) {
        if (!display3D)
        {
            return; //no input should be taken when 3D display is off.
        }
        int keyPressed = -1;
        while (iterator.hasNext()) {
            keyPressed = iterator.next();

            if (keyPressed == KeyEvent.VK_W) {
                GUIFunctions.printf("W pressed");
//                totalZ += .25 * Math.cos(Math.toRadians(totalRotation));
                totalZ -= .25 * Math.cos(Math.toRadians(totalRotation));
//                totalX -= .25 * Math.sin(Math.toRadians(totalRotation));
                totalX += .25 * Math.sin(Math.toRadians(totalRotation));
            }
            else if (keyPressed == KeyEvent.VK_A) {
                GUIFunctions.printf("You are pressing A!");
//                totalZ += .25 * Math.sin(Math.toRadians(totalRotation));
                totalZ -= .25 * Math.sin(Math.toRadians(totalRotation));
//                totalX += .25 * Math.cos(Math.toRadians(totalRotation));
                totalX -= .25 * Math.cos(Math.toRadians(totalRotation));
            }
            else if (keyPressed == KeyEvent.VK_S) {
                GUIFunctions.printf("S pressed");
//                totalZ -= .25 * Math.cos(Math.toRadians(totalRotation));
                totalZ += .25 * Math.cos(Math.toRadians(totalRotation));
//                totalX += .25 * Math.sin(Math.toRadians(totalRotation));
                totalX -= .25 * Math.sin(Math.toRadians(totalRotation));

            }
            else if (keyPressed == KeyEvent.VK_D) {
                GUIFunctions.printf("D pressed");
//                totalZ -= .25 * Math.sin(Math.toRadians(totalRotation));
                totalZ += .25 * Math.sin(Math.toRadians(totalRotation));
//                totalX -= .25 * Math.cos(Math.toRadians(totalRotation));
                totalX += .25 * Math.cos(Math.toRadians(totalRotation));

            }
            else if (keyPressed == KeyEvent.VK_Q) {
                GUIFunctions.printf("Q pressed");
                totalRotation -= 1;
                System.out.println("totalRotation = " + totalRotation);
            }
            else if (keyPressed == KeyEvent.VK_E) {
                GUIFunctions.printf("E pressed");
                totalRotation += 1;
                System.out.println("totalRotation = " + totalRotation);
            }
            else if (keyPressed == KeyEvent.VK_F) {
                GUIFunctions.printf("F pressed");
                totalY -= .25;

            }
            else if (keyPressed == KeyEvent.VK_R) {
                GUIFunctions.printf("R pressed");
                totalY += .25;

            }
            else if (keyPressed == KeyEvent.VK_H) {
                GUIFunctions.printf("H pressed");
                //glMatrixMode(GL_MODELVIEW);
                //glLoadIdentity();
                resetCoordinates();
            }
            GUIFunctions.refresh();
        }

    }

    private void resetCoordinates() {
        totalX = 0;
        totalY = 2;
        totalZ = 15;
        totalRotation = 0; // in radians

    }

    @Override
    public void openImage(File file) {
        model.getImage().open(file);
        model.resetBackgroundImage();
        GUIFunctions.refresh();
    }


    @Override
    public void saveImage(File file) {
        model.getImage().save(file);
    }

    @Override
    public void toggleBackgroundDisplay() {
        drawBackground = (!drawBackground);
        GUIFunctions.refresh();
    }

    @Override
    public void saveDrawing(File file) {
        model.save(file);
    }

    @Override
    public void openDrawing(File file) {
        model.open(file);
        GUIFunctions.refresh();
    }

    @Override
    public void doDeleteShape() {
        if (selectedShape != null) {
            int index = getSelectedIndex();
            model.deleteShape(index);
            selectedShape = null;
            model.setSelectedShape(null);
        }

    }

    @Override
    public void doEdgeDetection() {
        model.getImage().edgeDetection();
        model.resetBackgroundImage();
        GUIFunctions.refresh();

    }

    @Override
    public void doSharpen() {
        model.getImage().sharpen();
        model.resetBackgroundImage();
        GUIFunctions.refresh();
    }

    @Override
    public void doMedianBlur() {
        model.getImage().medianBlur();
        model.resetBackgroundImage();
        GUIFunctions.refresh();
    }

    @Override
    public void doUniformBlur() {
        model.getImage().uniformBlur();
        model.resetBackgroundImage();
        GUIFunctions.refresh();
    }

    @Override
    public void doGrayscale() {
        model.getImage().grayscale();
        model.resetBackgroundImage();
        GUIFunctions.refresh();
    }

    @Override
    public void doChangeContrast(int contrastAmountNum) {
        model.getImage().contrast(contrastAmountNum);
        model.resetBackgroundImage();
        GUIFunctions.refresh();
    }

    @Override
    public void doChangeBrightness(int brightnessAmountNum) {
        model.getImage().brightness(brightnessAmountNum);
        model.resetBackgroundImage();
        GUIFunctions.refresh();
    }

    @Override
    public void doMoveForward() {
        int index = getSelectedIndex();
        model.moveForward(index);
    }

    @Override
    public void doMoveBackward() {
        int index = getSelectedIndex();
        model.moveBackward(index);
    }

    @Override
    public void doSendToFront() {
        int index = getSelectedIndex();
        model.moveToFront(index);
    }

    @Override
    public void doSendtoBack() {
        int index = getSelectedIndex();
        model.movetoBack(index);
    }

    private int getSelectedIndex() {
        int index = 0;
        for (Shape shape : model.getShapes()) {
            if (shape.equals(selectedShape)) {
                break;
            }
            index++;
        }
        return index;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        Point2D.Double transformClick = new Point2D.Double();

        AffineTransform viewToWorldTransform = Transform.viewToWorld(horizPosition, vertiPosition, scale);
        viewToWorldTransform.transform(new Point2D.Double(e.getX(), e.getY()), transformClick);

        firstX = transformClick.getX();
        firstY = transformClick.getY();
        System.out.println("World Space Points");
        System.out.println("firstX = " + firstX);
        System.out.println("firstY = " + firstY);
        switch (buttonState) {
            case SELECT:
                boolean hit = false;

                //checks selection handle hits
                for (java.awt.Shape selectionShape : model.getSelectionShapes()) {
                    if (selectionShape instanceof Ellipse2D) {
                        Ellipse2D ellipse = (Ellipse2D) selectionShape;

                        Point2D.Double objectCoor = new Point2D.Double();
                        Transform.worldToObject(selectedShape).transform(transformClick, objectCoor);
                        //double selectedShapeOffset = selectedShape.getCenter().getY() -

                        if (ellipse.contains(objectCoor.getX(), objectCoor.getY())
                                && ellipse.getWidth() == DEFAULT_HANDLE_SIZE / scale) {
                            selectedPoint = new Point2D.Double(ellipse.getCenterX(), ellipse.getCenterY());
                            buttonState = ButtonState.ROTATION;
                            System.out.println("NOW IN ROTATION MODE");
                            hit = true;
                        }
                    }
                }

                //if you didn't hit a selection handle
                if (hit != true) {
                    //check for object hits
                    for (Shape shape : model.getShapes()) {
                        if (shape instanceof Line) {
                            if (shape.pointInShape(new Point2D.Double(transformClick.getX(), transformClick.getY()),
                                    DEFAULT_TOLERANCE / scale)) {
                                selectedShape = shape;
                                model.setSelectedShape(shape);
                                System.out.println(selectedShape.getType() + " hit!");
                                hit = true;
                                if (selectedShape.getColor() != null) {
                                    colorButtonHit(selectedShape.getColor());
                                }
                                break;
                            }

                        }
                        else {
                            Point2D.Double objectCoor = new Point2D.Double();
                            Transform.worldToObject(shape).transform(transformClick, objectCoor);
                            if (shape.pointInShape(new Point2D.Double(objectCoor.getX(), objectCoor.getY()),
                                    DEFAULT_TOLERANCE / scale)) {
                                selectedShape = shape;
                                model.setSelectedShape(shape);
                                System.out.println(selectedShape.getType() + " hit!");
                                System.out.println("It's rotation is " + shape.getRotation());
                                hit = true;
                                if (selectedShape.getColor() != null) {
                                    colorButtonHit(selectedShape.getColor());
                                }
                                break;
                            }
                        }
                    }
                }
                //if nothing is hit, then unselect everything.
                if (!hit) {
                    selectedShape = null;
                    model.setSelectedShape(null);
                }

                break;
            case CIRCLE:
                model.addShape(
                        new Circle(currentColor, new Point2D.Double(transformClick.getX(), transformClick.getY()), 0));
                System.out.println("Circle Made!");
                break;

            case ELLIPSE:
                model.addShape(
                        new Ellipse(currentColor, new Point2D.Double(transformClick.getX(), transformClick.getY()), 0, 0));
                System.out.println("Ellipse Made!");
                break;

            case LINE:
                Point2D.Double newPoint = new Point2D.Double(e.getX(), e.getY());
                model.addShape(new Line(currentColor, newPoint, newPoint));
                System.out.println("Line Made!");
                break;

            case RECTANGLE:
                model.addShape(
                        new Rectangle(currentColor, new Point2D.Double(transformClick.getX(), transformClick.getY()), 0,
                                0));
                System.out.println("Rectangle Made!");
                break;

            case SQUARE:

                model.addShape(
                        new Square(currentColor, new Point2D.Double(transformClick.getX(), transformClick.getY()), 0));
                System.out.println("Square Made!");
                break;

            case TRIANGLE:
                if (triFirstPoint == null) {
                    triFirstPoint = new Point2D.Double(transformClick.getX(), transformClick.getY());
                }
                else if (triSecondPoint == null) {
                    triSecondPoint = new Point2D.Double(transformClick.getX(), transformClick.getY());
                }
                else {
                    triThirdPoint = new Point2D.Double(transformClick.getX(), transformClick.getY());
                    Point2D.Double center = Triangle.findCenter(triFirstPoint, triSecondPoint, triThirdPoint);
                    model.addShape(new Triangle(currentColor, center, triFirstPoint, triSecondPoint, triThirdPoint));
                    triFirstPoint = null;
                    triSecondPoint = null;
                    triThirdPoint = null;
                }
                break;
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (buttonState == ButtonState.ROTATION) {
            buttonState = ButtonState.SELECT;
            System.out.println("LEAVING ROTATION MODE");
        }

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {
        Point2D.Double transformClick = new Point2D.Double();

        AffineTransform viewToWorldTransform = Transform.viewToWorld(horizPosition, vertiPosition, scale);
        viewToWorldTransform.transform(new Point2D.Double(e.getX(), e.getY()), transformClick);

        switch (buttonState) {
            //Rotation is dragging, but on a selection handle.
            case ROTATION:
                if (selectedShape != null) {
                    firstX = transformClick.getX();
                    firstY = transformClick.getY();

                    if (selectedShape.getType().equals("Line")) {
                        Line line = (Line) selectedShape;
                        if (selectedPoint.equals(new Point2D.Double(0, 0))) {
                            line.setCenter(new Point2D.Double(firstX, firstY));
                            System.out.println("Hit center!");
                        }
                        else {

                            Point2D.Double newPoint = new Point2D.Double(firstX, firstY);
                            line.setEnd(newPoint);
                            selectedPoint = newPoint;

                            System.out.println("Hit end!");
                        }
                        selectedShape = line;

                        //This gets the index of the shape that is selected.
                        int index = 0;
                        for (Shape shape : model.getShapes()) {
                            if (shape.equals(selectedShape)) {
                                break;
                            }
                            index++;
                        }
                        model.updateShape(selectedShape, index);
                        model.setSelectedShape(selectedShape);
                    }
                    else {
                        selectedShape.setRotation(selectedShape.calculateRotation(new Point2D.Double(firstX, firstY)));
                        System.out.println("Rotation is now: " + selectedShape.getRotation());
                        model.setSelectedShape(selectedShape);

                        //This gets the index of the shape that is selected.
                        int index = 0;
                        for (Shape shape : model.getShapes()) {
                            if (shape.equals(selectedShape)) {
                                break;
                            }
                            index++;
                        }
                        model.updateShape(selectedShape, index);
                    }
                }
                break;
            case SELECT:
                if (selectedShape != null) {

                    System.out.println("e.getPoint() = " + e.getPoint());
                    System.out.println("transformClick = " + transformClick);
                    System.out.println("firstX = " + firstX);
                    System.out.println("firstY = " + firstY);
                    double changeX = transformClick.getX() - firstX;
                    double changeY = transformClick.getY() - firstY;
                    firstX = transformClick.getX();
                    firstY = transformClick.getY();
                    selectedShape.updatePosition(changeX, changeY);
                    model.setSelectedShape(selectedShape);

                    //This gets the index of the shape that is selected.
                    int index = 0;
                    for (Shape shape : model.getShapes()) {
                        if (shape.equals(selectedShape)) {
                            break;
                        }
                        index++;
                    }
                    model.updateShape(selectedShape, index);
                }
                break;
            case CIRCLE:

                height = Math.abs(firstY - transformClick.getY());
                width = Math.abs(firstX - transformClick.getX());
                double radius = Math.min(height / 2, width / 2);
                double centerX = (firstX + transformClick.getX()) / 2;
                double centerY = (firstY + transformClick.getY()) / 2;

                if (centerX < (firstX - radius)) {
                    centerX = firstX - radius;
                }
                else if (centerX > (firstX + radius)) {
                    centerX = firstX + radius;
                }
                if (centerY < (firstY - radius)) {
                    centerY = firstY - radius;
                }
                else if (centerY > (firstY + radius)) {
                    centerY = firstY + radius;
                }

                center = new Point2D.Double(centerX, centerY);

                model.updateShape(new Circle(currentColor, center, radius), 0);
                System.out.println("Circle Made!");
                break;

            case ELLIPSE:
                height = Math.abs(firstY - transformClick.getY());
                width = Math.abs(firstX - transformClick.getX());

                center = new Point2D.Double((firstX + transformClick.getX()) / 2, (firstY + transformClick.getY()) / 2);
                model.updateShape(new Ellipse(currentColor, center, width, height), 0);
                System.out.println("Ellipse Made!");

                break;
            case LINE:
                Point2D.Double newPoint = new Point2D.Double(transformClick.getX(), transformClick.getY());
                Point2D.Double start = new Point2D.Double(firstX, firstY);
                model.updateShape(new Line(currentColor, start, newPoint), 0);
                System.out.println("Line Made!");
                break;

            case RECTANGLE:
                height = Math.abs(transformClick.getY() - firstY);
                width = Math.abs(transformClick.getX() - firstX);
                centerX = (firstX + transformClick.getX()) / 2;
                centerY = (firstY + transformClick.getY()) / 2;
                model.updateShape(new Rectangle(currentColor, new Point2D.Double(centerX, centerY), width, height), 0);
                System.out.println("Rectangle Made!");
                break;

            case SQUARE:

                double size = Math.min(Math.abs(transformClick.getX() - firstX), Math.abs(transformClick.getY() - firstY));

                centerX = (firstX + transformClick.getX()) / 2;
                centerY = (firstY + transformClick.getY()) / 2;
                //this keeps the beginning x coordinate at the same position.
                if (centerX < ((firstX - size / 2))) {
                    centerX = (firstX - size / 2);
                }
                else if (centerX > (firstX + size / 2)) {
                    centerX = firstX + size / 2;
                }
                //this keeps the beginning y coordinate at the same position.
                if (centerY < ((firstY - size / 2))) {
                    centerY = (firstY - size / 2);
                }
                else if (centerY > (firstY + size / 2)) {
                    centerY = firstY + size / 2;
                }

                model.updateShape(new Square(currentColor, new Point2D.Double(centerX, centerY), size), 0);
                System.out.println("Square Made!");
                break;

            case TRIANGLE:

                break;
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }
}
