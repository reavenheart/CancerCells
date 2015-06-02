package com.company;

import javafx.scene.shape.Circle;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

/**
 * Created by Denis on 11.04.2015.
 */
public class Recognition {

    private int width;
    private int height;
    private ArrayList<CellObject> objects;
    private ArrayList<CellObject> oldObjects = new ArrayList<CellObject>();
    private ArrayList<CellObject> OuterBoundaries = new ArrayList<CellObject>();
    private ArrayList<CellObject> Boundaries = new ArrayList<CellObject>();

    public Recognition() {
        objects = new ArrayList<CellObject>();
    }

    public BufferedImage watershed(BufferedImage image) {
   // public ArrayList<CellObject> watershed(BufferedImage image) {
        //CreateTmp();
        //CreateTemp();

        width = image.getWidth();
        height = image.getHeight();

        BufferedImage temp = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
        temp.setData(image.getData());

        BufferedImage temp2 = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
        temp2.setData(image.getData());

        BufferedImage temp3 = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
        temp3.setData(temp2.getData());

        int MaxHue = FindMaxHue(temp);
        int MinHue = FindMinHue(temp);
        //temp2 = new Median().countMedian(temp2, 3);
        //CountGauss(5, 3, matrixR);
        temp2 = new Binarization().countBinarization(temp, MinHue+1);
        countRecognition(temp2);
        optimizeObjects();
        countArea();
        findCenters();
        OuterBoundaries = objects;
        temp3 = showRecognitionResult(temp2);
        File output = new File("assets/tmp00-1.jpg");
        try {
            ImageIO.write(temp2, "jpg", output);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //int hue = 0;
        //for (int i = MaxHue - ((MinHue)*100/MaxHue/2); i > MinHue+10; i -= 10)

        for (int i = MaxHue; i > MinHue; i -= 10)
        {

            temp2 = new Binarization().countBinarization(temp, i);
            temp2 = new MathMorphology().useMorphology(temp2);
            output = new File("assets/result-red.jpg");
            try {
                ImageIO.write(temp2, "jpg", output);
            } catch (IOException e) {
                e.printStackTrace();
            }
            //drawBoundaries(Boundaries);
            countRecognition(temp2);
            optimizeObjects();
            countArea();
            findCenters();
            int counter;
            ArrayList<CellObject> innerObjects;

            float averageArea = 0;

            if (oldObjects.size() > 0) {
                for (int obj = 0; obj < oldObjects.size(); obj++) {
                    averageArea += oldObjects.get(obj).getArea();
                }

                averageArea /= oldObjects.size();
            }

            if (oldObjects.size() != 0)
            {
                for (int j = 0; j < objects.size(); j++) {
                    if (objects.get(j).getArea() > averageArea) {

                        counter = 0;
                        innerObjects = new ArrayList<>();
                        for (int k = 0; k < oldObjects.size(); k++) {
                            Point p1 = null;
                            for (Point point : objects.get(j).getPoints()) {
                                if (point.getX() == oldObjects.get(k).getCenter().getX() && point.getY() > oldObjects.get(k).getCenter().getY()) {
                                    p1 = point;
                                    break;
                                }
                            }

                            Point p2 = null;
                            for (Point point : objects.get(j).getPoints()) {
                                if (point.getX() == oldObjects.get(k).getCenter().getX() && point.getY() < oldObjects.get(k).getCenter().getY()) {
                                    p2 = point;
                                    break;
                                }
                            }

                            if (p1 != null && p2 != null) {
                                if (oldObjects.get(k).getCenter().getY() < p1.getY() && oldObjects.get(k).getCenter().getY() > p2.getY()) {
                                    p1 = null;
                                    for (Point point : objects.get(j).getPoints()) {
                                        if (point.getY() == oldObjects.get(k).getCenter().getY() && point.getX() > oldObjects.get(k).getCenter().getX()) {
                                            p1 = point;
                                            break;
                                        }
                                    }

                                    p2 = null;
                                    for (Point point : objects.get(j).getPoints()) {
                                        if (point.getY() == oldObjects.get(k).getCenter().getY() && point.getX() < oldObjects.get(k).getCenter().getX()) {
                                            p2 = point;
                                            break;
                                        }
                                    }

                                    if (p1 != p2) {
                                        counter++;
                                        innerObjects.add(oldObjects.get(k));
                                    }
                                }
                            }
                        }

                        if (counter > 1) {
                            sortByX(innerObjects);

                            float angle = 0;
                            float percent1, percent2 = 0;

                            Point center = new Point();

                            for (int f = 0; f < innerObjects.size() - 1; f++) {

                                if (innerObjects.get(f).getArea() < averageArea * 0.3) {
                                    innerObjects.remove(f);
                                } else {
                                    center.setX(innerObjects.get(f + 1).getCenter().getX() - (innerObjects.get(f + 1).getCenter().getX() - innerObjects.get(f).getCenter().getX()) / 2);
                                    center.setY(innerObjects.get(f + 1).getCenter().getY() - (innerObjects.get(f + 1).getCenter().getY() - innerObjects.get(f).getCenter().getY()) / 2);


                                    percent1 = innerObjects.get(f).getArea() / (innerObjects.get(f).getArea() + innerObjects.get(f + 1).getArea());
                                    percent2 = innerObjects.get(f + 1).getArea() / (innerObjects.get(f).getArea() + innerObjects.get(f + 1).getArea());

                                    float a, b;

                                    a = innerObjects.get(f + 1).getCenter().getX() - innerObjects.get(f).getCenter().getX();
                                    b = innerObjects.get(f + 1).getCenter().getY() - innerObjects.get(f).getCenter().getY();

                                    float vectorLength = (float) Math.sqrt(a * a + b * b);

                                    float directionX = a / vectorLength;
                                    float directionY = b / vectorLength;

                                    center.setX((int) (innerObjects.get(f).getCenter().getX() + directionX * vectorLength * percent1));
                                    center.setY((int) (innerObjects.get(f).getCenter().getY() + directionY * vectorLength * percent1));


                                    boolean isFound = false;

                                    double mistake = 2;

                                    a = center.getX();
                                    b = center.getY();

                                    angle = 90;

                                    float tmpX = directionX;
                                    float tmpY = directionY;

                                    directionX = (float) (tmpX * Math.cos(Math.toRadians(angle)) - tmpY * Math.sin(Math.toRadians(angle)));
                                    directionY = (float) (tmpX * Math.sin(Math.toRadians(angle)) + tmpY * Math.cos(Math.toRadians(angle)));

                                    paintToBlack(temp2, (int) a, (int) b);
                                    while (!isFound) {
                                        a += directionX;
                                        b += directionY;
                                        paintToBlack(temp2, (int) a, (int) b);
                                /*
                                for (int t = 0; t < objects.get(j).getPoints().size(); t++) {
                                    if (a >= objects.get(j).getPoints().get(t).getX() - mistake &&
                                            a <= objects.get(j).getPoints().get(t).getX() + mistake)
                                        if (b >= objects.get(j).getPoints().get(t).getY() - mistake &&
                                                b <= objects.get(j).getPoints().get(t).getY() + mistake) {
                                            a = objects.get(j).getPoints().get(t).getX();
                                            b = objects.get(j).getPoints().get(t).getY();
                                            paintToBlack(temp2, (int) a, (int) b);

                                            isFound = true;
                                            break;
                                        }
                                    if (a > width || b > height || a < 0 || b < 0) {
                                        isFound = true;
                                        break;
                                    }
                                }
                                if (isFound)
                                    break;
                                    */
                                        for (int n = 0; n < OuterBoundaries.size(); n++) {
                                            for (int t = 0; t < OuterBoundaries.get(n).getPoints().size(); t++) {
                                                if (a >= OuterBoundaries.get(n).getPoints().get(t).getX() - mistake &&
                                                        a <= OuterBoundaries.get(n).getPoints().get(t).getX() + mistake)
                                                    if (b >= OuterBoundaries.get(n).getPoints().get(t).getY() - mistake &&
                                                            b <= OuterBoundaries.get(n).getPoints().get(t).getY() + mistake) {
                                                        a = OuterBoundaries.get(n).getPoints().get(t).getX();
                                                        b = OuterBoundaries.get(n).getPoints().get(t).getY();
                                                        paintToBlack(temp2, (int) a, (int) b);

                                                        isFound = true;
                                                        break;
                                                    }
                                                if (a > width || b > height || a < 0 || b < 0) {
                                                    isFound = true;
                                                    break;
                                                }
                                            }
                                            if (isFound)
                                                break;
                                        }
                                    }

                                    isFound = false;

                                    a = center.getX();
                                    b = center.getY();

                                    angle = 180;

                                    directionX = (float) (directionX * Math.cos(Math.toRadians(angle)) - directionY * Math.sin(Math.toRadians(angle)));
                                    directionY = (float) (directionX * Math.sin(Math.toRadians(angle)) + directionY * Math.cos(Math.toRadians(angle)));

                                    paintToBlack(temp2, (int) a, (int) b);
                                    while (!isFound) {
                                        a += directionX;
                                        b += directionY;
                                        paintToBlack(temp2, (int) a, (int) b);
                                        for (int n = 0; n < OuterBoundaries.size(); n++) {
                                            for (int t = 0; t < OuterBoundaries.get(n).getPoints().size(); t++) {
                                                if (a >= OuterBoundaries.get(n).getPoints().get(t).getX() - mistake &&
                                                        a <= OuterBoundaries.get(n).getPoints().get(t).getX() + mistake)
                                                    if (b >= OuterBoundaries.get(n).getPoints().get(t).getY() - mistake &&
                                                            b <= OuterBoundaries.get(n).getPoints().get(t).getY() + mistake) {
                                                        a = OuterBoundaries.get(n).getPoints().get(t).getX();
                                                        b = OuterBoundaries.get(n).getPoints().get(t).getY();
                                                        paintToBlack(temp2, (int) a, (int) b);

                                                        isFound = true;
                                                        break;
                                                    }
                                                if (a > width || b > height || a < 0 || b < 0) {
                                                    isFound = true;
                                                    break;
                                                }
                                            }
                                            if (isFound)
                                                break;
                                        }
                                    }
                                }
                            }
                        }

                    }
                    else {
                        objects.remove(j);
                        j--;
                    }
                }
            }



            if (oldObjects.size() > 0)
            {
                countRecognition(temp2);
                //optimizeObjects();
                countArea();
                findCenters();
            }


            oldObjects = objects;

            output = new File("assets/tmp02.jpg");
            try {
                ImageIO.write(showRecognitionResult(temp2), "jpg", output);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //ShowRecognizion(true, false, false);
        //ShowCenters(true, false, false);

        BufferedImage result = showRecognitionResult(image);

        return result;

        //return objects;

    }

    private BufferedImage showRecognitionResult(BufferedImage image) {
        BufferedImage result = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());

        Graphics2D g2 = result.createGraphics();

        for (CellObject object : objects) {
            Random rand = new Random();
            float r = rand.nextFloat();
            float g = rand.nextFloat();
            float b = rand.nextFloat();

            Color randomColor = new Color(r, g, b);

            int[] arrayX = new int[object.getPoints().size()];
            int[] arrayY = new int[object.getPoints().size()];

            for (int i = 0; i < object.getPoints().size(); i++) {
                arrayX[i] = object.getPoints().get(i).getX();
                arrayY[i] = object.getPoints().get(i).getY();
            }

            Polygon p = new Polygon(arrayX, arrayY, object.getPoints().size());

            g2.setColor(randomColor);
            g2.fillPolygon(p);

            r = rand.nextFloat();
            g = rand.nextFloat();
            b = rand.nextFloat();

            randomColor = new Color(r, g, b);

            g2.setColor(randomColor);
            g2.fillOval(object.getCenter().getX(), object.getCenter().getY(), 5, 5);

        }

        return result;
    }

    private void paintToBlack(BufferedImage temp2, int x, int y) {
        if (x < width - 1 && y < height - 1 && x > 1 && y > 1)
        {
            Color c = new Color(0,0,0);
            temp2.setRGB(x,y, c.getRGB());
            temp2.setRGB(x-1,y-1, c.getRGB());
            temp2.setRGB(x-1,y, c.getRGB());
            temp2.setRGB(x-1,y+1, c.getRGB());
            temp2.setRGB(x,y+1, c.getRGB());
            temp2.setRGB(x+1,y, c.getRGB());
            temp2.setRGB(x+1,y-1, c.getRGB());
            temp2.setRGB(x,y-1, c.getRGB());
            temp2.setRGB(x+1,y+1, c.getRGB());
        }
    }

    private void sortByX(ArrayList<CellObject> innerObjects) {
        Collections.sort(innerObjects, new Comparator<CellObject>() {
            @Override
            public int compare(CellObject p1, CellObject p2) {
                return p1.getCenter().getX() - p2.getCenter().getX();
            }

        });
    }

    private void findCenters() {
        for(CellObject obj : objects)
        {
            double centerX = 0;
            double centerY = 0;

            for (int i = 0; i < obj.getPoints().size(); i++)
            {
                centerX += obj.getPoints().get(i).getX();
                centerY += obj.getPoints().get(i).getY();
            }

            centerX /= obj.getPoints().size();
            centerY /= obj.getPoints().size();

            obj.getCenter().setX((int)centerX);
            obj.getCenter().setY((int)centerY);
        }
    }

    private void countArea() {
        for (int i = 0; i < objects.size(); i++)
        {
            float sum1 = 0;
            float sum2 = 0;
            for (int j = 0; j < objects.get(i).getPoints().size()-1; j++)
            {
                sum1 += objects.get(i).getPoints().get(j).getX() * objects.get(i).getPoints().get(j+1).getY();
                sum2 += objects.get(i).getPoints().get(j).getY() * objects.get(i).getPoints().get(j+1).getX();
            }

            sum1 -= sum2;
            objects.get(i).setArea(sum1);
        }
    }

    private void optimizeObjects() {
        for (int i = 0; i < objects.size(); i++)
        {
            if (objects.get(i).getPoints().size() < 40)
            {
                objects.remove(objects.get(i));
                i--;
            }
        }
    }

    private void countRecognition(BufferedImage image) {
        objects = new ArrayList<>();

        for (int y = 0; y < image.getHeight(); y++)
        {
            for (int x = 0; x < image.getHeight(); x++)
            {
                if (new Color(image.getRGB(x,y)).getRed() > 128)
                {

                    if (isBoundaryPixel(image, x, y))
                    {
                        if (isNewPixel(x, y))
                        {
                            CellObject obj = RecognizeObject(image, x, y);
                            objects.add(obj);
                        }
                    }
                }
            }
        }
    }

    private CellObject RecognizeObject(BufferedImage image, int x, int y)
    {
        CellObject obj = new CellObject();

        Point firstPoint = new Point(x, y);
        Point newPoint = new Point();
        Point prevPoint = new Point();
        Point nextPoint = new Point();
        int posX = x, posY = y;
        int dX = 0, dY = 0;

        obj.getPoints().add(firstPoint);

        while (newPoint != firstPoint)
        {

            if (posX == x && posY == y)
            {
                prevPoint.setX(posX);
                prevPoint.setY(posY);

                int i = 1;
                int j = 2;

                dX = 1;
                dY = 0;

                int count = 0;
                boolean isFound = false;
                while (!isFound)
                {
                    switch (i)
                    {
                        case 0:
                            switch (j)
                            {
                                case 0: dX++; j++; count++; break;
                                case 1: dX++; j++; count++; break;
                                case 2: dY++; i++; count++; break;
                            }
                            break;
                        case 1:
                            switch (j)
                            {
                                case 0: dY--; i--; count++; break;
                                case 2: dY++; i++; count++; break;
                            }
                            break;
                        case 2:
                            switch (j)
                            {
                                case 0: dY--; i--; count++; break;
                                case 1: dX--; j--; count++; break;
                                case 2: dX--; j--; count++; break;
                            }
                            break;
                    }

                    if (posX + dX < width && posX + dX >= 0 && posY + dY < height && posY + dY >= 0)
                    {
                        if (new Color(image.getRGB(posX + dX,posY + dY)).getRed() > 128)
                        {
                            posX += dX;
                            posY += dY;
                            isFound = true;
                            break;
                        }
                    }
                    if (count >= 9)
                        break;
                }

                if (!isFound)
                    break;

            }
            else
            {

                dX = prevPoint.getX() - newPoint.getX();
                dY = prevPoint.getY() - newPoint.getY();

                int i, j;

                switch (dX)
                {
                    case -1: j = 0; break;
                    case 0: j = 1; break;
                    case 1: j = 2; break;
                    default: j = 2; break;
                }
                switch (dY)
                {
                    case -1: i = 0; break;
                    case 0: i = 1; break;
                    case 1: i = 2; break;
                    default: i = 1; break;
                }

                boolean isFound = false;
                while (!isFound)
                {
                    switch (i)
                    {
                        case 0:
                            switch (j)
                            {
                                case 0: dX++; j++; break;
                                case 1: dX++; j++; break;
                                case 2: dY++; i++; break;
                            }
                            break;
                        case 1:
                            switch (j)
                            {
                                case 0: dY--; i--; break;
                                case 2: dY++; i++; break;
                            }
                            break;
                        case 2:
                            switch (j)
                            {
                                case 0: dY--; i--; break;
                                case 1: dX--; j--; break;
                                case 2: dX--; j--; break;
                            }
                            break;
                    }

                    if (posX + dX < width && posX + dX >= 0 && posY + dY < width && posY + dY >= 0)
                    {
                        if (new Color(image.getRGB(posX + dX,posY + dY)).getRed() > 128)
                        {
                            prevPoint = new Point();
                            prevPoint.setX(newPoint.getX());
                            prevPoint.setY(newPoint.getY());
                            posX += dX;
                            posY += dY;
                            isFound = true;
                            break;
                        }
                    }
                }
            }

            newPoint = new Point();
            newPoint.setX(posX);
            newPoint.setY(posY);
            if (newPoint.getY() == firstPoint.getY() && newPoint.getX() == firstPoint.getX())
                break;
            obj.getPoints().add(newPoint);
        }

        return obj;
    }

    private boolean isBoundaryPixel(BufferedImage image, int x, int y)
    {
        if (x + 1 < width)
        {
            if (new Color(image.getRGB(x+1,y)).getRed() < 128)
                return true;
            else
                return false;
        }
        else
            return true;
    }

    private boolean isNewPixel(int x, int y)
    {
        if (objects.size() == 0)
            return true;
        else if (objects.size() > 0)
        {
            for(CellObject obj : objects)
            {
                for (Point point : obj.getPoints())
                {
                    if (point.getX() == x && point.getY() == y)
                        return false;
                }
            }

            return true;
        }
        else
            return false;
    }

    private int FindMaxHue(BufferedImage image) {
        int hue = 0;
        //double hue2 = 255;

        for (int i = 0; i < image.getHeight(); i++)
        {
            for (int j = 0; j < image.getWidth(); j++)
            {
                Color color = new Color(image.getRGB(j,i));
                if (hue < color.getRed())
                hue = color.getRed();
            }
        }

        hue -= 10;

        return hue;
    }

    private int FindMinHue(BufferedImage image) {
        int hue = 255;

        for (int i = 0; i < image.getHeight(); i++)
        {
            for (int j = 0; j < image.getWidth(); j++)
            {
                Color color = new Color(image.getRGB(j,i));

                if (hue > color.getRed())
                    hue = color.getRed();
            }
        }

        //hue += 5;

        return hue;
    }

}
