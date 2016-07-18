package cs355.model.image;

import cs355.model.scene.CS355Scene;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.awt.image.Raster;

/**
 * Created by liukaichi on 12/5/2015.
 */
public class Image extends CS355Image
{
    public Image()
    {
        super();
    }
    public Image(int width, int height)
    {
        super(width, height);
    }

    @Override public BufferedImage getImage()
    {
        BufferedImage bi = new BufferedImage(this.getWidth(), this.getHeight(), BufferedImage.TYPE_INT_ARGB);
        //final int[] a = ( (DataBufferInt) bi.getRaster().getDataBuffer()).getData();
        for (int x = 0; x < this.getWidth(); x++)
        {
            for (int y = 0; y < this.getHeight(); y++)
            {
                int RGB = new Color(getRed(x, y), getGreen(x, y), getBlue(x, y)).getRGB();
                bi.setRGB(x, y, RGB);
            }
        }

        return bi;
    }

    @Override public void edgeDetection()
    {
        Image temp = new Image(getWidth(), getHeight());
        for (int x = 0; x < this.getWidth(); x++)
        {
            for (int y = 0; y < this.getHeight(); y++)
            {
                Kernel kernel = new Kernel(this, x, y);
                temp.setPixel(x,y,kernel.edgeDetection());
            }
        }
        setPixels(temp);
    }

    @Override public void sharpen()
    {
        Image temp = new Image(getWidth(), getHeight());
        for (int x = 0; x < this.getWidth(); x++)
        {
            for (int y = 0; y < this.getHeight(); y++)
            {
                Kernel kernel = new Kernel(this, x, y);
                temp.setPixel(x,y,kernel.sharpen());
            }
        }
        setPixels(temp);
    }

    @Override public void medianBlur()
    {
        Image temp = new Image(getWidth(), getHeight());
        for (int x = 0; x < this.getWidth(); x++)
        {
            for (int y = 0; y < this.getHeight(); y++)
            {
                Kernel kernel = new Kernel(this, x, y);
                temp.setPixel(x,y,kernel.medianBlur());
            }
        }
        setPixels(temp);
    }

    @Override public void uniformBlur()
    {
        Image temp = new Image(getWidth(), getHeight());
        for (int x = 0; x < this.getWidth(); x++)
        {
            for (int y = 0; y < this.getHeight(); y++)
            {
                Kernel kernel = new Kernel(this, x, y);
                temp.setPixel(x,y,kernel.uniformBlur());
            }
        }
        setPixels(temp);
    }


    @Override public void grayscale()
    {
        float hsbValues[] = new float[3];
        for (int x = 0; x < this.getWidth(); x++)
        {
            for (int y = 0; y < this.getHeight(); y++)
            {
                Color.RGBtoHSB(getRed(x, y), getGreen(x, y), getBlue(x, y), hsbValues);

                hsbValues[1] = 0;
                Color newColor = Color.getHSBColor(hsbValues[0], hsbValues[1], hsbValues[2]);
                int[] rgb = new int[3];
                rgb[0] = newColor.getRed();
                rgb[1] = newColor.getGreen();
                rgb[2] = newColor.getBlue();

                setPixel(x, y, rgb);

            }
        }


    }

    @Override public void contrast(int amount)
    {
        float hsbValues[] = new float[3];
        double preAmount = amount;
        double adjustedContrast = preAmount / 100;
        for (int x = 0; x < this.getWidth(); x++)
        {
            for (int y = 0; y < this.getHeight(); y++)
            {


                Color.RGBtoHSB(getRed(x, y), getGreen(x, y), getBlue(x, y), hsbValues);

                hsbValues[2] = (float) (Math.pow(((preAmount + 100) / 100), 4.) * (hsbValues[2] - .5)
                        + .5);
                if (hsbValues[2] > 1)
                {
                    hsbValues[2] = 1;
                }
                else if (hsbValues[2] < 0)
                {
                    hsbValues[2] = 0;
                }

                Color newColor = Color.getHSBColor(hsbValues[0], hsbValues[1], hsbValues[2]);
                int[] rgb = new int[3];
                rgb[0] = newColor.getRed();
                rgb[1] = newColor.getGreen();
                rgb[2] = newColor.getBlue();

                setPixel(x, y, rgb);

            }
        }

    }

    @Override public void brightness(int amount)
    {
        float hsbValues[] = new float[3];
        float preAmount = amount;
        float adjustedBrightness = preAmount / 100;
        for (int x = 0; x < this.getWidth(); x++)
        {
            for (int y = 0; y < this.getHeight(); y++)
            {


                Color.RGBtoHSB(getRed(x, y), getGreen(x, y), getBlue(x, y), hsbValues);

                hsbValues[2] = hsbValues[2] + adjustedBrightness;
                if (hsbValues[2] > 1)
                {
                    hsbValues[2] = 1;
                }
                else if (hsbValues[2] < 0)
                {
                    hsbValues[2] = 0;
                }

                Color newColor = Color.getHSBColor(hsbValues[0], hsbValues[1], hsbValues[2]);
                int[] rgb = new int[3];
                rgb[0] = newColor.getRed();
                rgb[1] = newColor.getGreen();
                rgb[2] = newColor.getBlue();

                setPixel(x, y, rgb);

            }
        }

    }
}
