package com.company;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Calendar;

/**
 * Created by Denis on 05.04.2015.
 */
public class EdgeDetection {

    int width;
    int height;

    double[][] Edges3x3 = new double[][] {
            { -1, -1, -1, },
            { -1,  8, -1, },
            { -1, -1, -1, }
    };

    public BufferedImage countEdges(BufferedImage image) {

        width = image.getWidth();
        height = image.getHeight();

        BufferedImage image2 = new BufferedImage(width, height, image.getType());

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
                                Edges3x3[filterY + filterOffset][filterX + filterOffset];

                        greenX += (double) (c.getGreen()) *
                                Edges3x3[filterY + filterOffset][filterX + filterOffset];

                        redX += (double) (c.getRed()) *
                                Edges3x3[filterY + filterOffset][filterX + filterOffset];
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

        int maxHue = 0;
        double ar, br, ag, bg, ab, bb;

        BufferedImage image3 = new BufferedImage(width, height, image.getType());

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                Color c = new Color(image2.getRGB(j, i));
                if (maxHue < c.getRed())
                    maxHue = c.getRed();
            }
        }

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                Color c2 = new Color(image2.getRGB(j, i));
                Color c1 = new Color(image.getRGB(j, i));
                float[] components1 = new float[3];
                float[] components2 = new float[3];
                c1.getColorComponents(components1);
                c2.getColorComponents(components2);
                ar = components1[0];
                br = components2[0];

                ag = components1[1];
                bg = components2[1];
                ab = components1[2];
                bb = components2[2];

                double nr, ng, nb;

                if (br <= 0.5)
                    nr = 2 * ar * br + ar * ar * (1 - 2 * br);
                else
                    nr = 2 * ar * (1 - br) + Math.sqrt(ar) * (2 * br - 1);

                if (bg <= 0.5)
                    ng = 2 * ag * bg + ag * ag * (1 - 2 * bg);
                else
                    ng = 2 * ag * (1 - bg) + Math.sqrt(ag) * (2 * bg - 1);

                if (bb <= 0.5)
                    nb = 2 * ab * bb + ab * ab * (1 - 2 * bb);
                else
                    nb = 2 * ab * (1 - bb) + Math.sqrt(ab) * (2 * bb - 1);

                if (nr < 0)
                    nr = 0;
                else if (nr > 1)
                    nr = 1;

                if (ng < 0)
                    ng = 0;
                else if (ng > 1)
                    ng = 1;

                if (nb < 0)
                    nb = 0;
                else if (nb > 1)
                    nb = 1;

                c1 = new Color((float) nr, (float) ng, (float) nb);

                image3.setRGB(j, i, c1.getRGB());
            }
        }

        Calendar cal2 = Calendar.getInstance();
        System.out.println(cal2.getTime());

        return image3;
    }

    public void saveDividedChannels(BufferedImage sobel) {
        BufferedImage tmpR = new BufferedImage(sobel.getWidth(), sobel.getHeight(), sobel.getType());
        BufferedImage tmpG = new BufferedImage(sobel.getWidth(), sobel.getHeight(), sobel.getType());
        BufferedImage tmpB = new BufferedImage(sobel.getWidth(), sobel.getHeight(), sobel.getType());

        for (int y = 0; y < sobel.getHeight(); y++) {
            for (int x = 0; x < sobel.getWidth(); x++) {
                Color color = new Color(sobel.getRGB(x,y));

                Color newColor = new Color(color.getRed(),color.getRed(),color.getRed());
                tmpR.setRGB(x,y, newColor.getRGB());

                newColor = new Color(color.getGreen(),color.getGreen(),color.getGreen());
                tmpG.setRGB(x,y, newColor.getRGB());

                newColor = new Color(color.getBlue(),color.getBlue(),color.getBlue());
                tmpB.setRGB(x,y, newColor.getRGB());
            }
        }

        try {
            File output = new File("assets/sobelRed.jpg");
            ImageIO.write(tmpR, "jpg", output);

            output = new File("assets/sobelGreen.jpg");
            ImageIO.write(tmpG, "jpg", output);

            output = new File("assets/sobelBlue.jpg");
            ImageIO.write(tmpB, "jpg", output);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
