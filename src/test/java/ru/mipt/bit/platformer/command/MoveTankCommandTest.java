package ru.mipt.bit.platformer.command;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.Test;
import ru.mipt.bit.platformer.TestGameField;
import ru.mipt.bit.platformer.entities.Orientation;
import ru.mipt.bit.platformer.entities.TankMovement;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MoveTankCommandTest {

    @Test
    void shouldAskReceiverToMoveInConfiguredDirection() {
        TankMovement tank = new TankMovement(
                new TestGameField(3, 3),
                new GridPoint2(1, 1),
                ignored -> NoOpTankCommand.INSTANCE
        );
        MoveTankCommand command = new MoveTankCommand(
                tank,
                Orientation.UP
        );

        command.execute();

        assertEquals(
                new GridPoint2(1, 2),
                tank.getDestination()
        );
        assertEquals(Orientation.UP, tank.getOrientation());
    }
}
