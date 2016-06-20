package com.company;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.plugins.jpeg.JPEGImageWriteParam;
import javax.imageio.stream.FileImageOutputStream;
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

        BufferedImage image = null, imagePrev = null, tmp = null;
        File output;
        ArrayList<CellObject> cells = new ArrayList<>();

        JPEGImageWriteParam jpegParams = new JPEGImageWriteParam(null);
        jpegParams.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
        jpegParams.setCompressionQuality(1f);
        final ImageWriter writer = ImageIO.getImageWritersByFormatName("jpg").next();

        for (int i = 1; i <= 2; i++) {

            String filename = i < 10 ? "0" + String.valueOf(i) : String.valueOf(i);

            File input = new File("assets/Input/" + filename + ".JPG");
            //File input = new File("assets/Output/03/" + filename + ".jpg");
            try {
                image = ImageIO.read(input);
                //image = new Median().countMedian(image, 5);
                //image = new MathMorphology().countErosion(image, 3);
                //image = new MathMorphology().countDilatation(image,3);
                //image = new Median().countMedian(image, 5);

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
                else {
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

                writer.setOutput(new FileImageOutputStream(
                        new File("assets/Output/" + filename + "-prev.jpg")));

                writer.write(null, new IIOImage(imagePrev, null, null), jpegParams);

                if (i > 1) {
                    imagePrev = new Multiply().calculate(imagePrev, tmp);

                    writer.setOutput(new FileImageOutputStream(
                            new File("assets/Output/" + filename + "-prev02.jpg")));

                    writer.write(null, new IIOImage(imagePrev, null, null), jpegParams);
                }

                new AutoLevels().cutBackgroundWithLevels(image, 50);
                //image = new Binarization().countBinarization(image, 100);

                //image = new EuclidianDistance().countEuclidianDistance(image);
                //image = new Recognition().watershed(image);

                //cells.addAll(new Recognition().watershed(image, i, true));

                writer.setOutput(new FileImageOutputStream(
                        new File("assets/Output/" + filename + ".jpg")));

                writer.write(null, new IIOImage(image, null, null), jpegParams);

                //output = new File("assets/Output/" + filename + ".jpg");
                //ImageIO.write(image, "jpg", output);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //new Recognition().deleteCopies(cells);

        //new Exporter3D().exportData(cells, image.getWidth(), image.getHeight(), true, false, false);
    }
}
