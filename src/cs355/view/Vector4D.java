package cs355.view;

import cs355.model.scene.Point3D;

import java.awt.geom.Point2D;

/**
 * Created by liukaichi on 11/20/2015.
 */
public class Vector4D
{
    final static double DEFAULT_FOCAL_LENGTH = 1;

    double x;
    double y;
    double z;
    double w = DEFAULT_FOCAL_LENGTH;

    public Vector4D(Point3D point)
    {
        this.x = point.x;
        this.y = point.y;
        this.z = point.z;
    }

    public Vector4D(double x, double y, double z, double w)
    {
        this.x = x;
        this.y = y;
        this.z = -z;
        this.w = w;
    }

    public double getX()
    {
        return x;
    }

    public double getY()
    {
        return y;
    }

    public double getZ()
    {
        return z;
    }

    public double getW()
    {
        return w;
    }

    public boolean isInsideOutsideClip()
    {
        //near clip

        if ((x/w >= -1 && x/w <= 1) && (y/w >= -1 && y/w <= 1))
        {
            return true;
        }

        return false;
    }

    public boolean isInsideNearClips()
    {
        if (z/w >= -1 && z/w <= 1)
        {
            return true;
        }
        return false;
    }

    public Point2D generateClipToScreen(double width, double height)
    {
        double screenX = (width / 2) * (x / w) + (width / 2);
        double screenY = (-height / 2) * (y / w) + (height / 2);
        return new Point2D.Double(screenX, screenY);
    }
}
