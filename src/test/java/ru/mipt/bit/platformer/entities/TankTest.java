package ru.mipt.bit.platformer.entities;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import ru.mipt.bit.platformer.command.*;
import ru.mipt.bit.platformer.level.LogicalLevel;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

class TankTest {
    @Test
    void shouldCopyPositionAndRegisterTank() {
        LogicalLevel level = new LogicalLevel(3, 3);
        GridPoint2 initial = new GridPoint2(1, 1);
        Tank tank = level.addTank(initial, t -> NoOpTankCommand.INSTANCE);
        initial.set(9, 9);
        tank.getPosition().set(8, 8);
        tank.getDestination().set(7, 7);
        assertEquals(new GridPoint2(1, 1), tank.getPosition());
        assertEquals(Orientation.RIGHT, tank.getOrientation());
        assertTrue(level.isBlocked(new GridPoint2(1, 1)));
    }

    @Test
    void shouldReserveBothCellsAndReleaseSourceAfterArrival() {
        LogicalLevel level = new LogicalLevel(3, 2);
        Tank tank = level.addTank(new GridPoint2(0, 0),
                t -> new MoveTankCommand(t, Orientation.RIGHT));
        level.executeCommands(level.createCommands());
        level.live(.2f);
        assertEquals(new GridPoint2(0, 0), tank.getPosition());
        assertEquals(new GridPoint2(1, 0), tank.getDestination());
        assertEquals(.5f, tank.getMovementProgress(), .0001f);
        assertTrue(level.isBlocked(new GridPoint2(0, 0)));
        assertTrue(level.isBlocked(new GridPoint2(1, 0)));
        Tank second = level.addTank(new GridPoint2(1, 1), t -> NoOpTankCommand.INSTANCE);
        assertFalse(second.tryMove(Orientation.DOWN));
        level.live(.2f);
        assertEquals(new GridPoint2(1, 0), tank.getPosition());
        assertFalse(level.isBlocked(new GridPoint2(0, 0)));
        assertTrue(level.isBlocked(new GridPoint2(1, 0)));
    }

    @Test
    void shouldSeparateCommandCollectionExecutionAndTime() {
        LogicalLevel level = new LogicalLevel(3, 1);
        AtomicInteger requests = new AtomicInteger();
        Tank tank = level.addTank(new GridPoint2(0, 0), t -> {
            requests.incrementAndGet();
            return new MoveTankCommand(t, Orientation.RIGHT);
        });
        List<TankCommand> commands = level.createCommands();
        assertFalse(tank.isMoving());
        level.executeCommands(commands);
        assertEquals(0f, tank.getMovementProgress());
        level.live(.1f);
        level.createCommands(); // moving tanks don't poll the strategy
        assertEquals(1, requests.get());
        assertEquals(.25f, tank.getMovementProgress(), .0001f);
    }

    @ParameterizedTest
    @EnumSource(Orientation.class)
    void shouldNotCrossAnyBoundary(Orientation orientation) {
        LogicalLevel level = new LogicalLevel(1, 1);
        Tank tank = level.addTank(new GridPoint2(), t -> NoOpTankCommand.INSTANCE);
        assertFalse(tank.tryMove(orientation));
        assertEquals(new GridPoint2(), tank.getPosition());
    }

    @Test
    void shouldNotMoveThroughTreesOrStationaryTanks() {
        LogicalLevel level = new LogicalLevel(3, 2);
        Tank tank = level.addTank(new GridPoint2(), t -> NoOpTankCommand.INSTANCE);
        level.addTree(new GridPoint2(1, 0));
        level.addTank(new GridPoint2(0, 1), t -> NoOpTankCommand.INSTANCE);
        assertFalse(tank.tryMove(Orientation.RIGHT));
        assertFalse(tank.tryMove(Orientation.UP));
    }

    @Test
    void destructionShouldReleaseBothCellsAndInvalidateCollectedCommands() {
        LogicalLevel level = new LogicalLevel(3, 1);
        Tank tank = level.addTank(new GridPoint2(), t -> NoOpTankCommand.INSTANCE);
        TankCommand staleShot = new ShootTankCommand(tank);
        tank.tryMove(Orientation.RIGHT);
        tank.takeDamage(100);
        staleShot.execute();
        tank.live(1);
        assertTrue(level.getEntities().isEmpty());
        assertFalse(level.isBlocked(new GridPoint2()));
        assertFalse(level.isBlocked(new GridPoint2(1, 0)));
        assertFalse(tank.tryMove(Orientation.RIGHT));
    }

    @Test
    void shotCooldownShouldLimitHeldSpace() {
        LogicalLevel level = new LogicalLevel(10, 1);
        Tank tank = level.addTank(new GridPoint2(), t -> NoOpTankCommand.INSTANCE);
        assertTrue(tank.shoot());
        assertFalse(tank.shoot());
        level.live(.2f);
        assertFalse(tank.shoot());
        level.live(.2f);
        assertTrue(tank.shoot());
    }

    @Test
    void healthAndDamageShouldBeConfigurable() {
        LogicalLevel level = new LogicalLevel(2, 1);
        Tank shooter = level.addTank(new GridPoint2(), t -> NoOpTankCommand.INSTANCE, 80, 35);
        Tank target = level.addTank(new GridPoint2(1, 0), t -> NoOpTankCommand.INSTANCE, 30, 10);
        new ShootTankCommand(shooter).execute();
        assertEquals(80, shooter.getHealth());
        assertEquals(0, target.getHealth());
        assertFalse(level.contains(target));
        assertFalse(level.isBlocked(target.getPosition()));
    }

    @Test
    void invalidTicksShouldBeRejected() {
        LogicalLevel level = new LogicalLevel(1, 1);
        for (float tick : new float[]{-1, Float.NaN, Float.POSITIVE_INFINITY}) {
            assertThrows(IllegalArgumentException.class, () -> level.live(tick));
        }
    }
}
