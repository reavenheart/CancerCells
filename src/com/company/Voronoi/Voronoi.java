package com.company.Voronoi;

import com.company.Point;
import com.sun.javafx.iio.ImageStorage;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Created by Denis on 02.06.2015.
 */
public class Voronoi {

    private Layout layout;

    public void countVoronoi(ArrayList<Point> centers, int imgWidth, int imgHeight, BufferedImage img) {

        //BufferedImage img = new BufferedImage(imgWidth, imgHeight, BufferedImage.TYPE_INT_RGB);
        layout = new Layout(imgWidth, imgHeight, true, false);
        layout.g = img.createGraphics();

        for (Point p : centers) {
            Pnt point = new Pnt(p.getX(), p.getY());
            //System.out.println("Center at " + point);
            layout.addSite(point);
        }

        layout.paintComponent();

        /*
        try {
            ImageIO.write(img, "JPEG", new File("foo.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        */

    }

    private class Layout  {

        public Color voronoiColor = Color.BLACK;
        public Color delaunayColor = new Color(0,0,0,0);
        public int pointRadius = 3;

        private int width = 0;
        private int height = 0;
        private boolean isVoronoi = false;
        private boolean isColorful = false;

        private Triangulation dt;                   // Delaunay triangulation
        private Map<Object, Color> colorTable;      // Remembers colors for display
        private Triangle initialTriangle;           // Initial triangle
        private int initialSize = 10000;     // Size of initial triangle
        public Graphics2D g;                         // Stored graphics context
        private Random random = new Random();       // Source of random numbers

        /**
         * Create and initialize the DT.
         */
        public Layout() {
            initialTriangle = new Triangle(
                    new Pnt(-initialSize, -initialSize),
                    new Pnt(initialSize, -initialSize),
                    new Pnt(0, initialSize));
            dt = new Triangulation(initialTriangle);
            colorTable = new HashMap<Object, Color>();
        }

        public Layout(int width, int height, boolean isVoronoi, boolean isColorful) {

            this.width = width;
            this.height = height;
            this.isVoronoi = isVoronoi;
            this.isColorful = isColorful;

            initialTriangle = new Triangle(
                    new Pnt(-initialSize, -initialSize),
                    new Pnt(initialSize, -initialSize),
                    new Pnt(0, initialSize));
            dt = new Triangulation(initialTriangle);
            colorTable = new HashMap<Object, Color>();
        }

        /**
         * Add a new site to the DT.
         *
         * @param point the site to add
         */
        public void addSite(Pnt point) {
            dt.delaunayPlace(point);
        }

        /**
         * Re-initialize the DT.
         */
        public void clear() {
            dt = new Triangulation(initialTriangle);
        }

        /**
         * Get the color for the spcified item; generate a new color if necessary.
         *
         * @param item we want the color for this item
         * @return item's color
         */
        private Color getColor(Object item) {
            if (colorTable.containsKey(item)) return colorTable.get(item);
            Color color = new Color(Color.HSBtoRGB(random.nextFloat(), 1.0f, 1.0f));
            colorTable.put(item, color);
            return color;
        }

    /* Basic Drawing Methods */

        /**
         * Draw a point.
         *
         * @param point the Pnt to draw
         */
        public void draw(Pnt point) {
            int r = pointRadius;
            int x = (int) point.coord(0);
            int y = (int) point.coord(1);
            g.fillOval(x - r, y - r, r + r, r + r);
        }

        /**
         * Draw a circle.
         *
         * @param center    the center of the circle
         * @param radius    the circle's radius
         * @param fillColor null implies no fill
         */
        public void draw(Pnt center, double radius, Color fillColor) {
            int x = (int) center.coord(0);
            int y = (int) center.coord(1);
            int r = (int) radius;
            if (fillColor != null) {
                Color temp = g.getColor();
                g.setColor(fillColor);
                g.fillOval(x - r, y - r, r + r, r + r);
                g.setColor(temp);
            }
            g.drawOval(x - r, y - r, r + r, r + r);
        }

        /**
         * Draw a polygon.
         *
         * @param polygon   an array of polygon vertices
         * @param fillColor null implies no fill
         */
        public void draw(Pnt[] polygon, Color fillColor) {
            int[] x = new int[polygon.length];
            int[] y = new int[polygon.length];
            for (int i = 0; i < polygon.length; i++) {
                x[i] = (int) polygon[i].coord(0);
                y[i] = (int) polygon[i].coord(1);
            }
            if (fillColor != null) {
                //Color temp = g.getColor();
                //g.setColor(fillColor);
                g.drawPolygon(x, y, polygon.length);
                //g.setColor(temp);
            }
            g.setColor(Color.BLACK);
            g.setStroke(new BasicStroke(2));
            g.drawPolygon(x, y, polygon.length);
        }

    /* Higher Level Drawing Methods */

        public void paintComponent() {

            // Flood the drawing area with a "background" color
            Color temp = g.getColor();
            if (isVoronoi) g.setColor(delaunayColor);
            else if (dt.contains(initialTriangle)) g.setColor(Color.BLACK);
            else g.setColor(voronoiColor);
            g.fillRect(0, 0, width, height);
            g.setColor(temp);

            // If no colors then we can clear the color table
            if (!isColorful) colorTable.clear();

            // Draw the appropriate picture
            if (isVoronoi)
                drawAllVoronoi(isColorful, true);
            else drawAllDelaunay(isColorful);

            // Draw any extra info due to the mouse-entry switches
            temp = g.getColor();
            //g.setColor(Color.BLACK);
            //if (controller.showingCircles()) drawAllCircles();
            //if (controller.showingDelaunay()) drawAllDelaunay(false);
            //drawAllVoronoi(false, false);
            //g.setColor(temp);
        }

        /**
         * Draw all the Delaunay triangles.
         *
         * @param withFill true iff drawing Delaunay triangles with fill colors
         */
        public void drawAllDelaunay(boolean withFill) {
            for (Triangle triangle : dt) {
                Pnt[] vertices = triangle.toArray(new Pnt[0]);
                draw(vertices, withFill ? getColor(triangle) : null);
            }
        }

        /**
         * Draw all the Voronoi cells.
         *
         * @param withFill  true iff drawing Voronoi cells with fill colors
         * @param withSites true iff drawing the site for each Voronoi cell
         */
        public void drawAllVoronoi(boolean withFill, boolean withSites) {
            // Keep track of sites done; no drawing for initial triangles sites
            HashSet<Pnt> done = new HashSet<Pnt>(initialTriangle);
            for (Triangle triangle : dt)
                for (Pnt site : triangle) {
                    if (done.contains(site)) continue;
                    done.add(site);
                    java.util.List<Triangle> list = dt.surroundingTriangles(site, triangle);
                    Pnt[] vertices = new Pnt[list.size()];
                    int i = 0;
                    for (Triangle tri : list)
                        vertices[i++] = tri.getCircumcenter();
                    draw(vertices, withFill ? getColor(site) : null);
                    //if (withSites) draw(site);
                }
        }

        /**
         * Draw all the empty circles (one for each triangle) of the DT.
         */
        public void drawAllCircles() {
            // Loop through all triangles of the DT
            for (Triangle triangle : dt) {
                // Skip circles involving the initial-triangle vertices
                if (triangle.containsAny(initialTriangle)) continue;
                Pnt c = triangle.getCircumcenter();
                double radius = c.subtract(triangle.get(0)).magnitude();
                draw(c, radius, null);
            }
        }
    }

}
