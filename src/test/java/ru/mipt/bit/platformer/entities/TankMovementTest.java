package ru.mipt.bit.platformer.entities;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.Test;
import ru.mipt.bit.platformer.TestGameField;
import ru.mipt.bit.platformer.command.MoveTankCommand;
import ru.mipt.bit.platformer.command.NoOpTankCommand;
import ru.mipt.bit.platformer.strategy.TankControlStrategy;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TankMovementTest {

    @Test
    void shouldRegisterTankAndKeepInitialState() {
        TestGameField field = new TestGameField(3, 3);
        GridPoint2 initialPosition = new GridPoint2(1, 1);

        TankMovement tank = new TankMovement(
                field,
                initialPosition,
                ignored -> NoOpTankCommand.INSTANCE
        );

        assertEquals(initialPosition, tank.getPosition());
        assertEquals(initialPosition, tank.getDestination());
        assertEquals(Orientation.RIGHT, tank.getOrientation());
        assertTrue(field.isBlocked(initialPosition));
    }

    @Test
    void shouldMoveUsingCommandFromStrategy() {
        TestGameField field = new TestGameField(3, 1);
        GridPoint2 source = new GridPoint2(0, 0);
        GridPoint2 destination = new GridPoint2(1, 0);
        TankControlStrategy moveRight = tank ->
                new MoveTankCommand(tank, Orientation.RIGHT);
        TankMovement tank = new TankMovement(
                field,
                source,
                moveRight
        );

        tank.update(0.2f);

        assertEquals(source, tank.getPosition());
        assertEquals(destination, tank.getDestination());
        assertEquals(0.5f, tank.getMovementProgress());
        assertTrue(field.isBlocked(source));
        assertTrue(field.isBlocked(destination));

        tank.update(0.2f);

        assertEquals(destination, tank.getPosition());
        assertFalse(field.isBlocked(source));
        assertTrue(field.isBlocked(destination));
    }

    @Test
    void shouldNotRequestAnotherCommandWhileMoving() {
        TestGameField field = new TestGameField(3, 1);
        AtomicInteger commandRequests = new AtomicInteger();
        TankControlStrategy strategy = tank -> {
            commandRequests.incrementAndGet();
            return new MoveTankCommand(tank, Orientation.RIGHT);
        };
        TankMovement tank = new TankMovement(
                field,
                new GridPoint2(0, 0),
                strategy
        );

        tank.update(0.1f);
        tank.update(0.1f);

        assertEquals(1, commandRequests.get());
    }

    @Test
    void shouldStayInsideFieldWhenStrategyTargetsBoundary() {
        TestGameField field = new TestGameField(1, 1);
        GridPoint2 initialPosition = new GridPoint2(0, 0);
        TankMovement tank = new TankMovement(
                field,
                initialPosition,
                receiver -> new MoveTankCommand(
                        receiver,
                        Orientation.RIGHT
                )
        );

        tank.update(1f);

        assertEquals(initialPosition, tank.getPosition());
        assertEquals(initialPosition, tank.getDestination());
        assertEquals(1f, tank.getMovementProgress());
    }

    @Test
    void shouldReleaseBothReservedCellsWhenDisposedDuringMovement() {
        TestGameField field = new TestGameField(2, 1);
        GridPoint2 source = new GridPoint2(0, 0);
        GridPoint2 destination = new GridPoint2(1, 0);
        TankMovement tank = new TankMovement(
                field,
                source,
                ignored -> NoOpTankCommand.INSTANCE
        );
        tank.tryMove(Orientation.RIGHT);

        tank.dispose();

        assertFalse(field.isBlocked(source));
        assertFalse(field.isBlocked(destination));
    }

    @Test
    void shouldRejectNegativeDeltaTime() {
        TankMovement tank = new TankMovement(
                new TestGameField(1, 1),
                new GridPoint2(0, 0),
                ignored -> NoOpTankCommand.INSTANCE
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> tank.update(-0.1f)
        );
    }
}
