package ru.mipt.bit.platformer.strategy;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.Test;
import ru.mipt.bit.platformer.TestGameField;
import ru.mipt.bit.platformer.command.NoOpTankCommand;
import ru.mipt.bit.platformer.command.TankCommand;
import ru.mipt.bit.platformer.entities.Orientation;
import ru.mipt.bit.platformer.entities.TankMovement;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RandomTankControlStrategyTest {

    @Test
    void shouldCreateCommandForRandomlySelectedOrientation() {
        TankMovement tank = new TankMovement(
                new TestGameField(3, 3),
                new GridPoint2(1, 1),
                ignored -> NoOpTankCommand.INSTANCE
        );
        Random fixedRandom = new Random() {
            @Override
            public int nextInt(int bound) {
                return Orientation.DOWN.ordinal();
            }
        };
        RandomTankControlStrategy strategy =
                new RandomTankControlStrategy(fixedRandom);

        TankCommand command = strategy.nextCommand(tank);
        command.execute();

        assertEquals(
                new GridPoint2(1, 0),
                tank.getDestination()
        );
        assertEquals(Orientation.DOWN, tank.getOrientation());
    }
}
