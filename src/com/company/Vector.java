package com.company;

/**
 * Created by Denis on 05.04.2016.
 */
public class Vector {

    private float x;
    private float y;

    public Vector() {
        x = 0f;
        y = 0f;
    }

    public Vector(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void normalize() {
        float length = (float)Math.sqrt(x * x + y * y);

        x /= length;
        y /= length;
    }

    public float getLength() {
        return (float)Math.sqrt(x * x + y * y);
    }
}
