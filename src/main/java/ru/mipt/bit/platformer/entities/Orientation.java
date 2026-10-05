package ru.mipt.bit.platformer.entities;

public enum Orientation {

    UP(0, 1, 90f),
    LEFT(-1, 0, -180f),
    DOWN(0, -1, -90f),
    RIGHT(1, 0, 0f);

    private final int dx;
    private final int dy;
    private final float rotation;

    Orientation(int dx, int dy, float rotation) {
        this.dx = dx;
        this.dy = dy;
        this.rotation = rotation;
    }

    public int getDx() {
        return dx;
    }

    public int getDy() {
        return dy;
    }

    public float getRotation() {
        return rotation;
    }
}