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
        File input = new File("assets/02.JPG");
        File input2 = new File("assets/02-3.JPG");
        //File input = new File("assets/example01.jpg");
        //File input2 = new File("assets/result2.jpg");
        try {
            BufferedImage image = ImageIO.read(input);

            image = new Median().countMedian(image, 5);
            image = new BoxBlur().blur(image, 5, 3);
            image = new Median().countMedian(image, 5);
            new AutoLevels().cutBackground(image);
            image = new AutoLevels().countLevels(image);

            BufferedImage image2 = ImageIO.read(input2);
            image = new Multiply().calculate(image, image2);

            /*
            image = new BoxBlur().blur(image, 5, 3);
            image = new Median().countMedian(image, 5);
            image = new Contrast().countContrast(image, 1.2f);
            new AutoLevels().cutBackgroundWithLevels(image, 20);
*/
            /*
            BufferedImage image2 = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
            Graphics g = image2.getGraphics();
            g.drawImage(image, 0, 0, null);
            g.dispose();
            */

            //image = new BoxBlur().blur(image, 5, 3);
            //image = new Median().countMedian(image, 5);
            //image = new EuclidianDistance().countEuclidianDistance(image);

            File output2 = new File("assets/02-1-2.jpg");
            ImageIO.write(image, "jpg", output2);

            //image2 = new Multiply().calculate(image, image2);
            /*
            image2 = new BoxBlur().blurWithScale(image2,7,3);
            image2 = new BoxBlur().blur(image2, 5, 3);
            image2 = new Contrast().countContrast(image2, 0f);
            new AutoLevels().countLevels(image2);
            image2 = new Invert().invert(image2);
            */
            //output2 = new File("assets/02-4.jpg");
            //ImageIO.write(image2, "jpg", output2);

            //image = new Recognition().watershed(image);
            //File output = new File("assets/02-2.jpg");
            //ImageIO.write(image, "jpg", output);
            /*
            new AutoLevels().cutBackgroundWithLevels(image, 90);
            image = new BoxBlur().blur(image, 5, 3);
            image = new Median().countMedian(image, 5);
            image = new AutoLevels().countLevels(image);
            image = new Contrast().countContrast(image, 50);
            new AutoLevels().cutBackground(image);
            image = new BoxBlur().blur(image, 5, 3);
            image = new Binarization().countBinarization(image, 100);
            image = new EuclidianDistance().countEuclidianDistance(image);

            File output2 = new File("assets/03-1.jpg");
            ImageIO.write(image, "jpg", output2);

            image = new Recognition().watershed(image);
            File output = new File("assets/04-1.jpg");
            ImageIO.write(image, "jpg", output);

            /*
            BufferedImage image2 = new BoxBlur().blurWithScale(image, 3, 3);

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
            sobel = new Median().countMedian(sobel, 3);
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


            image = new Median().countMedian(image, 3);
            image = new AutoLevels().countLevels(image);

            BufferedImage imageRedCores = new ChannelDivider().getOneChannel(image, true, false, false);
            BufferedImage imageBlueCores = new ChannelDivider().getOneChannel(image, false, false, true);
            //BufferedImage imageShells =  new ChannelDivider().getOneChannel(image, false, true, false);
            BufferedImage imageShells = new GreyScale().countGreyScale(image);
            output = new File("assets/result-red.jpg");
            ImageIO.write(imageRedCores, "jpg", output);

            new AutoLevels().cutBackgroundWithLevels(imageRedCores);
            new AutoLevels().cutBackgroundWithLevels(imageBlueCores);
            new AutoLevels().cutBackgroundWithLevels(imageShells);

            imageRedCores = new MathMorphology().useMorphology(imageRedCores);
            imageBlueCores = new MathMorphology().useMorphology(imageBlueCores);
            imageShells = new MathMorphology().useMorphology(imageShells);

            imageRedCores = new Median().countMedian(imageRedCores, 3);
            imageBlueCores = new Median().countMedian(imageBlueCores, 3);
            imageShells = new Median().countMedian(imageShells, 3);

            output = new File("assets/result-shells-cutted.jpg");
            ImageIO.write(imageShells, "jpg", output);

            imageRedCores = new Binarization().countBinarization(imageRedCores, 10);
            imageRedCores = new MathMorphology().countErosion(imageRedCores, 5);
            imageRedCores = new MathMorphology().countDilatation(imageRedCores, 5);
            imageRedCores = new Median().countMedian(imageRedCores, 3);

            imageBlueCores = new Binarization().countBinarization(imageBlueCores, 10);
            imageBlueCores = new MathMorphology().countErosion(imageBlueCores, 5);
            imageBlueCores = new MathMorphology().countDilatation(imageBlueCores, 5);
            imageBlueCores = new Median().countMedian(imageBlueCores, 3);

            imageShells = new Binarization().countBinarization(imageShells, 10);
            imageShells = new MathMorphology().countErosion(imageShells, 5);
            imageShells = new MathMorphology().countDilatation(imageShells, 5);
            imageShells = new Median().countMedian(imageShells, 3);

            //new AutoLevels().cutBackgroundWithLevels(imageRedCores);

            //output = new File("assets/result-red-cutted-binary.jpg");
            //ImageIO.write(imageRedCores, "jpg", output);

            imageRedCores = new EuclidianDistance().countEuclidianDistance(imageRedCores);

            output = new File("assets/binary-red.bmp");
            ImageIO.write(imageRedCores, "bmp", output);

            imageBlueCores = new EuclidianDistance().countEuclidianDistance(imageBlueCores);

            output = new File("assets/binary-blue.bmp");
            ImageIO.write(imageBlueCores, "bmp", output);

            imageShells = new EuclidianDistance().countEuclidianDistance(imageShells);

            output = new File("assets/binary-shells.bmp");
            ImageIO.write(imageShells, "bmp", output);

            //imageRedCores = new MathMorphology().countErosion(imageRedCores, 5);
            //imageRedCores = new MathMorphology().countDilatation(imageRedCores, 5);

            //imageRedCores = new Median().countMedian(imageRedCores, 5);

            output = new File("assets/binary-test02.bmp");
            ImageIO.write(imageRedCores, "bmp", output);

            //ArrayList<CellObject> cells = new Recognition().watershed(imageRedCores);
            //new Exporter3D().exportData(cells, image.getWidth(), image.getHeight(), true, false, false);

            //cells = new Recognition().watershed(imageBlueCores);
            //new Exporter3D().exportData(cells, image.getWidth(), image.getHeight(), false, false, true);

            //cells = new Recognition().watershed(imageShells);
            //new Exporter3D().exportData(cells, image.getWidth(), image.getHeight(), false, true, false);

            imageRedCores = new Recognition().watershed(imageRedCores);
            output = new File("assets/result-red.jpg");
            ImageIO.write(imageRedCores, "jpg", output);

            imageBlueCores = new Recognition().watershed(imageBlueCores);
            output = new File("assets/result-blue.jpg");
            ImageIO.write(imageBlueCores, "jpg", output);

            imageShells = new Recognition().watershed(imageShells);
            output = new File("assets/result-shells.jpg");
            ImageIO.write(imageShells, "jpg", output);

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
