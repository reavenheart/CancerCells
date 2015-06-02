package com.company;

import java.util.ArrayList;

/**
 * Created by Denis on 05.05.2015.
 */
public class SimpleCellObject3D {

    public ArrayList<SimpleVertex3D> cellVerticies;
    public ArrayList<SimplePolygon3D> cellPolygons;
    public ArrayList<SimpleTriangle3D> cellTriangles;
    public int startIndex;

    public SimpleCellObject3D() {
        cellVerticies = new ArrayList<>();
        cellPolygons = new ArrayList<>();
        cellTriangles = new ArrayList<>();
    }

    public int giveVertexIndex() {
        return startIndex + cellVerticies.size() + 1;
    }
}
