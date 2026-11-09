package ru.mipt.bit.platformer.entities;

import com.badlogic.gdx.math.GridPoint2;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Keeps the non-graphical state of occupied field cells.
 */
public class FieldOccupancy {

    private final int width;
    private final int height;
    private final Set<GridPoint2> obstacles = new HashSet<>();
    private final Set<GridPoint2> tanks = new HashSet<>();

    public FieldOccupancy(int width, int height) {
        if (width <= 0 || height <= 0) {
            throw new IllegalArgumentException(
                    "Field dimensions must be positive"
            );
        }

        this.width = width;
        this.height = height;
    }

    public boolean isBlocked(GridPoint2 position) {
        Objects.requireNonNull(position, "position");

        return isOutsideField(position)
                || obstacles.contains(position)
                || tanks.contains(position);
    }

    public void addObstacle(GridPoint2 position) {
        requireInsideField(position);

        if (tanks.contains(position)) {
            throw new IllegalArgumentException(
                    "A tank already occupies " + position
            );
        }

        obstacles.add(new GridPoint2(position));
    }

    public void addTank(GridPoint2 position) {
        Objects.requireNonNull(position, "position");

        if (isBlocked(position)) {
            throw new IllegalArgumentException(
                    "Tank cannot occupy " + position
            );
        }

        tanks.add(new GridPoint2(position));
    }

    public boolean tryReserveTankMovement(
            GridPoint2 fromPosition,
            GridPoint2 toPosition
    ) {
        requireRegisteredTank(fromPosition);
        requireAdjacent(fromPosition, toPosition);

        if (isBlocked(toPosition)) {
            return false;
        }

        // The source stays occupied until completeTankMovement is called.
        tanks.add(new GridPoint2(toPosition));
        return true;
    }

    public void completeTankMovement(
            GridPoint2 fromPosition,
            GridPoint2 toPosition
    ) {
        requireRegisteredTank(fromPosition);
        requireRegisteredTank(toPosition);
        requireAdjacent(fromPosition, toPosition);

        tanks.remove(fromPosition);
    }

    public void removeTank(GridPoint2 position) {
        Objects.requireNonNull(position, "position");
        tanks.remove(position);
    }

    private void requireRegisteredTank(GridPoint2 position) {
        Objects.requireNonNull(position, "position");

        if (!tanks.contains(position)) {
            throw new IllegalStateException(
                    "No tank occupies " + position
            );
        }
    }

    private void requireAdjacent(
            GridPoint2 first,
            GridPoint2 second
    ) {
        Objects.requireNonNull(second, "toPosition");

        int distance = Math.abs(first.x - second.x)
                + Math.abs(first.y - second.y);

        if (distance != 1) {
            throw new IllegalArgumentException(
                    "Tank movement must target an adjacent cell"
            );
        }
    }

    private void requireInsideField(GridPoint2 position) {
        Objects.requireNonNull(position, "position");

        if (isOutsideField(position)) {
            throw new IllegalArgumentException(
                    "Position is outside the field: " + position
            );
        }
    }

    private boolean isOutsideField(GridPoint2 position) {
        return position.x < 0
                || position.x >= width
                || position.y < 0
                || position.y >= height;
    }
}
