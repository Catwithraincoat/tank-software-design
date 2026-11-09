package ru.mipt.bit.platformer.entities;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FieldOccupancyTest {

    @Test
    void shouldTreatEveryCellOutsideTheFieldAsBlocked() {
        FieldOccupancy occupancy = new FieldOccupancy(3, 2);

        assertTrue(occupancy.isBlocked(new GridPoint2(-1, 0)));
        assertTrue(occupancy.isBlocked(new GridPoint2(3, 0)));
        assertTrue(occupancy.isBlocked(new GridPoint2(0, -1)));
        assertTrue(occupancy.isBlocked(new GridPoint2(0, 2)));
        assertFalse(occupancy.isBlocked(new GridPoint2(2, 1)));
    }

    @Test
    void shouldTreatObstaclesAsBlocked() {
        FieldOccupancy occupancy = new FieldOccupancy(3, 2);
        GridPoint2 obstacle = new GridPoint2(1, 1);

        occupancy.addObstacle(obstacle);

        assertTrue(occupancy.isBlocked(obstacle));
    }

    @Test
    void shouldReserveBothCellsWhileTankIsMoving() {
        FieldOccupancy occupancy = new FieldOccupancy(3, 1);
        GridPoint2 source = new GridPoint2(0, 0);
        GridPoint2 destination = new GridPoint2(1, 0);
        GridPoint2 otherTank = new GridPoint2(2, 0);
        occupancy.addTank(source);
        occupancy.addTank(otherTank);

        assertTrue(occupancy.tryReserveTankMovement(
                source,
                destination
        ));
        assertTrue(occupancy.isBlocked(source));
        assertTrue(occupancy.isBlocked(destination));
        assertFalse(occupancy.tryReserveTankMovement(
                otherTank,
                destination
        ));
    }

    @Test
    void shouldReleaseSourceOnlyAfterMovementCompletes() {
        FieldOccupancy occupancy = new FieldOccupancy(2, 1);
        GridPoint2 source = new GridPoint2(0, 0);
        GridPoint2 destination = new GridPoint2(1, 0);
        occupancy.addTank(source);
        occupancy.tryReserveTankMovement(source, destination);

        occupancy.completeTankMovement(source, destination);

        assertFalse(occupancy.isBlocked(source));
        assertTrue(occupancy.isBlocked(destination));
    }

    @Test
    void shouldRejectMovementBeyondTheField() {
        FieldOccupancy occupancy = new FieldOccupancy(1, 1);
        GridPoint2 tank = new GridPoint2(0, 0);
        occupancy.addTank(tank);

        assertFalse(occupancy.tryReserveTankMovement(
                tank,
                new GridPoint2(1, 0)
        ));
    }

    @Test
    void shouldAllowOnlyMovementToAnAdjacentCell() {
        FieldOccupancy occupancy = new FieldOccupancy(3, 1);
        GridPoint2 tank = new GridPoint2(0, 0);
        occupancy.addTank(tank);

        assertThrows(
                IllegalArgumentException.class,
                () -> occupancy.tryReserveTankMovement(
                        tank,
                        new GridPoint2(2, 0)
                )
        );
    }
}
