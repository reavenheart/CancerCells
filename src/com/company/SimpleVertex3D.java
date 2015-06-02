package com.company;

import java.util.ArrayList;

/**
 * Created by Denis on 04.05.2015.
 */
public class SimpleVertex3D extends SimplePoint3D {
    public SimplePoint3D normal;

    public int index;

    public SimpleVertex3D () {
        normal = new SimplePoint3D();
        index = 0;
    }

    public void countNormal() {

    }

    public void countNormal(ArrayList<SimplePoint3D> faceNormals) {
        for (SimplePoint3D faceNormal : faceNormals) {
            normal.x += faceNormal.x;
            normal.y += faceNormal.y;
            normal.z += faceNormal.z;
        }

        float vectorLength = (float)Math.sqrt(normal.x * normal.x + normal.y * normal.y + normal.z * normal.z);

        normal.x = normal.x/ vectorLength;
        normal.y = normal.y/ vectorLength;
        normal.z = normal.z/ vectorLength;
    }
}
