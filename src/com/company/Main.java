package com.company;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {

        BufferedImage imagePrev = null, tmp = null;
        File output;

        for (int i = 1; i <= 19; i++) {

            String filename = i < 10 ? '0' + String.valueOf(i) : String.valueOf(i);

            File input = new File("assets/Input/" + filename + ".JPG");
            try {
                BufferedImage image = ImageIO.read(input);

                image = new Median().countMedian(image, 5);
                image = new BoxBlur().blur(image, 5, 3);
                image = new Median().countMedian(image, 5);
                new AutoLevels().cutBackground(image);
                image = new AutoLevels().countLevels(image);
                image = new Contrast().countContrast(image, 1.2f);
                new AutoLevels().cutBackgroundWithLevels(image, 20);
                image = new BoxBlur().blur(image, 5, 1);
                image = new Median().countMedian(image, 2);
                if (i > 1 && imagePrev != null) {
                    tmp = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
                    Graphics g = tmp.getGraphics();
                    g.drawImage(imagePrev, 0, 0, null);
                    g.dispose();

                    image = new Multiply().calculate(image, imagePrev);
                    image = new Contrast().countContrast(image, 0.3f);
                }

                imagePrev = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
                Graphics g = imagePrev.getGraphics();
                    g.drawImage(image, 0, 0, null);
                g.dispose();

                imagePrev = new Contrast().countContrast(imagePrev, 0.5f);
                imagePrev = new BoxBlur().blurWithScale(imagePrev,7,2);
                imagePrev = new Median().countMedian(imagePrev, 3);
                imagePrev = new BoxBlur().blur(imagePrev, 5, 3);
                new AutoLevels().countLevels(imagePrev);
                imagePrev = new Invert().invert(imagePrev);
                imagePrev = new Contrast().countContrast(imagePrev, 1f);
                imagePrev = new BoxBlur().blur(imagePrev, 5, 3);

                if (i > 1) {
                    imagePrev = new Multiply().calculate(imagePrev, tmp);
                }

                output = new File("assets/Output/" + filename + ".jpg");
                ImageIO.write(image, "jpg", output);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
