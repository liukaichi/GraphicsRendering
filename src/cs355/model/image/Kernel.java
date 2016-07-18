package cs355.model.image;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by liukaichi on 12/7/2015.
 */
public class Kernel
{
    int topLeft[];
    int topCenter[];
    int topRight[];
    int middleLeft[];
    int middleCenter[];
    int middleRight[];
    int bottomLeft[];
    int bottomCenter[];
    int bottomRight[];

    final int SHARPEN_CONSTANT = 2;

    boolean isEdgeCase;

    public Kernel(Image image, int x, int y)
    {
        if (y == 0 || y == image.getHeight() - 1 || x == 0 || x == image.getWidth() - 1)
        {
            isEdgeCase = true;
            return;
        }
        isEdgeCase = false;

        topLeft = new int[3];
        topCenter = new int[3];
        topRight = new int[3];
        middleLeft = new int[3];
        middleCenter = new int[3];
        middleRight = new int[3];
        bottomLeft = new int[3];
        bottomCenter = new int[3];
        bottomRight = new int[3];

        topLeft = image.getPixel(x - 1, y - 1, topLeft);
        topCenter = image.getPixel(x, y - 1, topCenter);
        topRight = image.getPixel(x + 1, y - 1, topRight);

        middleLeft = image.getPixel(x - 1, y, middleLeft);
        middleCenter = image.getPixel(x, y, middleCenter);
        middleRight = image.getPixel(x + 1, y, middleRight);

        bottomLeft = image.getPixel(x - 1, y + 1, bottomLeft);
        bottomCenter = image.getPixel(x, y + 1, bottomCenter);
        bottomRight = image.getPixel(x + 1, y + 1, bottomRight);
    }

    public int[] uniformBlur()
    {

        if (isEdgeCase)
        {
            return new int[3];
        }

        int result[] = new int[3];

        for (int i = 0; i < 3; i++)
        {
            double mean = 0;
            mean += topLeft[i];
            mean += topCenter[i];
            mean += topRight[i];
            mean += middleLeft[i];
            mean += middleCenter[i];
            mean += middleRight[i];
            mean += bottomLeft[i];
            mean += bottomCenter[i];
            mean += bottomRight[i];

            mean /= 9;
            result[i] = (int) mean;
        }
        return result;
    }

    public int[] medianBlur()
    {
        if (isEdgeCase)
        {
            return new int[3];
        }

        int medianColor[] = new int[3];

        for (int i = 0; i < 3; i++)
        {
            ArrayList<Integer> list = new ArrayList<>();
            list.add(topLeft[i]);
            list.add(topCenter[i]);
            list.add(topRight[i]);
            list.add(middleLeft[i]);
            list.add(middleCenter[i]);
            list.add(middleRight[i]);
            list.add(bottomLeft[i]);
            list.add(bottomCenter[i]);
            list.add(bottomRight[i]);
            Collections.sort(list);
            medianColor[i] = list.get(list.size() / 2 + 1);
        }

        return getClosestLeastSquaresMatch(medianColor);
    }

    private int[] getClosestLeastSquaresMatch(int median[])
    {
        double smallestDifference = 100000;
        int winner[] = new int[3];
        ArrayList<int[]> list = new ArrayList<>();
        list.add(topLeft);
        list.add(topCenter);
        list.add(topRight);
        list.add(middleLeft);
        list.add(middleCenter);
        list.add(middleRight);
        list.add(bottomLeft);
        list.add(bottomCenter);
        list.add(bottomRight);
        for (int[] color : list)
        {
            double first = Math.pow((double)(color[0] - median[0]), 2);
            double second = Math.pow((double)(color[1] - median[1]), 2);
            double third = Math.pow((double)(color[2] - median[2]), 2);
            double difference = Math.sqrt(first + second + third);
            if (difference < smallestDifference)
            {
                winner = color;
                smallestDifference = difference;
            }
        }
        return winner;
    }

    public int[] sharpen()
    {
        if (isEdgeCase)
        {
            return new int[3];
        }

        int result[] = new int[3];

        for (int i = 0; i < 3; i++)
        {
            double total = 0;
            total += topCenter[i] * -1;
            total += middleLeft[i] * -1;
            total += middleCenter[i] * (4 + SHARPEN_CONSTANT);
            total += middleRight[i] * -1;
            total += bottomCenter[i] * -1;

            total /= SHARPEN_CONSTANT;

            if (total < 0) total = 0;
            else if (total > 255) total = 255;
            result[i] = (int)total;
        }
        return result;
    }

    public int[] edgeDetection()
    {
        if (isEdgeCase)
        {
            return new int[3];
        }

        int result[] = new int[3];

        double gradientMagnitude = Math.sqrt(Math.pow(convolveX(), 2) + Math.pow(convolveY(), 2));
        gradientMagnitude *= 255;
        result[0] = (int)gradientMagnitude;
        result[1] = (int)gradientMagnitude;
        result[2] = (int)gradientMagnitude;

        return result;
    }


    private double convolveX()
    {
        float topLeftHSB[] = new float[3];
        Color.RGBtoHSB(topLeft[0], topLeft[1], topLeft[2], topLeftHSB);

        float middleLeftHSB[] = new float[3];
        Color.RGBtoHSB(middleLeft[0], middleLeft[1], middleLeft[2], middleLeftHSB);

        float bottomLeftHSB[] = new float[3];
        Color.RGBtoHSB(bottomLeft[0], bottomLeft[1], bottomLeft[2], bottomLeftHSB);

        float topRightHSB[] = new float[3];
        Color.RGBtoHSB(topRight[0], topRight[1], topRight[2], topRightHSB);

        float middleRightHSB[] = new float[3];
        Color.RGBtoHSB(middleRight[0], middleRight[1], middleRight[2], middleRightHSB);

        float bottomRightHSB[] = new float[3];
        Color.RGBtoHSB(bottomRight[0], bottomRight[1], bottomRight[2], bottomRightHSB);

        double total = 0;
        total -= topLeftHSB[2];
        total -= 2 * middleLeftHSB[2];
        total -= bottomLeftHSB[2];
        total += topRightHSB[2];
        total += 2 * middleRightHSB[2];
        total += bottomRightHSB[2];

        return total / 8;
    }

    private double convolveY()
    {
        float topLeftHSB[] = new float[3];
        Color.RGBtoHSB(topLeft[0], topLeft[1], topLeft[2], topLeftHSB);

        float topCenterHSB[] = new float[3];
        Color.RGBtoHSB(topCenter[0], topCenter[1], topCenter[2], topCenterHSB);

        float topRightHSB[] = new float[3];
        Color.RGBtoHSB(topRight[0], topRight[1], topRight[2], topRightHSB);

        float bottomLeftHSB[] = new float[3];
        Color.RGBtoHSB(bottomLeft[0], bottomLeft[1], bottomLeft[2], bottomLeftHSB);

        float bottomCenterHSB[] = new float[3];
        Color.RGBtoHSB(bottomCenter[0], bottomCenter[1], bottomCenter[2], bottomCenterHSB);

        float bottomRightHSB[] = new float[3];
        Color.RGBtoHSB(bottomRight[0], bottomRight[1], bottomRight[2], bottomRightHSB);

        double total = 0;
        total -= topLeftHSB[2];
        total -= 2 * topCenterHSB[2];
        total -= topRightHSB[2];
        total += bottomLeftHSB[2];
        total += 2 * bottomCenterHSB[2];
        total += bottomRightHSB[2];

        return total / 8;
    }
}
