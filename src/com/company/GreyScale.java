package com.company;

/**
 * Created by Denis on 02.04.2015.
 */
import java.awt.*;
import java.awt.image.BufferedImage;

import java.io.*;
import java.sql.Time;
import java.util.Calendar;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

public class GreyScale {

    int width;
    int height;

    public GreyScale() {
    }

    public BufferedImage countGreyScale(BufferedImage  image) {

        width = image.getWidth();
        height = image.getHeight();

        BufferedImage result = new BufferedImage(width, height, image.getType());

        Calendar cal1 = Calendar.getInstance();
        System.out.println(cal1.getTime());

        for (int i = 0; i < height; i++) {

            for (int j = 0; j < width; j++) {

                Color c = new Color(image.getRGB(j, i));
                //int red = (int) (c.getRed() * 0.299);
                //int green = (int) (c.getGreen() * 0.587);
                //int blue = (int) (c.getBlue() * 0.114);

                int red = (c.getRed() + c.getGreen() + c.getBlue())/3;
                if (red > 255)
                    red = 255;

                Color newColor = new Color(red, red, red);

                result.setRGB(j, i, newColor.getRGB());
            }
        }

        Calendar cal2 = Calendar.getInstance();
        System.out.println(cal2.getTime());

        return result;
    }
}
