package ru.mipt.bit.platformer.level;

import com.badlogic.gdx.math.GridPoint2;
import ru.mipt.bit.platformer.command.TankCommand;
import ru.mipt.bit.platformer.command.GameCommand;
import ru.mipt.bit.platformer.entities.*;
import ru.mipt.bit.platformer.strategy.TankControlStrategy;

import java.util.*;
import java.util.function.Supplier;

public class LogicalLevel {
    private final int width;
    private final int height;
    private final FieldOccupancy occupancy;
    private final Set<GameEntity> entities = new LinkedHashSet<>();
    private final Set<LevelListener> listeners = new LinkedHashSet<>();

    public LogicalLevel(int width, int height) {
        occupancy = new FieldOccupancy(width, height);
        this.width = width;
        this.height = height;
    }

    /** Replays existing entities too, so initial loading also uses Observer. */
    public void addListener(LevelListener listener) {
        Objects.requireNonNull(listener);
        if (listeners.add(listener)) {
            getEntities().forEach(listener::entityAdded);
        }
    }

    public void removeListener(LevelListener listener) {
        listeners.remove(listener);
    }

    public List<GameEntity> getEntities() { return List.copyOf(entities); }
    public boolean contains(GameEntity entity) { return entities.contains(entity); }
    public boolean isBlocked(GridPoint2 position) { return occupancy.isBlocked(position); }

    public boolean isOutside(GridPoint2 position) {
        return position.x < 0 || position.y < 0
                || position.x >= width || position.y >= height;
    }

    public void initialize(LevelData data, TankControlStrategy playerStrategy,
                           Supplier<TankControlStrategy> enemyStrategies) {
        if (!entities.isEmpty()) {
            throw new IllegalStateException("Level is already initialized");
        }
        data.getTreePositions().forEach(this::addTree);
        addTank(data.getPlayerPosition(), playerStrategy);
        for (GridPoint2 position : data.getAiTankPositions()) {
            addTank(position, enemyStrategies.get());
        }
    }

    public Tree addTree(GridPoint2 position) {
        if (isBlocked(position)) {
            throw new IllegalArgumentException("Tree cell must be free");
        }
        Tree tree = new Tree(position);
        occupancy.addObstacle(position);
        register(tree);
        return tree;
    }

    public Tank addTank(GridPoint2 position, TankControlStrategy strategy) {
        return addTank(position, strategy, Tank.DEFAULT_HEALTH, Tank.DEFAULT_DAMAGE);
    }

    public Tank addTank(GridPoint2 position, TankControlStrategy strategy,
                        int health, int bulletDamage) {
        Tank tank = new Tank(this, position, strategy, health, bulletDamage);
        occupancy.addTank(position);
        register(tank);
        return tank;
    }

    public Bullet spawnBullet(Tank tank, int damage) {
        if (!contains(tank)) {
            throw new IllegalArgumentException("Shooter is not in this level");
        }
        GridPoint2 position = tank.getPosition().add(
                tank.getOrientation().getDx(), tank.getOrientation().getDy());
        Bullet bullet = new Bullet(position, tank.getOrientation(), tank, damage);
        register(bullet);
        // Adjacent tanks, trees, bullets and the edge are checked at birth.
        resolveCollision(bullet);
        return bullet;
    }

    private void register(GameEntity entity) {
        entities.add(entity);
        List.copyOf(listeners).forEach(listener -> listener.entityAdded(entity));
    }

    public void remove(GameEntity entity) {
        if (!entities.remove(entity)) {
            return;
        }
        if (entity instanceof Tank tank) {
            occupancy.removeTank(tank.getPosition());
            occupancy.removeTank(tank.getDestination());
        } else if (entity instanceof Tree) {
            occupancy.removeObstacle(entity.getPosition());
        }
        List.copyOf(listeners).forEach(listener -> listener.entityRemoved(entity));
    }

    public boolean tryReserveTankMovement(GridPoint2 from, GridPoint2 to) {
        return occupancy.tryReserveTankMovement(from, to);
    }

    public void completeTankMovement(GridPoint2 from, GridPoint2 to) {
        occupancy.completeTankMovement(from, to);
    }

    /** Phase 1: collect without executing, from a stable snapshot. */
    public List<TankCommand> createCommands() {
        List<TankCommand> commands = new ArrayList<>();
        for (GameEntity entity : getEntities()) {
            if (entity instanceof Tank tank) {
                commands.add(tank.createCommand());
            }
        }
        return List.copyOf(commands);
    }

    /** Phase 2: removed receivers ignore already collected commands. */
    public void executeCommands(List<? extends GameCommand> commands) {
        commands.forEach(GameCommand::execute);
    }

    /** Phase 3: only advance logical time; never poll input or draw. */
    public void live(float tick) {
        requireValidTick(tick);
        // A moving tank occupies both cells, including before its arrival.
        resolveAllCollisions();
        advanceBullets();
        float remaining = tick;
        while (remaining > 0f) {
            // Stop at each bullet arrival. A large frame must not finish a
            // tank's whole movement before checking an earlier bullet hit.
            float step = remaining;
            for (Bullet bullet : bullets()) {
                step = Math.min(step, bullet.timeToNextCell());
            }
            for (GameEntity entity : getEntities()) {
                if (contains(entity)) {
                    entity.live(step);
                }
            }
            resolveAllCollisions();
            advanceBullets();
            remaining = Math.max(0f, remaining - step);
        }
    }

    private List<Bullet> bullets() {
        return entities.stream().filter(Bullet.class::isInstance)
                .map(Bullet.class::cast).toList();
    }

    private void resolveAllCollisions() {
        for (Bullet bullet : bullets()) {
            resolveCollision(bullet);
        }
    }

    private boolean resolveCollision(Bullet bullet) {
        if (!contains(bullet)) {
            return true;
        }
        if (isOutside(bullet.getPosition())) {
            remove(bullet);
            return true;
        }
        for (GameEntity other : getEntities()) {
            if (other == bullet || other == bullet.getOwner()) {
                continue;
            }
            boolean collision = other instanceof Tank tank
                    ? tank.occupies(bullet.getPosition())
                    : other.getPosition().equals(bullet.getPosition());
            if (collision) {
                remove(bullet);
                if (other instanceof Tank tank) {
                    tank.takeDamage(bullet.getDamage());
                } else if (other instanceof Bullet) {
                    remove(other);
                }
                return true;
            }
        }
        return false;
    }

    private void advanceBullets() {
        // All bullets advance together per cell. Check both same-cell hits
        // and opposite bullets exchanging cells; large ticks cannot tunnel.
        while (bullets().stream().anyMatch(Bullet::isStepDue)) {
            List<Bullet> active = bullets();
            Map<Bullet, GridPoint2> targets = new LinkedHashMap<>();
            for (Bullet bullet : active) {
                targets.put(bullet, bullet.isStepDue()
                        ? bullet.getNextPosition() : bullet.getPosition());
            }
            Set<Bullet> collided = new HashSet<>();
            for (int i = 0; i < active.size(); i++) {
                Bullet first = active.get(i);
                for (int j = i + 1; j < active.size(); j++) {
                    Bullet second = active.get(j);
                    boolean sameCell = targets.get(first).equals(targets.get(second));
                    boolean crossing = targets.get(first).equals(second.getPosition())
                            && targets.get(second).equals(first.getPosition());
                    if (sameCell || crossing) {
                        collided.add(first);
                        collided.add(second);
                    }
                }
            }
            collided.forEach(this::remove);
            for (Bullet bullet : active) {
                if (contains(bullet) && bullet.isStepDue()) {
                    bullet.advance();
                }
            }
            resolveAllCollisions();
        }
    }

    public void clear() {
        getEntities().forEach(this::remove);
    }

    public static void requireValidTick(float tick) {
        if (!Float.isFinite(tick) || tick < 0f) {
            throw new IllegalArgumentException("Tick must be finite and non-negative");
        }
    }
}
