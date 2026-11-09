package ru.mipt.bit.platformer.command;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.Test;
import ru.mipt.bit.platformer.entities.*;
import ru.mipt.bit.platformer.level.LogicalLevel;
import static org.junit.jupiter.api.Assertions.*;

class MoveTankCommandTest {
    @Test
    void shouldDeferMovementUntilExecuted() {
        LogicalLevel level = new LogicalLevel(3, 3);
        Tank tank = level.addTank(new GridPoint2(1, 1), t -> NoOpTankCommand.INSTANCE);
        TankCommand command = new MoveTankCommand(tank, Orientation.UP);
        assertFalse(tank.isMoving());
        command.execute();
        assertEquals(new GridPoint2(1, 2), tank.getDestination());
        assertEquals(Orientation.UP, tank.getOrientation());
    }
}
