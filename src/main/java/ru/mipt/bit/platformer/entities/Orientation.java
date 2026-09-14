package ru.mipt.bit.platformer.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

import static com.badlogic.gdx.Input.Keys.*;

public enum Orientation {

    UP(0, 1, 90f, Input.Keys.UP, Input.Keys.W),
    LEFT(-1, 0, -180f, Input.Keys.LEFT, Input.Keys.A),
    DOWN(0, -1, -90f, Input.Keys.DOWN, Input.Keys.S),
    RIGHT(1, 0, 0f, Input.Keys.RIGHT, Input.Keys.D);

    private final int dx;
    private final int dy;
    private final float rotation;
    private final int arrowKey;
    private final int wasdKey;

    Orientation(int dx, int dy, float rotation, int arrowKey, int wasdKey) {
        this.dx = dx;
        this.dy = dy;
        this.rotation = rotation;
        this.arrowKey = arrowKey;
        this.wasdKey = wasdKey;
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

    public boolean isPressed() {
        return Gdx.input.isKeyPressed(arrowKey)
                || Gdx.input.isKeyPressed(wasdKey);
    }
}
