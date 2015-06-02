package com.company;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;

/**
 * Created by Denis on 03.04.2015.
 */
public class Median {

    int width;
    int height;

    public BufferedImage countMedian(BufferedImage image, int radius) {

        width = image.getWidth();
        height = image.getHeight();

        BufferedImage image2 = new BufferedImage(width, height, image.getType());

        Calendar cal1 = Calendar.getInstance();
        System.out.println(cal1.getTime());

        int[][] filterMatrix = new int[3][2 * radius];

        for (int i = radius / 2; i < height - radius / 2; i++) {
            for (int j = radius / 2; j < width - radius / 2; j++) {
                for (int k = 0, m = i - radius / 2; k < radius; k++, m++) {

                    Color c = new Color(image.getRGB(m, j));

                    filterMatrix[0][k] = c.getRed();
                    filterMatrix[1][k] = c.getGreen();
                    filterMatrix[2][k] = c.getBlue();
                }
                for (int k = radius, m = j - radius / 2; k < 2 * radius; k++, m++) {
                    Color c = new Color(image.getRGB(i, m));

                    filterMatrix[0][k] = c.getRed();
                    filterMatrix[1][k] = c.getGreen();
                    filterMatrix[2][k] = c.getBlue();
                }
                //filterMatrix = SortFilter(filterMatrix, 2 * radius);
                Arrays.sort(filterMatrix[0]);
                Arrays.sort(filterMatrix[1]);
                Arrays.sort(filterMatrix[2]);

                Color res = new Color(filterMatrix[0][radius - 1], filterMatrix[1][radius - 1], filterMatrix[2][radius - 1]);

                image2.setRGB(i, j, res.getRGB());
            }
        }


        Calendar cal2 = Calendar.getInstance();
        System.out.println(cal2.getTime());

        return image2;

    }
}
