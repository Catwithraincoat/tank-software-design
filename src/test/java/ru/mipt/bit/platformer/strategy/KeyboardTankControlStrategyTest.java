package ru.mipt.bit.platformer.strategy;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.Test;
import ru.mipt.bit.platformer.command.*;
import ru.mipt.bit.platformer.entities.*;
import ru.mipt.bit.platformer.input.InputController;
import ru.mipt.bit.platformer.level.LogicalLevel;
import static org.junit.jupiter.api.Assertions.*;

class KeyboardTankControlStrategyTest {
    @Test
    void noInputShouldReturnNoOp() {
        Tank tank = newTank();
        assertSame(NoOpTankCommand.INSTANCE,
                new KeyboardTankControlStrategy(() -> null).nextCommand(tank));
    }

    @Test
    void directionShouldCreateMoveCommand() {
        Tank tank = newTank();
        new KeyboardTankControlStrategy(() -> Orientation.LEFT).nextCommand(tank).execute();
        assertEquals(new GridPoint2(0, 1), tank.getDestination());
    }

    @Test
    void spaceShouldCreateShotAndTakePriorityOverDirection() {
        Tank tank = newTank();
        InputController input = new InputController() {
            public Orientation getPressedOrientation() { return Orientation.UP; }
            public boolean isShootPressed() { return true; }
        };
        TankCommand command = new KeyboardTankControlStrategy(input).nextCommand(tank);
        assertInstanceOf(ShootTankCommand.class, command);
        assertFalse(tank.isMoving());
        command.execute();
        assertFalse(tank.isMoving());
    }

    private Tank newTank() {
        return new LogicalLevel(4, 3).addTank(
                new GridPoint2(1, 1), t -> NoOpTankCommand.INSTANCE);
    }
}
