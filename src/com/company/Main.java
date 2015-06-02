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
        File input = new File("assets/quick04.bmp");
        //File input = new File("assets/example01.jpg");
        //File input2 = new File("assets/result2.jpg");
        try {
            BufferedImage image = ImageIO.read(input);

            /*
            BufferedImage image2 = new BoxBlur().blurWithScale(image, 10, 3);

            File output2 = new File("assets/result0001.jpg");
            ImageIO.write(image2, "jpg", output2);

            image2 = new GreyScale().countGreyScale(image2);

            image2 = new AutoLevels().countLevels(image2);

            image2 = new Invert().invert(image2);

            image = new SoftBlend().applySoftBlend(image, image2);

            output2 = new File("assets/result0001.jpg");
            ImageIO.write(image, "jpg", output2);

            //image = new Gauss().countGauss(image, 3);

            image = new Median().countMedian(image, 3);

            BufferedImage sobel = new Sobel().countSobel(image);
            File output = new File("assets/result5.jpg");
            ImageIO.write(sobel, "jpg", output);
            new EdgeDetection().saveDividedChannels(sobel);

            sobel = new MathMorphology().useMorphology(sobel);

            //sobel = new EdgeDetection().countEdges(image);
            //output = new File("assets/result5.jpg");
            //ImageIO.write(sobel, "jpg", output);

            image = new SoftBlend().applySoftBlend(image, sobel);
            output = new File("assets/result5.jpg");
            ImageIO.write(image, "jpg", output);

            image = new Median().countMedian(image, 3);

            output = new File("assets/result3.jpg");
            ImageIO.write(image, "jpg", output);

            image = new AutoLevels().countLevels(image);

            output = new File("assets/result4.jpg");
            ImageIO.write(image, "jpg", output);

            //output = new File("assets/result4.bmp");
            //ImageIO.write(image, "bmp", output);

            */



            BufferedImage imageRedCores = new ChannelDivider().getOneChannel(image, false, false, true);
            //BufferedImage imageRedCores = new GreyScale().countGreyScale(image);
            File output = new File("assets/result-red.jpg");
            ImageIO.write(imageRedCores, "jpg", output);

            new AutoLevels().cutBackgroundWithLevels(imageRedCores);
            imageRedCores = new MathMorphology().useMorphology(imageRedCores);

            imageRedCores = new Median().countMedian(imageRedCores, 3);

            output = new File("assets/result-red-cutted.jpg");
            ImageIO.write(imageRedCores, "jpg", output);

            imageRedCores = new Binarization().countBinarization(imageRedCores, 10);


            imageRedCores = new MathMorphology().countErosion(imageRedCores, 5);
            imageRedCores = new MathMorphology().countDilatation(imageRedCores, 5);

            imageRedCores = new Median().countMedian(imageRedCores, 3);

            //new AutoLevels().cutBackgroundWithLevels(imageRedCores);

            output = new File("assets/result-red-cutted-binary.jpg");
            ImageIO.write(imageRedCores, "jpg", output);

            imageRedCores = new EuclidianDistance().countEuclidianDistance(imageRedCores);

            output = new File("assets/binary-test01.bmp");
            ImageIO.write(imageRedCores, "bmp", output);

            //imageRedCores = new MathMorphology().countErosion(imageRedCores, 5);
            //imageRedCores = new MathMorphology().countDilatation(imageRedCores, 5);

            //imageRedCores = new Median().countMedian(imageRedCores, 5);

            output = new File("assets/binary-test02.bmp");
            ImageIO.write(imageRedCores, "bmp", output);

            imageRedCores = new Recognition().watershed(imageRedCores);
            output = new File("assets/result-red.jpg");
            ImageIO.write(imageRedCores, "jpg", output);

            /*
            BufferedImage imageBlueCores = new ChannelDivider().getOneChannel(image, false, false, true);
            output = new File("assets/result-blue.jpg");
            ImageIO.write(imageBlueCores, "jpg", output);

            new AutoLevels().cutBackgroundWithLevels(imageBlueCores);
            imageBlueCores = new MathMorphology().useMorphology(imageBlueCores);

            output = new File("assets/result-blue-cutted.jpg");
            ImageIO.write(imageBlueCores, "jpg", output);

            BufferedImage imageShells = new GreyScale().countGreyScale(image);
            output = new File("assets/result-shells.jpg");
            ImageIO.write(imageShells, "jpg", output);

            new AutoLevels().cutBackgroundWithLevels(imageShells);
            imageShells = new MathMorphology().useMorphology(imageShells);

            output = new File("assets/result-shells-cutted.jpg");
            ImageIO.write(imageShells, "jpg", output);
            */

            //ArrayList<CellObject> cells = new Recognition().watershed(imageRedCores);
            //new Exporter3D().exportData(cells, image.getWidth(), image.getHeight(), true, false, false);

            //BufferedImage imageShells = new GreyScale().countGreyScale(image);
            //output = new File("assets/result-shells.jpg");
            //ImageIO.write(imageShells, "jpg", output);

            //new AutoLevels().cutBackgroundWithLevels(imageShells);
            //imageShells = new MathMorphology().useMorphology(imageShells);
            //cells = new Recognition().watershed(imageShells);
           // new Exporter3D().exportData(cells, image.getWidth(), image.getHeight(), false, true, false);


            //new Exporter3D().exportData(cells, image.getWidth(), image.getHeight(), false, false, true);

            //output = new File("assets/result5-red.jpg");
            //ImageIO.write(image, "jpg", output);

            /*

            image = new Recognition().watershed(imageShells);
            output = new File("assets/result5-shells.jpg");
            ImageIO.write(image, "jpg", output);

            image = new Recognition().watershed(imageBlueCores);
            output = new File("assets/result5-blue.jpg");
            ImageIO.write(image, "jpg", output);
            */

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
