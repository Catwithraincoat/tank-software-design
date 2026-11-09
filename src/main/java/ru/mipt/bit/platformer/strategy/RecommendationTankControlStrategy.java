package ru.mipt.bit.platformer.strategy;

import com.badlogic.gdx.math.GridPoint2;
import ru.mipt.bit.platformer.ai.RecommendationEngine;
import ru.mipt.bit.platformer.command.*;
import ru.mipt.bit.platformer.entities.*;
import ru.mipt.bit.platformer.level.LogicalLevel;

import java.util.Objects;

/** Adapter: converts the recommendation engine's vocabulary to commands. */
public class RecommendationTankControlStrategy implements TankControlStrategy {
    private final RecommendationEngine engine;
    private final LogicalLevel level;

    public RecommendationTankControlStrategy(RecommendationEngine engine,
                                             LogicalLevel level) {
        this.engine = Objects.requireNonNull(engine);
        this.level = Objects.requireNonNull(level);
    }

    @Override
    public TankCommand nextCommand(Tank tank) {
        return switch (engine.recommend(hasTargetAhead(tank))) {
            case NORTH -> new MoveTankCommand(tank, Orientation.UP);
            case WEST -> new MoveTankCommand(tank, Orientation.LEFT);
            case SOUTH -> new MoveTankCommand(tank, Orientation.DOWN);
            case EAST -> new MoveTankCommand(tank, Orientation.RIGHT);
            case FIRE -> new ShootTankCommand(tank);
            case WAIT -> NoOpTankCommand.INSTANCE;
        };
    }

    private boolean hasTargetAhead(Tank tank) {
        GridPoint2 cell = tank.getPosition();
        Orientation orientation = tank.getOrientation();
        while (true) {
            cell.add(orientation.getDx(), orientation.getDy());
            if (level.isOutside(cell)) {
                return false;
            }
            for (GameEntity entity : level.getEntities()) {
                if (entity instanceof Tree && entity.getPosition().equals(cell)) {
                    return false;
                }
                if (entity instanceof Tank other && other != tank && other.occupies(cell)) {
                    return true;
                }
            }
        }
    }
}
