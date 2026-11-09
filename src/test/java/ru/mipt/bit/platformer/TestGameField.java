package ru.mipt.bit.platformer;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Rectangle;
import ru.mipt.bit.platformer.entities.FieldOccupancy;
import ru.mipt.bit.platformer.entities.GameField;

public class TestGameField implements GameField {

    private final int width;
    private final int height;
    private final FieldOccupancy occupancy;

    public TestGameField(int width, int height) {
        this.width = width;
        this.height = height;
        this.occupancy = new FieldOccupancy(width, height);
    }

    @Override
    public void render() {
        // Graphics are deliberately excluded from unit tests.
    }

    @Override
    public boolean isBlocked(GridPoint2 position) {
        return occupancy.isBlocked(position);
    }

    @Override
    public void addObstacle(GridPoint2 position) {
        occupancy.addObstacle(position);
    }

    @Override
    public void addTank(GridPoint2 position) {
        occupancy.addTank(position);
    }

    @Override
    public boolean tryReserveTankMovement(
            GridPoint2 fromPosition,
            GridPoint2 toPosition
    ) {
        return occupancy.tryReserveTankMovement(
                fromPosition,
                toPosition
        );
    }

    @Override
    public void completeTankMovement(
            GridPoint2 fromPosition,
            GridPoint2 toPosition
    ) {
        occupancy.completeTankMovement(
                fromPosition,
                toPosition
        );
    }

    @Override
    public void removeTank(GridPoint2 position) {
        occupancy.removeTank(position);
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public void moveRectangleToTileCenter(
            Rectangle rectangle,
            GridPoint2 position
    ) {
        // Graphics are deliberately excluded from unit tests.
    }

    @Override
    public void moveRectangleBetweenTileCenters(
            Rectangle rectangle,
            GridPoint2 fromPosition,
            GridPoint2 toPosition,
            float progress
    ) {
        // Graphics are deliberately excluded from unit tests.
    }

    @Override
    public void dispose() {
        // No resources to release.
    }
}
