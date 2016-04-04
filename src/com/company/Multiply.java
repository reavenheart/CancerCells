package com.company;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by Denis on 04.04.2016.
 */
public class Multiply {

    public BufferedImage calculate(BufferedImage image1, BufferedImage image2) {
        BufferedImage result = new BufferedImage(image1.getWidth(), image1.getHeight(), image1.getType());

        for (int i = 0; i < image1.getHeight(); i++)
        {
            for (int j = 0; j < image1.getWidth(); j++)
            {
                Color c1 = new Color(image1.getRGB(j,i));
                Color c2 = new Color(image2.getRGB(j,i));
                float red = c1.getRed() * c2.getRed() / 255f / 255f;
                red = red > 1 ? 1 : red < 0 ? 0 : red;

                float green = c1.getGreen() * c2.getGreen() / 255f / 255f;
                green = green > 1 ? 1 : green < 0 ? 0 : green;

                float blue = c1.getBlue() * c2.getBlue() / 255f / 255f;
                blue = blue > 1 ? 1 : blue < 0 ? 0 : blue;

                result.setRGB(j,i, new Color(red, green, blue).getRGB());
            }
        }

        return result;
    }

}
