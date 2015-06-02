package com.company;

/**
 * Created by Denis on 04.05.2015.
 */
public class SimplePolygon3D extends SimpleVertex3D {

    public SimpleVertex3D v1;
    public SimpleVertex3D v2;
    public SimpleVertex3D v3;
    public SimpleVertex3D v4;

    public SimplePolygon3D() {
        v1 = new SimpleVertex3D();
        v2 = new SimpleVertex3D();
        v3 = new SimpleVertex3D();
        v4 = new SimpleVertex3D();

        normal = new SimplePoint3D();
    }

    @Override
    public void countNormal() {
        float[] nVector1 = new float[3];
        float[] nVector2 = new float[3];

        float vectorLength = (float)Math.sqrt(
                (v2.x - v1.x)*(v2.x - v1.x) +
                        (v2.y - v1.y)*(v2.y - v1.y) +
                        (v2.z - v1.z)*(v2.z - v1.z)
        );

        if (vectorLength != 0) {

            nVector1[0] = (v2.x - v1.x) / vectorLength;
            nVector1[1] = (v2.y - v1.y) / vectorLength;
            nVector1[2] = (v2.z - v1.z) / vectorLength;
        }
        else
            return;


        vectorLength = (float)Math.sqrt(
                (v4.x - v1.x)*(v4.x - v1.x) +
                        (v4.y - v1.y)*(v4.y - v1.y) +
                        (v4.z - v1.z)*(v4.z - v1.z)
        );

        if (vectorLength != 0) {
            nVector2[0] = (v3.x - v1.x) / vectorLength;
            nVector2[1] = (v3.y - v1.y) / vectorLength;
            nVector2[2] = (v3.z - v1.z) / vectorLength;
        }
        else
            return;

        normal.x = nVector2[1]*nVector1[2] - nVector2[2]*nVector1[1];
        normal.y = nVector2[2]*nVector1[0] - nVector2[0]*nVector1[2];
        normal.z = nVector2[0]*nVector1[1] - nVector2[1]*nVector1[0];

    }
}
