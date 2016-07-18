package cs355.model;

import cs355.model.drawing.CS355Drawing;
import cs355.model.drawing.Shape;
import cs355.model.image.Image;
import cs355.model.scene.HouseModel;
import cs355.model.scene.Instance;
import cs355.model.scene.WireFrame;
import cs355.view.DrawableShape;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by liukaichi on 9/3/2015.
 */
public class Model extends CS355Drawing
{

    List<Shape> shapes = new ArrayList<>();
    List<java.awt.Shape> selectionShapes = new ArrayList<>();
    Shape selectedShape = null;
    Image image = new Image();
    BufferedImage backgroundImage = null;

    //3D Stuff
    //ArrayList<Instance> instances = new ArrayList<>();
    ArrayList<WireFrame> shapes3D = new ArrayList<>();

    public Model(){
        shapes3D.add(new HouseModel());
        shapes3D.add(new HouseModel());
        shapes3D.add(new HouseModel());
        shapes3D.add(new HouseModel());
        shapes3D.add(new HouseModel());
        shapes3D.add(new HouseModel());
    }

    @Override
    public Shape getShape(int index) {
        return shapes.get(index);
    }

    @Override
    public int addShape(Shape s) {
        shapes.add(0, s);
        System.out.println("Notifying observers");
        setChanged();
        notifyObservers();
        return 0;
    }

    @Override
    public void deleteShape(int index) {
        shapes.remove(index);
        //setChanged();
        //notifyObservers();
    }

    public void updateShape(Shape s, int index){
        shapes.remove(index);
        shapes.add(index, s);
        setChanged();
    notifyObservers();
}

    @Override
    public void moveToFront(int index) {
        //takes a sublist from 0 to the index and rotates them all up 1 index
        Collections.rotate(shapes.subList(0, index + 1), 1);
        setChanged();
        notifyObservers();

    }

    @Override
    public void movetoBack(int index) {
        //takes a sublist from the index to the end of the list and rotates them all down 1 index
        Collections.rotate(shapes.subList(index, shapes.size()), -1);
        setChanged();
        notifyObservers();
    }

    @Override
    public void moveForward(int index) {

        if (index > 0) {
            Collections.swap(shapes, index, index - 1);
        }
        setChanged();
        notifyObservers();
    }

    @Override
    public void moveBackward(int index) {
        if (index < shapes.size() - 1) {
            Collections.swap(shapes, index, index + 1);
        }
        setChanged();
        notifyObservers();
    }

    @Override
    public List<Shape> getShapes() {
        return shapes;
    }

    @Override
    public List<Shape> getShapesReversed() {
        Collections.reverse(shapes);
        ArrayList<Shape> temp = new ArrayList<>(shapes);
        Collections.reverse(shapes);
        return temp;
    }

    @Override
    public void setShapes(List<Shape> shapes) {
        this.shapes = shapes;
        setChanged();
        notifyObservers();
    }

    @Override
    public void addInstance(Instance instance) {
        /*System.out.println("Added an instance!");
        instances.add(instance);
        setChanged();
        notifyObservers();*/
    }

    public List<java.awt.Shape> getSelectionShapes()
    {
        return selectionShapes;
    }

    public void setSelectedShape(Shape selectedShape)
    {
        this.selectedShape = selectedShape;
        selectionShapes = DrawableShape.GenerateSelectionShapes(this.selectedShape);
        setChanged();
        notifyObservers();
    }
    public Shape getSelectedShape(){
        return selectedShape;
    }

    public ArrayList<WireFrame> getShapes3D()
    {
        return shapes3D;
    }

    public Image getImage()
    {

        return image;
    }

    public BufferedImage getBackgroundImage()
    {
        return backgroundImage;
    }

    public void resetBackgroundImage()
    {
        backgroundImage = image.getImage();
    }
}
