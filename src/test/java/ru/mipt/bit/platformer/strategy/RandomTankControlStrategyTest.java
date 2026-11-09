package ru.mipt.bit.platformer.strategy;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.Test;
import ru.mipt.bit.platformer.command.*;
import ru.mipt.bit.platformer.entities.*;
import ru.mipt.bit.platformer.level.LogicalLevel;
import java.util.Random;
import static org.junit.jupiter.api.Assertions.*;

class RandomTankControlStrategyTest {
    @Test
    void shouldChooseAllFourDirectionsAndShooting() {
        for (int choice = 0; choice < 5; choice++) {
            int selected = choice;
            LogicalLevel level = new LogicalLevel(4, 4);
            Tank tank = level.addTank(new GridPoint2(1, 1), t -> NoOpTankCommand.INSTANCE);
            Random random = new Random() {
                public int nextInt(int bound) {
                    assertEquals(5, bound);
                    return selected;
                }
            };
            TankCommand command = new RandomTankControlStrategy(random).nextCommand(tank);
            command.execute();
            if (choice == 4) {
                assertInstanceOf(ShootTankCommand.class, command);
                assertEquals(1, level.getEntities().stream().filter(Bullet.class::isInstance).count());
            } else {
                Orientation direction = Orientation.values()[choice];
                assertEquals(new GridPoint2(1 + direction.getDx(), 1 + direction.getDy()),
                        tank.getDestination());
            }
        }
    }
}
