package com.company;

import javax.imageio.ImageIO;
import java.awt.*;

import java.awt.Component;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ConvolveOp;
import java.awt.image.ImageObserver;
import java.awt.image.Kernel;
import java.io.File;
import java.io.IOException;

/**
 * Created by Denis on 11.04.2015.
 */
public class Main02 {

    static boolean imageLoaded = false;
    static BufferedImage image;

    public static void main(String[] args) {

        File input = new File("assets/example01.jpg");
        image = null;
        try {
            image = ImageIO.read(input);
        } catch (IOException e) {
            e.printStackTrace();
        }

        int filterSize = 7;
        int iterations = 20;



    }
}
