package com.company;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by Denis on 11.04.2015.
 */
public class Binarization {
    public BufferedImage countBinarization(BufferedImage image, int boundary) {

        BufferedImage result = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());


        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                Color color = new Color(image.getRGB(x, y));
                //TODO change on different channels
                if ((color.getRed() + color.getGreen() + color.getBlue())/3 >= boundary)
                    result.setRGB(x,y,new Color(255,255,255,255).getRGB());
                else
                    result.setRGB(x,y,new Color(0,0,0,255).getRGB());
            }
        }

        return result;
    }
}
