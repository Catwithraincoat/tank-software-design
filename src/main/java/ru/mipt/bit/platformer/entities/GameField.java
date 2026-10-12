
package ru.mipt.bit.platformer.entities;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Rectangle;

public interface GameField {

    void render();

    boolean isBlocked(GridPoint2 position);

    void addObstacle(GridPoint2 position);

    int getWidth();

    int getHeight();

    void moveRectangleToTileCenter(
            Rectangle rectangle,
            GridPoint2 position
    );

    void moveRectangleBetweenTileCenters(
            Rectangle rectangle,
            GridPoint2 fromPosition,
            GridPoint2 toPosition,
            float progress
    );

    void dispose();
}
