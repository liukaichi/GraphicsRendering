package cs355.view;

import cs355.controller.WorldToCameraInput;
import cs355.model.scene.Point3D;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by liukaichi on 11/20/2015.
 */
public class Matrix4D
{
    List<Double> x = new ArrayList<>(4);
    List<Double> y = new ArrayList<>(4);
    List<Double> z = new ArrayList<>(4);
    List<Double> w = new ArrayList<>(4);

    public Matrix4D()
    {

    }

    public Matrix4D(Point3D point)
    {

    }

    public Matrix4D(List<Double> x, List<Double> y, List<Double> z, List<Double> w)
    {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    private static Matrix4D generateRotationMatrix(float rotation)
    {
        rotation = (float)Math.toRadians(rotation);

        Double arrayX[] = new Double[4];
        Arrays.fill(arrayX, 0.0);
        List<Double> rotateX = Arrays.asList(arrayX);
        rotateX.set(0, Math.cos(rotation));
        rotateX.set(2, Math.sin(rotation));

        Double arrayY[] = new Double[4];
        Arrays.fill(arrayY, 0.0);
        List<Double> rotateY = Arrays.asList(arrayY);
        rotateY.set(1, 1.0);

        Double arrayZ[] = new Double[4];
        Arrays.fill(arrayZ, 0.0);
        List<Double> rotateZ = Arrays.asList(arrayZ);
        rotateZ.set(0, -Math.sin(rotation));
        rotateZ.set(2, Math.cos(rotation));

        Double arrayW[] = new Double[4];
        Arrays.fill(arrayW, 0.0);
        List<Double> rotateW = Arrays.asList(arrayW);
        rotateW.set(3, 1.0);

        return new Matrix4D(rotateX, rotateY, rotateZ, rotateW);
    }

    private static Matrix4D generateTranslationMatrix(double x, double y, double z)
    {
        Double arrayX[] = new Double[4];
        Arrays.fill(arrayX, 0.0);
        List<Double> translateX = Arrays.asList(arrayX);
        translateX.set(3, x);
        translateX.set(0, 1.0);

        Double arrayY[] = new Double[4];
        Arrays.fill(arrayY, 0.0);
        List<Double> translateY = Arrays.asList(arrayY);
        translateY.set(3, y);
        translateY.set(1, 1.0);

        Double arrayZ[] = new Double[4];
        Arrays.fill(arrayZ, 0.0);
        List<Double> translateZ = Arrays.asList(arrayZ);
        translateZ.set(3, z);
        translateZ.set(2, 1.0);

        Double arrayW[] = new Double[4];
        Arrays.fill(arrayW, 0.0);
        List<Double> translateW = Arrays.asList(arrayW);
        translateW.set(3, 1.0);

        return new Matrix4D(translateX, translateY, translateZ, translateW);
    }

    public static Matrix4D matrixMultiply(Matrix4D A, Matrix4D B)
    {
        List<Double> rowX = Arrays.asList(new Double[4]);
        for (int i = 0; i < 4; i++)
        {
            //rowX.set(0, 1.0);
            rowX.set(i, rowMultiply(A.getX(), B.getX().get(i), B.getY().get(i), B.getZ().get(i), B.getW().get(i)));
        }
        List<Double> rowY = Arrays.asList(new Double[4]);
        for (int i = 0; i < 4; i++)
        {
            rowY.set(i, rowMultiply(A.getY(), B.getX().get(i), B.getY().get(i), B.getZ().get(i), B.getW().get(i)));
        }
        List<Double> rowZ = Arrays.asList(new Double[4]);
        for (int i = 0; i < 4; i++)
        {
            rowZ.set(i, rowMultiply(A.getZ(), B.getX().get(i), B.getY().get(i), B.getZ().get(i), B.getW().get(i)));
        }
        List<Double> rowW = Arrays.asList(new Double[4]);
        for (int i = 0; i < 4; i++)
        {
            rowW.set(i, rowMultiply(A.getW(), B.getX().get(i), B.getY().get(i), B.getZ().get(i), B.getW().get(i)));
        }
        return new Matrix4D(rowX, rowY, rowZ, rowW);
    }

    public Vector4D transformVector(Vector4D vector)
    {
        double newX = rowMultiply(x, vector);
        double newY = rowMultiply(y, vector);
        double newZ = rowMultiply(z, vector);
        double newW = rowMultiply(w, vector);
        return new Vector4D(newX, newY, newZ, newW);
    }

    public double rowMultiply(List<Double> row, Vector4D vector)
    {
        return (row.get(0) * vector.getX()) + (row.get(1) * vector.getY()) + (row.get(2) * vector.getZ()) + (row.get(3)
                * vector.getW());
    }

    public static double rowMultiply(List<Double> row, double x, double y, double z, double w)
    {
        return (row.get(0) * x) + (row.get(1) * y) + (row.get(2) * z) + (row.get(3) * w);
    }

    public static Matrix4D generateWorldToCamera(WorldToCameraInput input)
    {
        Matrix4D translationMatrix = generateTranslationMatrix(-input.getTotalX(), -input.getTotalY(),
                -input.getTotalZ());
        Matrix4D rotationMatrix = generateRotationMatrix(input.getTotalRotation());

        return matrixMultiply(rotationMatrix, translationMatrix);
    }

    public static Matrix4D generateClipMatrix(double fieldOfView, double aspectRatio, double near, double far)
    {
        Double arrayX[] = new Double[4];
        Arrays.fill(arrayX, 0.0);
        List<Double> translateX = Arrays.asList(arrayX);
        //translateX.set(0, -(2 * near * far) / (far - near)fieldOfView * aspectRatio);
        translateX.set(0, fieldOfView * aspectRatio);

        Double arrayY[] = new Double[4];
        Arrays.fill(arrayY, 0.0);
        List<Double> translateY = Arrays.asList(arrayY);
        //translateY.set(1, near / fieldOfView);
        translateY.set(1, near / fieldOfView);

        Double arrayZ[] = new Double[4];
        Arrays.fill(arrayZ, 0.0);
        List<Double> translateZ = Arrays.asList(arrayZ);
        translateZ.set(2, (far + near) / (far - near));
        translateZ.set(3, -(2 * near * far) / (far - near));

        Double arrayW[] = new Double[4];
        Arrays.fill(arrayW, 0.0);
        List<Double> translateW = Arrays.asList(arrayW);
        translateW.set(2, 1.);

        return new Matrix4D(translateX, translateY, translateZ, translateW);
    }

    public void setX(ArrayList<Double> x)
    {
        this.x = x;
    }

    public void setY(ArrayList<Double> y)
    {
        this.y = y;
    }

    public void setZ(ArrayList<Double> z)
    {
        this.z = z;
    }

    public void setW(ArrayList<Double> w)
    {
        this.w = w;
    }

    public List<Double> getX()
    {
        return x;
    }

    public List<Double> getY()
    {
        return y;
    }

    public List<Double> getZ()
    {
        return z;
    }

    public List<Double> getW()
    {
        return w;
    }
}
