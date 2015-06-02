package com.company;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Arrays;
import java.util.Calendar;

/**
 * Created by Denis on 03.04.2015.
 */
public class Sobel {

    int width;
    int height;

    double[][] Sobel3x3Horizontal = new double[][]  {
            { -1,  0,  1, },
            { -2,  0,  2, },
            { -1,  0,  1, }
    };

    double[][] Sobel3x3Vertical = new double[][] {
            {  1,  2,  1, },
            {  0,  0,  0, },
            { -1, -2, -1, }
    };

    public BufferedImage countSobel(BufferedImage image) {

        width = image.getWidth();
        height = image.getHeight();

        BufferedImage image2 = new BufferedImage(width, height, image.getType());

        Calendar cal1 = Calendar.getInstance();
        System.out.println(cal1.getTime());

        double blueX = 0.0;
        double greenX = 0.0;
        double redX = 0.0;

        double blueY = 0.0;
        double greenY = 0.0;
        double redY = 0.0;

        double blueTotal = 0.0;
        double greenTotal = 0.0;
        double redTotal = 0.0;

        int filterOffset = 1;
        int calcOffset = 0;

        int byteOffset = 0;

        for (int offsetY = filterOffset; offsetY <
                height - filterOffset; offsetY++) {
            for (int offsetX = filterOffset; offsetX <
                    width - filterOffset; offsetX++) {
                blueX = greenX = redX = 0;
                blueY = greenY = redY = 0;

                blueTotal = greenTotal = redTotal = 0.0;

                for (int filterY = -filterOffset;
                     filterY <= filterOffset; filterY++) {
                    for (int filterX = -filterOffset;
                         filterX <= filterOffset; filterX++) {
                        Color c = new Color(image.getRGB(offsetX + filterX, offsetY + filterY));

                        blueX += (double) (c.getBlue()) *
                                Sobel3x3Horizontal[filterY + filterOffset][filterX + filterOffset];

                        greenX += (double) (c.getGreen()) *
                                Sobel3x3Horizontal[filterY + filterOffset][filterX + filterOffset];

                        redX += (double) (c.getRed()) *
                                Sobel3x3Horizontal[filterY + filterOffset][filterX + filterOffset];

                        blueY += (double) (c.getBlue()) *
                                Sobel3x3Vertical[filterY + filterOffset][filterX + filterOffset];

                        greenY += (double) (c.getGreen()) *
                                Sobel3x3Vertical[filterY + filterOffset][filterX + filterOffset];

                        redY += (double) (c.getRed()) *
                                Sobel3x3Vertical[filterY + filterOffset][filterX + filterOffset];
                    }
                }

                blueTotal = Math.sqrt((blueX * blueX) + (blueY * blueY));
                greenTotal = Math.sqrt((greenX * greenX) + (greenY * greenY));
                redTotal = Math.sqrt((redX * redX) + (redY * redY));

                if (blueTotal > 255) {
                    blueTotal = 255;
                } else if (blueTotal < 0) {
                    blueTotal = 0;
                }

                if (greenTotal > 255) {
                    greenTotal = 255;
                } else if (greenTotal < 0) {
                    greenTotal = 0;
                }

                if (redTotal > 255) {
                    redTotal = 255;
                } else if (redTotal < 0) {
                    redTotal = 0;
                }

                Color res = new Color((int) redTotal, (int) greenTotal, (int) blueTotal);

                image2.setRGB(offsetX, offsetY, res.getRGB());
            }
        }

        Calendar cal2 = Calendar.getInstance();
        System.out.println(cal2.getTime());

        return image2;
    }

}
