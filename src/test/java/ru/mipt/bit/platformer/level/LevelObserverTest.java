package ru.mipt.bit.platformer.level;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.Test;
import ru.mipt.bit.platformer.command.NoOpTankCommand;
import ru.mipt.bit.platformer.entities.*;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

class LevelObserverTest {
    static class RecordingListener implements LevelListener {
        final List<GameEntity> added = new ArrayList<>();
        final List<GameEntity> removed = new ArrayList<>();
        public void entityAdded(GameEntity entity) { added.add(entity); }
        public void entityRemoved(GameEntity entity) { removed.add(entity); }
    }

    @Test
    void initialLoadingShouldUseSameNotificationsAsRuntimeObjects() {
        LogicalLevel level = new LogicalLevel(5, 3);
        RecordingListener listener = new RecordingListener();
        level.addListener(listener);
        level.initialize(new LevelData(new GridPoint2(),
                        List.of(new GridPoint2(2, 2)), List.of(new GridPoint2(4, 2))),
                t -> NoOpTankCommand.INSTANCE, () -> t -> NoOpTankCommand.INSTANCE);
        assertEquals(level.getEntities(), listener.added);
        Tank player = (Tank) level.getEntities().get(1);
        player.shoot();
        assertInstanceOf(Bullet.class, listener.added.get(3));
        level.clear();
        assertEquals(new HashSet<>(listener.added), new HashSet<>(listener.removed));
        assertTrue(level.getEntities().isEmpty());
    }

    @Test
    void lateSubscriptionShouldReplayObjectsOnceAndUnsubscribeShouldStopEvents() {
        LogicalLevel level = new LogicalLevel(3, 1);
        Tree tree = level.addTree(new GridPoint2());
        RecordingListener listener = new RecordingListener();
        level.addListener(listener);
        level.addListener(listener);
        assertEquals(List.of(tree), listener.added);
        level.remove(tree);
        level.remove(tree);
        assertEquals(List.of(tree), listener.removed);
        level.removeListener(listener);
        level.addTree(new GridPoint2(1, 0));
        assertEquals(1, listener.added.size());
    }

    @Test
    void immediateHitShouldEmitBulletAdditionThenRemovalAndTankRemoval() {
        LogicalLevel level = new LogicalLevel(2, 1);
        Tank shooter = level.addTank(new GridPoint2(), t -> NoOpTankCommand.INSTANCE, 100, 100);
        Tank target = level.addTank(new GridPoint2(1, 0), t -> NoOpTankCommand.INSTANCE);
        RecordingListener listener = new RecordingListener();
        level.addListener(listener);
        shooter.shoot();
        GameEntity bullet = listener.added.get(2);
        assertInstanceOf(Bullet.class, bullet);
        assertEquals(List.of(bullet, target), listener.removed);
    }
}
