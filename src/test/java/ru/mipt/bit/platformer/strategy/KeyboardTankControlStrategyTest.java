package ru.mipt.bit.platformer.strategy;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.Test;
import ru.mipt.bit.platformer.TestGameField;
import ru.mipt.bit.platformer.command.NoOpTankCommand;
import ru.mipt.bit.platformer.command.TankCommand;
import ru.mipt.bit.platformer.entities.Orientation;
import ru.mipt.bit.platformer.entities.TankMovement;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

class KeyboardTankControlStrategyTest {

    @Test
    void shouldReturnNoOpWhenNoKeyIsPressed() {
        TankMovement tank = newTank();
        KeyboardTankControlStrategy strategy =
                new KeyboardTankControlStrategy(() -> null);

        assertSame(
                NoOpTankCommand.INSTANCE,
                strategy.nextCommand(tank)
        );
    }

    @Test
    void shouldCreateMoveCommandForPressedDirection() {
        TankMovement tank = newTank();
        KeyboardTankControlStrategy strategy =
                new KeyboardTankControlStrategy(
                        () -> Orientation.LEFT
                );

        TankCommand command = strategy.nextCommand(tank);
        command.execute();

        assertEquals(
                new GridPoint2(0, 1),
                tank.getDestination()
        );
    }

    private static TankMovement newTank() {
        return new TankMovement(
                new TestGameField(3, 3),
                new GridPoint2(1, 1),
                ignored -> NoOpTankCommand.INSTANCE
        );
    }
}
