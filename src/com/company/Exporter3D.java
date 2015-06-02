package com.company;

import java.io.*;
import java.util.ArrayList;

/**
 * Created by Denis on 04.05.2015.
 */
public class Exporter3D {

    private int numberOfEdges = 12;

    private int zEdges;

    private int lastIndex;

    public void exportData(ArrayList<CellObject> objects, int width, int height, boolean isRed, boolean isGreen, boolean isBlue) {

        ArrayList<SimpleCellObject3D> cells = new ArrayList<>();

        if (numberOfEdges % 2 == 0)
            zEdges = (numberOfEdges - 1) / 2;
        else
            zEdges = numberOfEdges / 2;

        boolean isFound = false;

        for (CellObject cell : objects) {
            Point center = cell.getCenter();

            SimpleCellObject3D cell3D = new SimpleCellObject3D();
            cell3D.startIndex = lastIndex;

            float[] directionVector = new float[2];
            float[] directionVector2 = new float[2];

            directionVector[0] = 1;
            directionVector[1] = 0;

            float[] temp = new float[2];

            temp[0] = directionVector[0];
            temp[1] = directionVector[1];

            float stepAngle = 360 / numberOfEdges;

            float edgePos = 0;

            float aproxRadius = 0;

            isFound = false;

            for (int edge = 0; edge < zEdges; edge++) {
                if (edge == 0) {

                    for (int i = 0; i < numberOfEdges; i++) {

                        float angle = (float) Math.toRadians(stepAngle * i);

                        Point point = new Point(center.getX(), center.getY());

                        directionVector2[0] = (float) (directionVector[0] * Math.cos(angle) - directionVector[1] * Math.sin(angle));
                        directionVector2[1] = (float) (directionVector[0] * Math.sin(angle) + directionVector[1] * Math.cos(angle));

                        float pX = point.getX();
                        float pY = point.getY();

                        // могут найтись не все. Получу потом OutOfBoundariesArray
                        while (!isFound && (point.getX() < width && point.getX() > 0) && (point.getY() < height && point.getY() > 0)) {

                            pX += directionVector2[0];
                            pY += directionVector2[1];

                            point.setX((int) pX);
                            point.setY((int) pY);

                            for (Point p : cell.getPoints()) {
                                if (p.getX() - 1 <= point.getX() && p.getY() - 1 <= point.getY() &&
                                        p.getX() + 1 >= point.getX() && p.getY() + 1 >= point.getY()) {
                                    isFound = true;
                                    break;
                                }
                            }
                        }

                        if (isFound) {
                            SimpleVertex3D vertex = new SimpleVertex3D();

                            aproxRadius = (aproxRadius + (float) Math.sqrt(point.getX() * point.getX() + point.getY() * point.getY())) / (i + 1);

                            vertex.x = point.getX();
                            vertex.y = point.getY();
                            vertex.z = edgePos;

                            vertex.index = cell3D.giveVertexIndex();
                            lastIndex = vertex.index;

                            cell3D.cellVerticies.add(vertex);
                        } else
                            break;
                    }

                    if (!isFound)
                        break;
                } else if (edge < zEdges - 1) {
                    float scale1 = (float) (edge) / zEdges;
                    float scale2 = (float) (edge) / zEdges;

                    scale2 = (float) Math.sqrt(1 - (scale1 - 1) * (scale1 - 1));

                    edgePos = scale2 * aproxRadius;

                    ArrayList<SimpleVertex3D> tempArray = new ArrayList<>();

                    for (int k = 0; k < cell3D.cellVerticies.size(); k++) {
                        SimpleVertex3D vertex = new SimpleVertex3D();
                        vertex.x = cell3D.cellVerticies.get(k).x;
                        vertex.y = cell3D.cellVerticies.get(k).y;
                        vertex.z = cell3D.cellVerticies.get(k).z;

                        tempArray.add(vertex);
                    }

                    for (SimpleVertex3D vertex : tempArray) {

                        vertex.x = vertex.x - (vertex.x - center.getX()) * scale1;
                        vertex.y = vertex.y - (vertex.y - center.getY()) * scale1;
                        vertex.z = edgePos;

                        vertex.index = cell3D.giveVertexIndex();
                        lastIndex = vertex.index;

                        cell3D.cellVerticies.add(vertex);
                    }
                } else {
                    SimpleVertex3D vertex = new SimpleVertex3D();

                    vertex.x = center.getX();
                    vertex.y = center.getY();
                    vertex.z = aproxRadius;

                    vertex.index = cell3D.giveVertexIndex();
                    lastIndex = vertex.index;

                    cell3D.cellVerticies.add(vertex);
                }

            }

            if (isFound) {

            for (int i = 0; i < cell3D.cellVerticies.size() - numberOfEdges - 1; i += numberOfEdges) {
                for (int j = 0; j < numberOfEdges; j++) {
                    SimplePolygon3D polygon = new SimplePolygon3D();

                    if (j < numberOfEdges - 1) {
                        polygon.v1 = cell3D.cellVerticies.get(i + j);
                        polygon.v2 = cell3D.cellVerticies.get(i + numberOfEdges + j);
                        polygon.v3 = cell3D.cellVerticies.get(i + numberOfEdges + j + 1);
                        polygon.v4 = cell3D.cellVerticies.get(i + j + 1);
                    } else {
                        polygon.v1 = cell3D.cellVerticies.get(i + j);
                        polygon.v2 = cell3D.cellVerticies.get(i + numberOfEdges + j);
                        polygon.v3 = cell3D.cellVerticies.get(i + numberOfEdges);
                        polygon.v4 = cell3D.cellVerticies.get(i);
                    }

                    polygon.countNormal();

                    cell3D.cellPolygons.add(polygon);
                }
            }

            for (int i = cell3D.cellVerticies.size() - numberOfEdges - 1; i < cell3D.cellVerticies.size() - 1; i++) {
                SimpleTriangle3D triangle = new SimpleTriangle3D();
                if (i < cell3D.cellVerticies.size() - 2) {
                    triangle.v1 = cell3D.cellVerticies.get(i);
                    triangle.v2 = cell3D.cellVerticies.get(cell3D.cellVerticies.size() - 1);
                    triangle.v3 = cell3D.cellVerticies.get(i + 1);
                } else {
                    triangle.v1 = cell3D.cellVerticies.get(i);
                    triangle.v2 = cell3D.cellVerticies.get(cell3D.cellVerticies.size() - 1);
                    triangle.v3 = cell3D.cellVerticies.get(cell3D.cellVerticies.size() - numberOfEdges - 1);
                }

                triangle.countNormal();

                cell3D.cellTriangles.add(triangle);
            }

                int vertSize = cell3D.cellVerticies.size();

                for (int i = numberOfEdges; i < vertSize; i++) {
                    SimpleVertex3D vertex = new SimpleVertex3D();

                    vertex.x = cell3D.cellVerticies.get(i).x;
                    vertex.y = cell3D.cellVerticies.get(i).y;
                    vertex.z = -cell3D.cellVerticies.get(i).z;

                    vertex.normal.x = cell3D.cellVerticies.get(i).normal.x;
                    vertex.normal.y = cell3D.cellVerticies.get(i).normal.y;
                    vertex.normal.z = -cell3D.cellVerticies.get(i).normal.z;

                    vertex.index = cell3D.giveVertexIndex();
                    lastIndex = vertex.index;

                    cell3D.cellVerticies.add(vertex);
                }

                for (int i = 0; i < cell3D.cellVerticies.size() - numberOfEdges - 1; i += numberOfEdges) {
                    for (int j = 0; j < numberOfEdges; j++) {
                        SimplePolygon3D polygon = new SimplePolygon3D();

                        if (i == 0) {
                            if (j < numberOfEdges - 1) {

                                polygon.v1 = cell3D.cellVerticies.get(i + vertSize + j);
                                polygon.v2 = cell3D.cellVerticies.get(i + j);
                                polygon.v3 = cell3D.cellVerticies.get(i + j + 1);
                                polygon.v4 = cell3D.cellVerticies.get(i + vertSize + j + 1);

                            } else {

                                polygon.v1 = cell3D.cellVerticies.get(i + vertSize + j);
                                polygon.v2 = cell3D.cellVerticies.get(i + j);
                                polygon.v3 = cell3D.cellVerticies.get(i);
                                polygon.v4 = cell3D.cellVerticies.get(i + vertSize);

                            }
                        } else {
                            if (j < numberOfEdges - 1) {

                                polygon.v1 = cell3D.cellVerticies.get(i + numberOfEdges + j);
                                polygon.v2 = cell3D.cellVerticies.get(i + j);
                                polygon.v3 = cell3D.cellVerticies.get(i + j + 1);
                                polygon.v4 = cell3D.cellVerticies.get(i + numberOfEdges + j + 1);

                            } else {

                                polygon.v1 = cell3D.cellVerticies.get(i + numberOfEdges + j);
                                polygon.v2 = cell3D.cellVerticies.get(i + j);
                                polygon.v3 = cell3D.cellVerticies.get(i);
                                polygon.v4 = cell3D.cellVerticies.get(i + numberOfEdges);

                            }
                        }

                        polygon.countNormal();

                        cell3D.cellPolygons.add(polygon);
                    }
                    if (i == 0)
                        i = vertSize - numberOfEdges;
                }

                for (int i = cell3D.cellVerticies.size() - numberOfEdges - 1; i < cell3D.cellVerticies.size() - 1; i++) {
                    SimpleTriangle3D triangle = new SimpleTriangle3D();
                    if (i < cell3D.cellVerticies.size() - 2) {
                        triangle.v3 = cell3D.cellVerticies.get(i);
                        triangle.v2 = cell3D.cellVerticies.get(cell3D.cellVerticies.size() - 1);
                        triangle.v1 = cell3D.cellVerticies.get(i + 1);
                    } else {
                        triangle.v3 = cell3D.cellVerticies.get(i);
                        triangle.v2 = cell3D.cellVerticies.get(cell3D.cellVerticies.size() - 1);
                        triangle.v1 = cell3D.cellVerticies.get(cell3D.cellVerticies.size() - numberOfEdges - 1);
                    }

                    triangle.countNormal();

                    cell3D.cellTriangles.add(triangle);
                }

                recountVerticiesNormals(cell3D.cellVerticies, cell3D.cellPolygons, cell3D.cellTriangles);
                scale(cell3D.cellVerticies, width, height);

                cells.add(cell3D);
            }
        }

        saveData(isRed, isGreen, isBlue, cells);
    }

    private void scale(ArrayList<SimpleVertex3D> cellVerticies, int width, int height) {

        float scale = 0;
        if (width > height)
            scale = 100f/width;
        else
            scale = 100f/height;

        for (SimpleVertex3D vertex : cellVerticies) {
            vertex.x *= scale/100;
            vertex.y *= scale/100;
            vertex.z *= scale/100;
        }
    }

    private void recountVerticiesNormals(ArrayList<SimpleVertex3D> cellVerticies, ArrayList<SimplePolygon3D> cellPolygons, ArrayList<SimpleTriangle3D> cellTriangles) {
        for (SimpleVertex3D vertex : cellVerticies) {
            ArrayList<SimplePoint3D> faceNormals = new ArrayList<>();

            for (SimpleTriangle3D face1 : cellTriangles) {
                if (face1.v1.equals(vertex) ||
                        face1.v2.equals(vertex) ||
                        face1.v3.equals(vertex))
                    faceNormals.add(face1.normal);
            }

            for (SimplePolygon3D polygon1 : cellPolygons) {
                if (polygon1.v1.equals(vertex) ||
                        polygon1.v2.equals(vertex) ||
                        polygon1.v3.equals(vertex) ||
                        polygon1.v4.equals(vertex))
                    faceNormals.add(polygon1.normal);
            }

            vertex.countNormal(faceNormals);
        }
    }

    private void saveData(boolean isRed, boolean isGreen, boolean isBlue, ArrayList<SimpleCellObject3D> cells) {
        PrintWriter writer = null;
        try {
            if (isRed)
                writer = new PrintWriter("assets/red.obj", "UTF-8");
            else if (isGreen)
                writer = new PrintWriter("assets/green.obj", "UTF-8");
            else if (isBlue)
                writer = new PrintWriter("assets/blue.obj", "UTF-8");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        writer.println("# List of geometric vertices, with (x,y,z[,w]) coordinates, w is optional and defaults to 1.0.");

        for (SimpleCellObject3D cell : cells)
            for (SimpleVertex3D vertex : cell.cellVerticies) {
                writer.println("v " + vertex.x + " " + vertex.y + " " + vertex.z);
            }

        writer.println("# List of vertex normals in (x,y,z) form; normals might not be unit vectors.");

        for (SimpleCellObject3D cell : cells)
            for (SimpleVertex3D vertex : cell.cellVerticies) {
                writer.println("vn " + vertex.normal.x + " " + vertex.normal.y + " " + vertex.normal.z);
        }

        for (SimpleCellObject3D cell : cells)
            for (SimpleTriangle3D triangle : cell.cellTriangles) {
                writer.println("f " + triangle.v1.index + "/" + "/" + triangle.v1.index + " "
                        + triangle.v2.index + "/" + "/" + triangle.v2.index + " "
                        + triangle.v3.index + "/" + "/" + triangle.v3.index);
            }

        for (SimpleCellObject3D cell : cells)
            for (SimplePolygon3D polygon : cell.cellPolygons) {
                writer.println("f " + polygon.v1.index + "/" + "/" + polygon.v1.index + " "
                        + polygon.v2.index + "/" + "/" + polygon.v2.index + " "
                        + polygon.v3.index + "/" + "/" + polygon.v3.index + " "
                        + polygon.v4.index + "/" + "/" + polygon.v4.index);
            }

        writer.close();
    }
}
