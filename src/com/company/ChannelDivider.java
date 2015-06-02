package com.company;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by Denis on 11.04.2015.
 */
public class ChannelDivider {
    public BufferedImage getOneChannel(BufferedImage image, boolean isRed, boolean isGreen, boolean isBlue) {

        BufferedImage result = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());

        if (isRed) {
            for (int y = 0; y < image.getHeight(); y++) {
                for (int x = 0; x < image.getWidth(); x++) {
                    Color color = new Color(image.getRGB(x,y));
                    color = new Color(color.getRed(),color.getRed(),color.getRed(), color.getAlpha());
                    result.setRGB(x, y, color.getRGB());
                }
            }
        }
        else if (isGreen) {
            for (int y = 0; y < image.getHeight(); y++) {
                for (int x = 0; x < image.getWidth(); x++) {
                    Color color = new Color(image.getRGB(x,y));
                    color = new Color(color.getGreen(),color.getGreen(),color.getGreen(), color.getAlpha());
                    result.setRGB(x, y, color.getRGB());
                }
            }
        }
        else if (isBlue) {
            for (int y = 0; y < image.getHeight(); y++) {
                for (int x = 0; x < image.getWidth(); x++) {
                    Color color = new Color(image.getRGB(x,y));
                    color = new Color(color.getBlue(),color.getBlue(),color.getBlue(), color.getAlpha());
                    result.setRGB(x, y, color.getRGB());
                }
            }
        }

        return result;
    }
}
