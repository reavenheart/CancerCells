package com.company;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Calendar;

/**
 * Created by Denis on 05.04.2015.
 */
public class AutoLevels {

    int width;
    int height;

    public BufferedImage countLevels(BufferedImage image) {

        width = image.getWidth();
        height = image.getHeight();

        int r = 0,g = 0, b = 0;
        for (int i = 0; i < height; i++)
        {
            for (int j = 0; j < width; j++)
            {
                Color c = new Color(image.getRGB(j,i));
                if (r < c.getRed())
                    r = c.getRed();
                if (g < c.getGreen())
                    g = c.getGreen();
                if (b < c.getBlue())
                    b = c.getBlue();
                //tmpR[i, j] = new Pixel();
            }
        }

        countAutoLevels(r, g, b, image);

        return image;
    }

    private void countAutoLevels(int r, int g, int b, BufferedImage image) {

        double percentR, percentG, percentB;

        percentR = r * 100 / 255;
        percentR = (100 - percentR)/100;

        percentG = g * 100 / 255;
        percentG = (100 - percentG)/100;

        percentB = b * 100 / 255;
        percentB = (100 - percentB)/100;

        for (int i = 0; i < height; i++)
        {
            for (int j = 0; j < width; j++)
            {
                Color c = new Color(image.getRGB(j,i));
                r = (int)(c.getRed() + c.getRed() * percentR);
                if (r > 255)
                    r = 255;
                else if (r < 0)
                    r = 0;

                g = (int)(c.getGreen() + c.getGreen() * percentG);
                if (g > 255)
                    g = 255;
                else if (g < 0)
                    g = 0;

                b = (int)(c.getBlue() + c.getBlue() * percentB);
                if (b > 255)
                    b = 255;
                else if (b < 0)
                    b = 0;

                image.setRGB(j, i, new Color(r, g, b).getRGB());
            }
        }

        Calendar cal2 = Calendar.getInstance();
        System.out.println(cal2.getTime());
    }

    public void cutBackground(BufferedImage image) {

        int minPercent = 50;

        width = image.getWidth();
        height = image.getHeight();

        float[] red = new float[256];
        float[] green = new float[256];
        float[] blue = new float[256];

        for (int i = 0; i < height; i++)
        {
            for (int j = 0; j < width; j++)
            {
                Color c = new Color(image.getRGB(j,i));
                red[c.getRed()]++;
                green[c.getGreen()]++;
                blue[c.getBlue()]++;
            }
        }

        int maxNumberRed = 0;
        int maxColorRed = 0;
        int maxNumberGreen = 0;
        int maxColorGreen = 0;
        int maxNumberBlue = 0;
        int maxColorBlue = 0;

        for (int i = 0; i < red.length; i++) {
            if (maxNumberRed < red[i]) {
                maxNumberRed = (int) red[i];
                maxColorRed = i;
            }
            if (maxNumberGreen < green[i]) {
                maxNumberGreen = (int) green[i];
                maxColorGreen = i;
            }
            if (maxNumberBlue < blue[i]) {
                maxNumberBlue = (int) blue[i];
                maxColorBlue = i;
            }
        }

        for (int i = 0; i < red.length; i++) {
            red[i] = red[i] * 100 / maxNumberRed;
            green[i] = green[i] * 100 / maxNumberGreen;
            blue[i] = blue[i] * 100 / maxNumberBlue;
        }

        Calendar cal2 = Calendar.getInstance();
        System.out.println(cal2.getTime());

        int minAcceptableRed = 0;
        int minAcceptableGreen = 0;
        int minAcceptableBlue = 0;

        for (int i = maxColorRed; i < red.length; i++) {
            if (red[i] > minPercent)
                minAcceptableRed = i;
        }

        for (int i = maxColorGreen; i < green.length; i++) {
            if (green[i] > minPercent)
                minAcceptableGreen = i;
        }

        for (int i = maxColorBlue; i < blue.length; i++) {
            if (blue[i] > minPercent)
                minAcceptableBlue = i;
        }

        for (int i = 0; i < height; i++)
        {
            for (int j = 0; j < width; j++)
            {
                Color c = new Color(image.getRGB(j,i));
                Color newColor = new Color(c.getRed(), c.getGreen(), c.getGreen(), c.getAlpha());

                if (c.getRed() < minAcceptableRed)
                    newColor = new Color(0, newColor.getGreen(), newColor.getGreen(), newColor.getAlpha());
                if (c.getGreen() < minAcceptableGreen)
                    newColor = new Color(newColor.getRed(), 0, newColor.getGreen(), newColor.getAlpha());
                if (c.getBlue() < minAcceptableBlue)
                    newColor = new Color(newColor.getRed(), newColor.getGreen(), 0, newColor.getAlpha());

                image.setRGB(j,i, newColor.getRGB());
            }
        }
    }

    public void cutBackgroundWithLevels(BufferedImage image, int minPercent) {

        width = image.getWidth();
        height = image.getHeight();

        float[] red = new float[256];
        float[] green = new float[256];
        float[] blue = new float[256];

        for (int i = 0; i < height; i++)
        {
            for (int j = 0; j < width; j++)
            {
                Color c = new Color(image.getRGB(j,i));
                red[c.getRed()]++;
                green[c.getGreen()]++;
                blue[c.getBlue()]++;
            }
        }

        int maxNumberRed = 0;
        int maxColorRed = 0;
        int maxNumberGreen = 0;
        int maxColorGreen = 0;
        int maxNumberBlue = 0;
        int maxColorBlue = 0;

        for (int i = 0; i < red.length; i++) {
            if (maxNumberRed < red[i]) {
                maxNumberRed = (int) red[i];
                maxColorRed = i;
            }
            if (maxNumberGreen < green[i]) {
                maxNumberGreen = (int) green[i];
                maxColorGreen = i;
            }
            if (maxNumberBlue < blue[i]) {
                maxNumberBlue = (int) blue[i];
                maxColorBlue = i;
            }
        }

        for (int i = 0; i < red.length; i++) {
            red[i] = red[i] * 100 / maxNumberRed;
            green[i] = green[i] * 100 / maxNumberGreen;
            blue[i] = blue[i] * 100 / maxNumberBlue;
        }

        Calendar cal2 = Calendar.getInstance();
        System.out.println(cal2.getTime());

        int minAcceptableRed = 0;
        int minAcceptableGreen = 0;
        int minAcceptableBlue = 0;

        for (int i = maxColorRed; i < red.length; i++) {
            if (red[i] > minPercent)
                minAcceptableRed = i;
        }

        for (int i = maxColorGreen; i < green.length; i++) {
            if (green[i] > minPercent)
                minAcceptableGreen = i;
        }

        for (int i = maxColorBlue; i < blue.length; i++) {
            if (blue[i] > minPercent)
                minAcceptableBlue = i;
        }

        for (int i = 0; i < height; i++)
        {
            for (int j = 0; j < width; j++)
            {
                Color c = new Color(image.getRGB(j,i));
                Color newColor = new Color(c.getRed(), c.getGreen(), c.getGreen(), c.getAlpha());

                if (c.getRed() < minAcceptableRed)
                    newColor = new Color(0, newColor.getGreen(), newColor.getGreen(), newColor.getAlpha());
                if (c.getGreen() < minAcceptableGreen)
                    newColor = new Color(newColor.getRed(), 0, newColor.getGreen(), newColor.getAlpha());
                if (c.getBlue() < minAcceptableBlue)
                    newColor = new Color(newColor.getRed(), newColor.getGreen(), 0, newColor.getAlpha());

                image.setRGB(j,i, newColor.getRGB());
            }
        }

        for (int i = 0; i < height; i++)
        {
            for (int j = 0; j < width; j++)
            {
                Color c = new Color(image.getRGB(j,i));
                Color newColor = new Color(c.getRed(), c.getGreen(), c.getGreen(), c.getAlpha());

                if (c.getRed() != 0)
                    newColor = new Color(newColor.getRed()-minAcceptableRed, newColor.getGreen(), newColor.getBlue(), newColor.getAlpha());
                if (c.getGreen() != 0)
                    newColor = new Color(newColor.getRed(), newColor.getGreen()-minAcceptableGreen, newColor.getBlue(), newColor.getAlpha());
                if (c.getBlue() != 0)
                    newColor = new Color(newColor.getRed(), newColor.getGreen(), newColor.getBlue()-minAcceptableBlue, newColor.getAlpha());

                image.setRGB(j,i, newColor.getRGB());
            }
        }

        image = countLevels(image);
    }

    /**
     *
     * @param image
     * @param channel 0=red, 1=green, 2-blue, 3=rgb
     * @return maxValue
     */
    public int getMaxValue(BufferedImage image, int channel) {
        width = image.getWidth();
        height = image.getHeight();

        int maxValue = 0;

        for (int i = 0; i < height; i++)
        {
            for (int j = 0; j < width; j++)
            {
                Color c = new Color(image.getRGB(j,i));

                if (channel == 0)
                    if (c.getRed() > maxValue)
                        maxValue = c.getRed();
                if (channel == 1)
                    if (c.getGreen() > maxValue)
                        maxValue = c.getGreen();
                if (channel == 2)
                    if (c.getBlue() > maxValue)
                        maxValue = c.getBlue();
                if (channel == 3)
                    if ((c.getRed() + c.getGreen() + c.getBlue())/3 > maxValue)
                        maxValue = (c.getRed() + c.getGreen() + c.getBlue())/3;
            }
        }

        return maxValue;
    }

    /**
     *
     * @param image
     * @param channel 0=red, 1=green, 2-blue, 3=rgb
     * @return minValue
     */
    public int getMinValue(BufferedImage image, int channel) {
        width = image.getWidth();
        height = image.getHeight();

        int minValue = 0;

        for (int i = 0; i < height; i++)
        {
            for (int j = 0; j < width; j++)
            {
                Color c = new Color(image.getRGB(j,i));

                if (channel == 0)
                    if (c.getRed() < minValue)
                        minValue = c.getRed();
                if (channel == 1)
                    if (c.getGreen() < minValue)
                        minValue = c.getGreen();
                if (channel == 2)
                    if (c.getBlue() < minValue)
                        minValue = c.getBlue();
                if (channel == 3)
                    if ((c.getRed() + c.getGreen() + c.getBlue())/3 < minValue)
                        minValue = (c.getRed() + c.getGreen() + c.getBlue())/3;
            }
        }

        return minValue;
    }
}
