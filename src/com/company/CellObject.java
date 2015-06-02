package com.company;


import java.util.ArrayList;

/**
 * Created by Denis on 11.04.2015.
 */
public class CellObject {

    private ArrayList<Point> points;
    private Point center;
    private float area;

    public CellObject()
    {
        points = new ArrayList<Point>();
        center = new Point();
        area = 0;
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
}

