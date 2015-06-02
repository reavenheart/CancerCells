package com.company;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by Denis on 11.04.2015.
 */
public class Invert {

    public BufferedImage invert(BufferedImage image) {

        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                int rgba = image.getRGB(x, y);
                Color col = new Color(rgba, true);
                col = new Color(255 - col.getRed(),
                        255 - col.getGreen(),
                        255 - col.getBlue());
                image.setRGB(x, y, col.getRGB());
            }
        }

        return image;
    }
}
