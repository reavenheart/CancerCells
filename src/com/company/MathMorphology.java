package com.company;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by Denis on 03.05.2015.
 */
public class MathMorphology {

    public BufferedImage useMorphology(BufferedImage image) {

        BufferedImage result = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());

        countMorphology(image, result, 3, false);
        BufferedImage result2 = new Median().countMedian(result, 5);
        saveDividedChannels(result2);

        countMorphology(result2, result, 3, true);

        result = new AutoLevels().countLevels(result);
        saveDividedChannels(result2);

        return result;

    }


    public BufferedImage countErosion(BufferedImage image, int radius) {

        BufferedImage result = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());

        countMorphology(image, result, radius, false);

        return result;
    }

    public BufferedImage countDilatation(BufferedImage image, int radius) {

        BufferedImage result = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());

        countMorphology(image, result, radius, true);

        return result;
    }

    private void countMorphology(BufferedImage image, BufferedImage result, int radius, boolean isDilatation) {
        int[][] matrixR = new int[radius][radius];
        int[][] matrixG = new int[radius][radius];
        int[][] matrixB = new int[radius][radius];

        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                result.setRGB(x,y, image.getRGB(x,y));
            }
        }


        for (int y = radius/2; y < image.getHeight() - radius/2; y++) {
            for (int x = radius / 2; x < image.getWidth() - radius / 2; x++) {
                for (int i = 0; i < radius; i++) {
                    for (int j = 0; j < radius; j++) {
                        Color color = new Color(image.getRGB(x + j - radius / 2, y + i - radius / 2));

                        matrixR[j][i] = color.getRed();
                        matrixG[j][i] = color.getGreen();
                        matrixB[j][i] = color.getBlue();
                    }
                }

                Color color = new Color(image.getRGB(x, y));

                Color newColor;
                int minRed = 255;
                int minGreen = 255;
                int minBlue = 255;
                int maxRed = 0;
                int maxGreen = 0;
                int maxBlue = 0;

                int middleRed = 0;
                int middleGreen = 0;
                int middleBlue = 0;

                /*
                for (int i = 0; i < radius; i++) {
                    if (matrixR[i][radius/2] < minRed)
                        minRed = matrixR[i][radius/2];
                    if (matrixG[i][radius/2] < minGreen)
                        minGreen = matrixG[i][radius/2];
                    if (matrixB[i][radius/2] < minBlue)
                        minBlue = matrixB[i][radius/2];

                    if (matrixR[i][radius/2] > maxRed)
                        maxRed = matrixR[i][radius/2];
                    if (matrixG[i][radius/2] > maxGreen)
                        maxGreen = matrixG[i][radius/2];
                    if (matrixB[i][radius/2] > maxBlue)
                        maxBlue = matrixB[i][radius/2];
                }
                for (int j = 0; j < radius; j++) {
                    if (matrixR[radius/2][j] < minRed)
                        minRed = matrixR[radius/2][j];
                    if (matrixG[radius/2][j] < minGreen)
                        minGreen = matrixG[radius/2][j];
                    if (matrixB[radius/2][j] < minBlue)
                        minBlue = matrixB[radius/2][j];

                    if (matrixR[radius/2][j] > maxRed)
                        maxRed = matrixR[radius/2][j];
                    if (matrixG[radius/2][j] > maxGreen)
                        maxGreen = matrixG[radius/2][j];
                    if (matrixB[radius/2][j] > maxBlue)
                        maxBlue = matrixB[radius/2][j];
                }
                */
                for (int i = 0; i < radius; i++) {
                    for (int j = 0; j < radius; j++) {
                        if (matrixR[i][j] < minRed)
                            minRed = matrixR[i][j];
                        if (matrixG[i][j] < minGreen)
                            minGreen = matrixG[i][j];
                        if (matrixB[i][j] < minBlue)
                            minBlue = matrixB[i][j];

                        if (matrixR[i][j] > maxRed)
                            maxRed = matrixR[i][j];
                        if (matrixG[i][j] > maxGreen)
                            maxGreen = matrixG[i][j];
                        if (matrixB[i][j] > maxBlue)
                            maxBlue = matrixB[i][j];
                    }
                }


                middleRed = (maxRed + minRed) / 2;
                middleGreen = (maxGreen + minGreen) / 2;
                middleBlue = (maxBlue + minBlue) / 2;


                if (isDilatation)
                    newColor = new Color(maxRed, maxGreen, maxBlue);
                else {


                    newColor = new Color(minRed, minGreen, minBlue);
                }


                result.setRGB(x, y, newColor.getRGB());

            }
        }
/*
        try {
            File output = new File("assets/erosionRaw.jpg");
            ImageIO.write(result, "jpg", output);

            saveDividedChannels(result);

            result = new AutoLevels().countLevels(result);

            output = new File("assets/erosionAfterLevels.jpg");
            ImageIO.write(result, "jpg", output);
        } catch (IOException e) {
            e.printStackTrace();
        }
        */
    }

    public void saveDividedChannels(BufferedImage img) {
        BufferedImage tmpR = new BufferedImage(img.getWidth(), img.getHeight(), img.getType());
        BufferedImage tmpG = new BufferedImage(img.getWidth(), img.getHeight(), img.getType());
        BufferedImage tmpB = new BufferedImage(img.getWidth(), img.getHeight(), img.getType());

        for (int y = 0; y < img.getHeight(); y++) {
            for (int x = 0; x < img.getWidth(); x++) {
                Color color = new Color(img.getRGB(x,y));

                Color newColor = new Color(color.getRed(),color.getRed(),color.getRed());
                tmpR.setRGB(x,y, newColor.getRGB());

                newColor = new Color(color.getGreen(),color.getGreen(),color.getGreen());
                tmpG.setRGB(x,y, newColor.getRGB());

                newColor = new Color(color.getBlue(),color.getBlue(),color.getBlue());
                tmpB.setRGB(x,y, newColor.getRGB());
            }
        }

        /*
        try {
            File output = new File("assets/erosionR.jpg");
            ImageIO.write(tmpR, "jpg", output);

            output = new File("assets/erosG.jpg");
            ImageIO.write(tmpG, "jpg", output);

            output = new File("assets/erosionB.jpg");
            ImageIO.write(tmpB, "jpg", output);
        } catch (IOException e) {
            e.printStackTrace();
        }
        */
    }
}
