package com.company;

import com.company.Voronoi.Voronoi;
import javafx.scene.shape.Circle;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;

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

    private int zIndex = 0;

    public Recognition() {
        objects = new ArrayList<CellObject>();
    }

    //public BufferedImage watershed(BufferedImage image) {
    public ArrayList<CellObject> watershed(BufferedImage image, int zIndex, boolean isBinary) {
        //CreateTmp();
        //CreateTemp();
        this.zIndex = zIndex;
        width = image.getWidth();
        height = image.getHeight();

        BufferedImage temp = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
        temp.setData(image.getData());

        BufferedImage temp2 = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
        temp2.setData(image.getData());

        BufferedImage temp3 = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
        temp3.setData(temp2.getData());

        int MaxHue = 1;
        int MinHue = 0;
        if(!isBinary) {
            MaxHue = FindMaxHue(temp);
            MinHue = FindMinHue(temp);
            temp2 = new Binarization().countBinarization(temp, MinHue+10);
        }
        //temp2 = new Median().countMedian(temp2, 3);
        //CountGauss(5, 3, matrixR);
        countRecognition(temp2);
        optimizeObjects();
        countArea();
        findCenters();
        OuterBoundaries = objects;
        /*
        temp3 = showRecognitionResult(temp2);
        File output = new File("assets/tmp00-1.jpg");
        try {
            ImageIO.write(temp2, "jpg", output);
        } catch (IOException e) {
            e.printStackTrace();
        }
        */

        //int hue = 0;
        //for (int i = MaxHue - ((MinHue)*100/MaxHue/2); i > MinHue+10; i -= 10)

        for (int i = MaxHue; i > MinHue; i -= 10)
        {

            temp2 = new Binarization().countBinarization(temp, i);
            temp2 = new MathMorphology().useMorphology(temp2);
            /*
            output = new File("assets/result-red.jpg");
            try {
                ImageIO.write(temp2, "jpg", output);
            } catch (IOException e) {
                e.printStackTrace();
            }
            */
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

                        countVoronoi(temp2, averageArea);

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
                optimizeObjects();
                countArea();
                findCenters();
            }


            oldObjects = objects;



        }
        //ShowRecognizion(true, false, false);
        //ShowCenters(true, false, false);

        BufferedImage result = showRecognitionResult(image);

        File output = new File("assets/tmp02.jpg");
        try {
            ImageIO.write(result, "jpg", output);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //return result;

        return objects;

    }

    private void countVoronoi(BufferedImage temp2, float averageArea) {
        ArrayList<Point> centers = new ArrayList<>();

        //optimizeOldObjects();
        for (CellObject obj : oldObjects) {
            if (obj.getArea() > averageArea * 0.3)
                centers.add(obj.getCenter());
        }

        if (centers.size() > 0)
            new Voronoi().countVoronoi(centers, width, height, temp2);
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
        long allPoints = 0;
        for (int i = 0; i < objects.size(); i++)
            allPoints += objects.get(i).getPoints().size();

        allPoints /= objects.size();

        allPoints *= 0.7f;

        for (int i = 0; i < objects.size(); i++)
        {
            if (objects.get(i).getPoints().size() < allPoints)
            {
                objects.remove(objects.get(i));
                i--;
            }
        }
    }

    private void optimizeOldObjects() {
        for (int i = 0; i < oldObjects.size(); i++)
        {
            if (oldObjects.get(i).getPoints().size() < 120)
            {
                oldObjects.remove(oldObjects.get(i));
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

        obj.setzIndex(zIndex);
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

    public void deleteCopies(ArrayList<CellObject> cells) {

        for (int i = cells.size() - 1; i >= 0; i--) {

            CellObject cell01 = cells.get(i);

            for (int j = i; j >= 0; j--) {
                CellObject cell02 = cells.get(j);

                if (cell02.intersects(cell01) && cell01.getzIndex() != cell02.getzIndex()) {
                    if (cell02.getArea() <= cell01.getArea()) {
                        cells.remove(j);
                    }
                    else {
                        cells.remove(cell01);
                        break;
                    }
                }
            }

            if (i > cells.size())
                i = cells.size();
        }
    }
}
