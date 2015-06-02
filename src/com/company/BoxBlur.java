package com.company;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;

/**
 * Created by Denis on 11.04.2015.
 */
public class BoxBlur {

    public BufferedImage blur(BufferedImage image, int filterSize, int iterations) {
        float[] blurArray = new float[filterSize*filterSize];

        for (int i = 0; i < filterSize; i++) {
            for (int j = 0; j < filterSize; j++) {
                blurArray[j + filterSize*i] = 1f/(float)(filterSize*filterSize);
            }
        }

        Kernel kernel = new Kernel(filterSize, filterSize, blurArray);

        BufferedImageOp op = new ConvolveOp(kernel);

        for (int i=0; i < iterations; i++)
            image = op.filter(image, null);

        return image;
    }

    public BufferedImage blurWithScale(BufferedImage imageSource, int filterSize, int iterations) {

        int smallWidth = imageSource.getWidth()/filterSize;
        int smallHeight = imageSource.getHeight()/filterSize;

        BufferedImage newImage = new BufferedImage(smallWidth, smallHeight, imageSource.getType());

        Graphics g = newImage.createGraphics();
        g.drawImage(imageSource, 0, 0, smallWidth, smallHeight, null);
        g.dispose();

        float[] blurArray = new float[filterSize*filterSize];

        for (int i = 0; i < filterSize; i++) {
            for (int j = 0; j < filterSize; j++) {
                blurArray[j + filterSize*i] = 1f/(float)(filterSize*filterSize);
            }
        }

        Kernel kernel = new Kernel(filterSize, filterSize, blurArray);

        BufferedImageOp op = new ConvolveOp(kernel);

        for (int i=0; i < iterations; i++)
            newImage = op.filter(newImage, null);

        smallWidth = imageSource.getWidth();
        smallHeight = imageSource.getHeight();

        imageSource = new BufferedImage(smallWidth, smallHeight, imageSource.getType());

        g = imageSource.createGraphics();
        g.drawImage(newImage, 0, 0, smallWidth, smallHeight, null);
        g.dispose();

        return imageSource;
    }

}
