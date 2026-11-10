package ru.mipt.bit.platformer.entities;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Rectangle;

/** Geometry and rendering only; occupancy belongs to LogicalLevel. */
public interface GameField {
    void render();
    int getWidth();
    int getHeight();
    void moveRectangleToTileCenter(Rectangle rectangle, GridPoint2 position);
    void moveRectangleBetweenTileCenters(Rectangle rectangle, GridPoint2 from,
                                         GridPoint2 to, float progress);
    void dispose();
}
