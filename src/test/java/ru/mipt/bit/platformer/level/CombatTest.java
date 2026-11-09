package ru.mipt.bit.platformer.level;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import ru.mipt.bit.platformer.command.*;
import ru.mipt.bit.platformer.entities.*;

import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class CombatTest {
    private Tank tank(LogicalLevel level, int x, int y) {
        return level.addTank(new GridPoint2(x, y), t -> NoOpTankCommand.INSTANCE);
    }

    private Tank leftFacing(LogicalLevel level, int x) {
        Tank tank = tank(level, x + 1, 0);
        assertTrue(tank.tryMove(Orientation.LEFT));
        level.live(Tank.MOVEMENT_DURATION);
        return tank;
    }

    private List<Bullet> bullets(LogicalLevel level) {
        return level.getEntities().stream().filter(Bullet.class::isInstance)
                .map(Bullet.class::cast).toList();
    }

    @ParameterizedTest
    @EnumSource(Orientation.class)
    void shotShouldStartInNextCellAndKeepShooterOrientation(Orientation direction) {
        LogicalLevel level = new LogicalLevel(10, 10);
        Tank shooter = tank(level, 5, 5);
        shooter.tryMove(direction);
        level.live(Tank.MOVEMENT_DURATION);
        new ShootTankCommand(shooter).execute();
        Bullet bullet = bullets(level).get(0);
        GridPoint2 expected = shooter.getPosition().add(direction.getDx(), direction.getDy());
        assertEquals(expected, bullet.getPosition());
        assertEquals(direction, bullet.getOrientation());
        level.live(Bullet.CELL_DURATION);
        assertEquals(expected.add(direction.getDx(), direction.getDy()), bullet.getPosition());
        assertEquals(100, shooter.getHealth());
    }

    @Test
    void adjacentTankShouldTakeDamageImmediately() {
        LogicalLevel level = new LogicalLevel(3, 1);
        Tank shooter = tank(level, 0, 0);
        Tank target = tank(level, 1, 0);
        shooter.shoot();
        assertEquals(50, target.getHealth());
        assertTrue(bullets(level).isEmpty());
        level.live(Tank.SHOT_INTERVAL);
        shooter.shoot();
        assertFalse(level.contains(target));
        assertTrue(bullets(level).isEmpty());
    }

    @Test
    void treeShouldAbsorbBulletWithoutTakingDamage() {
        LogicalLevel level = new LogicalLevel(5, 1);
        Tank shooter = tank(level, 0, 0);
        Tree tree = level.addTree(new GridPoint2(2, 0));
        Tank target = tank(level, 3, 0);
        shooter.shoot();
        level.live(1f);
        assertTrue(level.contains(tree));
        assertEquals(100, target.getHealth());
        assertTrue(bullets(level).isEmpty());
    }

    @Test
    void adjacentTreeShouldAbsorbShotAtBirth() {
        LogicalLevel level = new LogicalLevel(2, 1);
        Tank shooter = tank(level, 0, 0);
        level.addTree(new GridPoint2(1, 0));
        shooter.shoot();
        assertTrue(bullets(level).isEmpty());
    }

    @Test
    void shotAtBoundaryAndBulletLeavingFieldShouldDisappear() {
        LogicalLevel level = new LogicalLevel(2, 1);
        Tank shooter = tank(level, 1, 0);
        shooter.shoot();
        assertTrue(bullets(level).isEmpty());
        level.remove(shooter);
        shooter = tank(level, 0, 0);
        shooter.shoot();
        assertEquals(1, bullets(level).size());
        level.live(.1f);
        assertTrue(bullets(level).isEmpty());
    }

    @Test
    void largeTickShouldNotSkipTarget() {
        LogicalLevel level = new LogicalLevel(10, 1);
        Tank shooter = tank(level, 0, 0);
        Tank target = tank(level, 5, 0);
        shooter.shoot();
        level.live(2f);
        assertEquals(50, target.getHealth());
        assertTrue(bullets(level).isEmpty());
    }

    @Test
    void opposingBulletsShouldDisappearWhenCrossingBetweenCells() {
        LogicalLevel level = new LogicalLevel(6, 1);
        Tank right = leftFacing(level, 3);
        Tank left = tank(level, 0, 0);
        left.shoot(); // (1,0) -> right
        right.shoot(); // (2,0) -> left
        level.live(.1f);
        assertTrue(bullets(level).isEmpty());
        assertEquals(100, left.getHealth());
        assertEquals(100, right.getHealth());
    }

    @Test
    void opposingBulletsShouldDisappearOnSameDestination() {
        LogicalLevel level = new LogicalLevel(7, 1);
        Tank right = leftFacing(level, 4);
        Tank left = tank(level, 0, 0);
        left.shoot();
        right.shoot();
        level.live(.1f);
        assertTrue(bullets(level).isEmpty());
    }

    @Test
    void shotIntoExistingBulletShouldRemoveBothAtBirth() {
        LogicalLevel level = new LogicalLevel(5, 1);
        Tank right = leftFacing(level, 2);
        Tank left = tank(level, 0, 0);
        left.shoot();
        right.shoot();
        assertTrue(bullets(level).isEmpty());
    }

    @Test
    void equallyFastFollowingBulletsShouldNotCollideDueToIterationOrder() {
        LogicalLevel level = new LogicalLevel(20, 1);
        Tank shooter = tank(level, 0, 0);
        level.spawnBullet(shooter, 10);
        level.live(.1f);
        level.spawnBullet(shooter, 10);
        level.live(.1f);
        assertEquals(2, bullets(level).size());
        assertEquals(List.of(new GridPoint2(3, 0), new GridPoint2(2, 0)),
                bullets(level).stream().map(Bullet::getPosition).toList());
    }

    @Test
    void movingTanksShouldBeHitInBothReservedCells() {
        for (int targetX : new int[]{1, 2}) {
            LogicalLevel level = new LogicalLevel(5, 2);
            Tank shooter = tank(level, 0, 0);
            Tank target = tank(level, targetX, 0);
            target.tryMove(targetX == 1 ? Orientation.RIGHT : Orientation.LEFT);
            level.spawnBullet(shooter, 100);
            assertFalse(level.contains(target));
            assertFalse(level.isBlocked(new GridPoint2(1, 0)));
            assertFalse(level.isBlocked(new GridPoint2(2, 0)));
        }
    }

    @Test
    void tankEnteringBulletCellShouldBeHitBeforeMovementReleasesCell() {
        LogicalLevel level = new LogicalLevel(4, 2);
        Tank shooter = tank(level, 0, 0);
        Tank target = tank(level, 1, 1);
        shooter.shoot();
        target.tryMove(Orientation.DOWN);
        level.live(.4f);
        assertEquals(50, target.getHealth());
        assertTrue(bullets(level).isEmpty());
    }

    @Test
    void deadTankShouldNotExecutePreviouslyCollectedShot() {
        LogicalLevel level = new LogicalLevel(4, 1);
        level.addTank(new GridPoint2(0, 0), ShootTankCommand::new, 100, 100);
        Tank target = level.addTank(new GridPoint2(1, 0), ShootTankCommand::new);
        List<TankCommand> commands = level.createCommands();
        level.executeCommands(commands);
        assertFalse(level.contains(target));
        assertTrue(bullets(level).isEmpty());
    }

    @Test
    void largeTickShouldHitMovingTankBeforeItLeavesBulletPath() {
        LogicalLevel level = new LogicalLevel(6, 2);
        Tank shooter = tank(level, 0, 0);
        Tank target = tank(level, 2, 0);
        shooter.shoot();
        target.tryMove(Orientation.UP);
        level.live(.5f);
        assertEquals(50, target.getHealth());
        assertTrue(bullets(level).isEmpty());
    }

    @Test
    void tankShouldBeAbleToAimAtAdjacentEnemyWithoutMovingIntoIt() {
        LogicalLevel level = new LogicalLevel(3, 1);
        Tank shooter = tank(level, 1, 0);
        Tank target = tank(level, 0, 0);
        assertFalse(shooter.tryMove(Orientation.LEFT));
        assertEquals(Orientation.LEFT, shooter.getOrientation());
        shooter.shoot();
        assertEquals(50, target.getHealth());
        assertEquals(new GridPoint2(1, 0), shooter.getPosition());
    }
}
