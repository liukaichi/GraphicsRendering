package cs355.controller;

/**
 * Created by liukaichi on 11/21/2015.
 */
public class WorldToCameraInput
{
    private double totalX = 35;
    private double totalY = -1.5;
    private double totalZ = -15;
    private float totalRotation = 90;

    public WorldToCameraInput(double totalX, double totalY, double totalZ, float totalRotation)
    {
        this.totalX = totalX;
        this.totalY = totalY;
        this.totalZ = totalZ;
        this.totalRotation = totalRotation;
    }

    public double getTotalX()
    {
        return totalX;
    }

    public double getTotalY()
    {
        return totalY;
    }

    public double getTotalZ()
    {
        return totalZ;
    }

    public float getTotalRotation()
    {
        return totalRotation;
    }
}
