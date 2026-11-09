package ru.mipt.bit.platformer.entities;

import com.badlogic.gdx.math.GridPoint2;
import ru.mipt.bit.platformer.command.NoOpTankCommand;
import ru.mipt.bit.platformer.command.TankCommand;
import ru.mipt.bit.platformer.level.LogicalLevel;
import ru.mipt.bit.platformer.strategy.TankControlStrategy;

import java.util.Objects;

public class Tank implements GameEntity {
    public static final int DEFAULT_HEALTH = 100;
    public static final int DEFAULT_DAMAGE = 50;
    public static final float MOVEMENT_DURATION = 0.4f;
    public static final float SHOT_INTERVAL = 0.4f;

    private final LogicalLevel level;
    private final TankControlStrategy strategy;
    private final GridPoint2 position;
    private final GridPoint2 destination;
    private final int bulletDamage;
    private final int maxHealth;
    private Orientation orientation = Orientation.RIGHT;
    private float progress = 1f;
    private float shotCooldown;
    private int health;

    public Tank(LogicalLevel level, GridPoint2 position,
                TankControlStrategy strategy, int health, int bulletDamage) {
        if (health <= 0 || bulletDamage <= 0) {
            throw new IllegalArgumentException("Health and damage must be positive");
        }
        this.level = Objects.requireNonNull(level);
        this.strategy = Objects.requireNonNull(strategy);
        this.position = new GridPoint2(position);
        this.destination = new GridPoint2(position);
        this.maxHealth = health;
        this.health = health;
        this.bulletDamage = bulletDamage;
    }

    public TankCommand createCommand() {
        if (!level.contains(this) || isMoving()) {
            return NoOpTankCommand.INSTANCE;
        }
        return Objects.requireNonNull(strategy.nextCommand(this));
    }

    public boolean tryMove(Orientation direction) {
        Objects.requireNonNull(direction);
        if (!level.contains(this) || isMoving()) {
            return false;
        }
        GridPoint2 target = new GridPoint2(position)
                .add(direction.getDx(), direction.getDy());
        // Turning is allowed even against an obstacle: an adjacent enemy
        // must be aimable without first driving into its occupied cell.
        orientation = direction;
        if (!level.tryReserveTankMovement(position, target)) {
            return false;
        }
        destination.set(target);
        progress = 0f;
        return true;
    }

    public boolean shoot() {
        if (!level.contains(this) || isMoving() || shotCooldown > 0f) {
            return false;
        }
        shotCooldown = SHOT_INTERVAL;
        level.spawnBullet(this, bulletDamage);
        return true;
    }

    public void takeDamage(int damage) {
        if (damage < 0) {
            throw new IllegalArgumentException("Damage cannot be negative");
        }
        if (!level.contains(this)) {
            return;
        }
        health = Math.max(0, health - damage);
        if (health == 0) {
            level.remove(this);
        }
    }

    @Override
    public void live(float tick) {
        LogicalLevel.requireValidTick(tick);
        if (!level.contains(this)) {
            return;
        }
        shotCooldown = Math.max(0f, shotCooldown - tick);
        if (shotCooldown < 0.000001f) {
            shotCooldown = 0f;
        }
        if (isMoving()) {
            progress = Math.min(1f, progress + tick / MOVEMENT_DURATION);
            if (1f - progress < 0.000001f) {
                progress = 1f;
            }
            if (!isMoving()) {
                level.completeTankMovement(position, destination);
                position.set(destination);
            }
        }
    }

    public boolean occupies(GridPoint2 cell) {
        return position.equals(cell) || destination.equals(cell);
    }

    public boolean isMoving() { return progress < 1f; }
    public int getHealth() { return health; }
    public int getMaxHealth() { return maxHealth; }
    public Orientation getOrientation() { return orientation; }
    public float getMovementProgress() { return progress; }
    public GridPoint2 getDestination() { return new GridPoint2(destination); }

    @Override
    public GridPoint2 getPosition() { return new GridPoint2(position); }
}
