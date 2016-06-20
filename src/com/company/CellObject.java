package com.company;


import java.util.ArrayList;

/**
 * Created by Denis on 11.04.2015.
 */
public class CellObject {

    private ArrayList<Point> points;
    private Point center;
    private float area;
    private int zIndex;

    public CellObject()
    {
        points = new ArrayList<Point>();
        center = new Point();
        area = 0;
        zIndex = 0;
    }

    public ArrayList<Point> getPoints() {
        return points;
    }

    public void setPoints(ArrayList<Point> points) {
        this.points = points;
    }

    public Point getCenter() {
        return center;
    }

    public void setCenter(Point center) {
        this.center = center;
    }

    public float getArea() {
        return area;
    }

    public void setArea(float area) {
        this.area = area;
    }

    public int getzIndex() {
        return zIndex;
    }

    public void setzIndex(int zIndex) {
        this.zIndex = zIndex;
    }

    public Point[] getBorderRect() {
        Point[] rect = new Point[]{null, null, null, null};

        for (Point p : points) {
            if (rect[0] == null)
                rect[0] = p;
            else if (p.getX() < rect[0].getX())
                rect[0] = p;

            if (rect[1] == null)
                rect[1] = p;
            else if (p.getX() > rect[1].getX())
                rect[1] = p;

            if (rect[2] == null)
                rect[2] = p;
            else if (p.getY() < rect[2].getY())
                rect[2] = p;

            if (rect[3] == null)
                rect[3] = p;
            else if (p.getY() > rect[3].getY())
                rect[3] = p;
        }

        return rect;
    }

    public boolean intersects(CellObject other) {

        Point[] rect = getBorderRect();

        if (other.getCenter().getX() >= rect[0].getX() &&
                other.getCenter().getX() <= rect[1].getX() &&
                other.getCenter().getY() >= rect[2].getY() &&
                other.getCenter().getY() <= rect[3].getY())
            return true;

        return false;
    }
}

