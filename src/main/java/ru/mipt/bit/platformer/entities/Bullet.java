package ru.mipt.bit.platformer.entities;

import com.badlogic.gdx.math.GridPoint2;
import ru.mipt.bit.platformer.level.LogicalLevel;

import java.util.Objects;

public class Bullet implements GameEntity {
    public static final float CELL_DURATION = 0.1f;
    private final GridPoint2 position;
    private final Orientation orientation;
    private final Tank owner;
    private final int damage;
    private double elapsed;

    public Bullet(GridPoint2 position, Orientation orientation, Tank owner, int damage) {
        if (damage <= 0) {
            throw new IllegalArgumentException("Damage must be positive");
        }
        this.position = new GridPoint2(position);
        this.orientation = Objects.requireNonNull(orientation);
        this.owner = Objects.requireNonNull(owner);
        this.damage = damage;
    }

    @Override
    public void live(float tick) {
        LogicalLevel.requireValidTick(tick);
        elapsed += tick;
    }

    public boolean isStepDue() { return elapsed + 1e-8 >= CELL_DURATION; }

    public float timeToNextCell() {
        return (float) Math.max(0, CELL_DURATION - elapsed);
    }

    public void advance() {
        if (!isStepDue()) {
            throw new IllegalStateException("Bullet is not ready to advance");
        }
        position.set(getNextPosition());
        elapsed = Math.max(0, elapsed - CELL_DURATION);
    }

    public GridPoint2 getNextPosition() {
        return new GridPoint2(position).add(orientation.getDx(), orientation.getDy());
    }

    public float getProgress() { return (float) Math.min(1, elapsed / CELL_DURATION); }
    public Orientation getOrientation() { return orientation; }
    public Tank getOwner() { return owner; }
    public int getDamage() { return damage; }

    @Override
    public GridPoint2 getPosition() { return new GridPoint2(position); }
}
