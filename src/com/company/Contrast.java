package com.company;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by Denis on 04.04.2016.
 */
public class Contrast {

    int width;
    int height;

    public BufferedImage countContrast(BufferedImage image, float factor) {
        width = image.getWidth();
        height = image.getHeight();

        int maxValue = new AutoLevels().getMaxValue(image, 3);
        int minValue = new AutoLevels().getMinValue(image, 3);

        float factor2 = (maxValue - minValue) * factor;

        float newMaxValue = 255;
        float newMinValue = -newMaxValue * factor;

        //float a = (newMaxValue - newMinValue)/(maxValue - minValue);
        //float b = newMinValue - a * minValue;

        //factor = (259 * (contrast + 255)) / (255 * (259 - contrast));

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                Color c = new Color(image.getRGB(j,i));
                //float red = newMinValue * (float)Math.pow(newMaxValue/newMinValue, (c.getRed() - minValue)/(maxValue - minValue));
                float red = (newMaxValue - newMinValue) * (c.getRed() - minValue)/(maxValue - minValue) + newMinValue;
                red = red > 255 ? 255 : red < 0 ? 0 : red;

                Color newColor = new Color((int)red,(int)red,(int)red);
                image.setRGB(j,i, newColor.getRGB());
            }
        }

        return image;
    }

    /*
    private int SetNewContrast(Color color, float a, float b) {
        float red = a * color.getRed() + b;
        red = red > 255 ? 255 : red < 0 ? 0 : red;

        float green = a * color.getGreen() + b;
        green = green > 255 ? 255 : green < 0 ? 0 : green;

        float blue = a * color.getBlue() + b;
        blue = blue > 255 ? 255 : blue < 0 ? 0 : blue;

        return new Color((int)red, (int)green, (int)blue).getRGB();
    }
    */

}
