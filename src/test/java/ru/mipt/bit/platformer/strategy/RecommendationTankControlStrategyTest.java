package ru.mipt.bit.platformer.strategy;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import ru.mipt.bit.platformer.ai.*;
import ru.mipt.bit.platformer.command.*;
import ru.mipt.bit.platformer.entities.*;
import ru.mipt.bit.platformer.level.LogicalLevel;
import java.util.Random;
import static org.junit.jupiter.api.Assertions.*;

class RecommendationTankControlStrategyTest {
    @ParameterizedTest
    @EnumSource(Recommendation.class)
    void adapterShouldTranslateEveryRecommendation(Recommendation recommendation) {
        LogicalLevel level = new LogicalLevel(5, 5);
        Tank tank = level.addTank(new GridPoint2(2, 2), t -> NoOpTankCommand.INSTANCE);
        TankCommand command = new RecommendationTankControlStrategy(
                ahead -> recommendation, level).nextCommand(tank);
        switch (recommendation) {
            case FIRE -> assertInstanceOf(ShootTankCommand.class, command);
            case WAIT -> assertSame(NoOpTankCommand.INSTANCE, command);
            default -> {
                command.execute();
                Orientation direction = Orientation.values()[recommendation.ordinal()];
                assertEquals(direction, tank.getOrientation());
                assertEquals(new GridPoint2(2 + direction.getDx(), 2 + direction.getDy()),
                        tank.getDestination());
            }
        }
    }

    @Test
    void engineShouldRecommendShotWhenTankVisibleAhead() {
        LogicalLevel level = new LogicalLevel(5, 2);
        Tank tank = level.addTank(new GridPoint2(), t -> NoOpTankCommand.INSTANCE);
        level.addTank(new GridPoint2(4, 0), t -> NoOpTankCommand.INSTANCE);
        RecommendationTankControlStrategy adapter = new RecommendationTankControlStrategy(
                new SimpleRecommendationEngine(new Random(1)), level);
        assertInstanceOf(ShootTankCommand.class, adapter.nextCommand(tank));
    }

    @Test
    void treeShouldHideTankFromRecommendationEngine() {
        LogicalLevel level = new LogicalLevel(5, 2);
        Tank tank = level.addTank(new GridPoint2(), t -> NoOpTankCommand.INSTANCE);
        level.addTree(new GridPoint2(2, 0));
        level.addTank(new GridPoint2(4, 0), t -> NoOpTankCommand.INSTANCE);
        new RecommendationTankControlStrategy(ahead -> {
            assertFalse(ahead);
            return Recommendation.WAIT;
        }, level).nextCommand(tank);
    }

    @Test
    void engineShouldAlsoChooseRandomShotsWithoutVisibleTarget() {
        for (int choice = 0; choice < 5; choice++) {
            int selected = choice;
            Random random = new Random() {
                public int nextInt(int bound) {
                    assertEquals(5, bound);
                    return selected;
                }
            };
            assertEquals(Recommendation.values()[choice],
                    new SimpleRecommendationEngine(random).recommend(false));
        }
    }
}
