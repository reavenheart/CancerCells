package com.company;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Denis on 31.05.2015.
 */
public class EuclidianDistance {

    private int width = 0;
    private int height = 0;

    private float diagonalDistance = 0;

    private ArrayList<CellObject> objects;
    private ArrayList<EuclidianObject> euclidianObjects;

    BufferedImage result;

    BufferedImage image;

    public BufferedImage countEuclidianDistance(BufferedImage image) {
        width = image.getWidth();
        height = image.getHeight();

        diagonalDistance = (float) (Math.sqrt(width * width + height * height));

        this.image = image;

        result = new BufferedImage(width, height, image.getType());

        countRecognition(image);
        euclidian();

        return result;
    }

    private void euclidian() {
        euclidianObjects = new ArrayList<>();

        for (CellObject obj : objects) {
            EuclidianObject eObj = new EuclidianObject();
            eObj.boundaries = obj;
            eObj.points = new ArrayList<>();
            euclidianObjects.add(eObj);
        }

        for (EuclidianObject eObject : euclidianObjects) {
            if (eObject.boundaries.getPoints().size() > 2) {
                countEuclidianPoints(eObject);
            }
        }

        for (EuclidianObject eObject : euclidianObjects) {

            for (EuclidianPoint point : eObject.points) {
                float percent = point.nearestDistance * 100 / eObject.maxDistance;
                int c = (int) (2.55 * percent);
                Color color = new Color(c,c,c);
                point.color = color;
            }
        }

        Graphics2D g2 = result.createGraphics();

        for (EuclidianObject eObject : euclidianObjects) {
            for (EuclidianPoint point : eObject.points) {

                g2.setColor(point.color);

                g2.fillRect(point.point.getX(), point.point.getY(), 1, 1);
            }
        }
    }

    private float countDistance(Point p1, Point p2) {
        return (float) (Math.sqrt((p1.getX() - p2.getX()) * (p1.getX() - p2.getX()) + (p1.getY()-p2.getY())*(p1.getY()-p2.getY())));
    }

    private void countEuclidianPoints(EuclidianObject eObject) {
        getAllInnerPoints(eObject);
    }

    private void getAllInnerPoints(EuclidianObject eObject) {

        ArrayList<EuclidianPoint> ePoints = new ArrayList<>();

        float maxDistance = 0;

        for (Point boundaryPoint : eObject.boundaries.getPoints()) {

            ArrayList<Point> otherPoints = new ArrayList<>();

            for (Point boundaryPoint2 : eObject.boundaries.getPoints()) {
                if (!boundaryPoint.equals(boundaryPoint2)
                        && boundaryPoint.getY() == boundaryPoint2.getY()
                        && boundaryPoint.getX() < boundaryPoint2.getX()) {

                    otherPoints.add(boundaryPoint2);

                }
            }

            if (otherPoints.size() > 0) {
                for (int x = boundaryPoint.getX(); x <= otherPoints.get(otherPoints.size()-1).getX(); x++) {
                    Color imgColor = new Color(image.getRGB(x, boundaryPoint.getY()));
                    if (imgColor.getRed() > 128) {
                        boolean containsPoint = false;
                        for (EuclidianPoint p : ePoints) {
                            if (p.point.getX() == x && p.point.getY() == boundaryPoint.getY()) {
                                containsPoint = true;
                                break;
                            }
                        }

                        if (!containsPoint) {
                            EuclidianPoint p = new EuclidianPoint();
                            p.point = new Point(x, boundaryPoint.getY());


                            float minDistance = diagonalDistance;
                            float tempDistance = 0;
                            for (Point boundaryPoint3 : eObject.boundaries.getPoints()) {
                                tempDistance = countDistance(p.point, boundaryPoint3);
                                if (minDistance > tempDistance)
                                    minDistance = tempDistance;
                            }
                            p.nearestDistance = minDistance;
                            if (p.nearestDistance > maxDistance)
                                maxDistance = p.nearestDistance;

                            ePoints.add(p);
                        }
                    }
                }
            }
        }

        eObject.points = ePoints;
        eObject.maxDistance = maxDistance;
    }

    private Point rotateOnAngle(Point vector, int angle) {
        double angleRad = Math.toRadians(angle);
        double x = Math.cos(angleRad) * vector.getX() - Math.sin(angleRad) * vector.getX();
        double y = Math.sin(angleRad)*vector.getY() + Math.cos(angleRad)*vector.getY();
        if (x < 0) {
            if (x > 0.5)
                x=Math.ceil(x);
            else
                x=Math.floor(x);
        }
        else {
            if (x > 0.5)
                x=Math.ceil(x);
            else
                x=Math.floor(x);
        }

        if (y < 0) {
            if (y > 0.5)
                y=Math.ceil(y);
            else
                y=Math.floor(y);
        }
        else {
            if (y > 0.5)
                y=Math.ceil(y);
            else
                y=Math.floor(y);
        }


        return new Point((int) x, (int) y);
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

}
