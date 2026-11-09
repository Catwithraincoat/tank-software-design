package ru.mipt.bit.platformer.entities;

import com.badlogic.gdx.math.GridPoint2;
import ru.mipt.bit.platformer.command.TankCommand;
import ru.mipt.bit.platformer.strategy.TankControlStrategy;

import java.util.Objects;

import static com.badlogic.gdx.math.MathUtils.isEqual;
import static ru.mipt.bit.platformer.util.GdxGameUtils.continueProgress;

/**
 * Non-graphical tank movement model and command receiver.
 */
public class TankMovement {

    private static final float MOVEMENT_DURATION = 0.4f;

    private final GameField field;
    private final TankControlStrategy controlStrategy;
    private final GridPoint2 position;
    private final GridPoint2 destination;

    private Orientation orientation = Orientation.RIGHT;
    private float movementProgress = 1f;

    public TankMovement(
            GameField field,
            GridPoint2 initialPosition,
            TankControlStrategy controlStrategy
    ) {
        this.field = Objects.requireNonNull(field, "field");
        this.controlStrategy = Objects.requireNonNull(
                controlStrategy,
                "controlStrategy"
        );
        this.position = new GridPoint2(
                Objects.requireNonNull(initialPosition, "initialPosition")
        );
        this.destination = new GridPoint2(initialPosition);

        field.addTank(this.position);
    }

    public void update(float deltaTime) {
        if (deltaTime < 0f) {
            throw new IllegalArgumentException(
                    "deltaTime cannot be negative"
            );
        }

        if (isReadyForCommand()) {
            TankCommand command = Objects.requireNonNull(
                    controlStrategy.nextCommand(this),
                    "A control strategy cannot return null"
            );
            command.execute();
        }

        if (isReadyForCommand()) {
            return;
        }

        movementProgress = continueProgress(
                movementProgress,
                deltaTime,
                MOVEMENT_DURATION
        );

        if (isReadyForCommand()) {
            field.completeTankMovement(position, destination);
            position.set(destination);
        }
    }

    public boolean tryMove(Orientation newOrientation) {
        Objects.requireNonNull(newOrientation, "newOrientation");

        if (!isReadyForCommand()) {
            return false;
        }

        GridPoint2 newDestination = new GridPoint2(position)
                .add(
                        newOrientation.getDx(),
                        newOrientation.getDy()
                );

        if (!field.tryReserveTankMovement(
                position,
                newDestination
        )) {
            return false;
        }

        orientation = newOrientation;
        destination.set(newDestination);
        movementProgress = 0f;
        return true;
    }

    public GridPoint2 getPosition() {
        return new GridPoint2(position);
    }

    public GridPoint2 getDestination() {
        return new GridPoint2(destination);
    }

    public Orientation getOrientation() {
        return orientation;
    }

    public float getMovementProgress() {
        return movementProgress;
    }

    public void dispose() {
        field.removeTank(position);

        if (!position.equals(destination)) {
            field.removeTank(destination);
        }
    }

    private boolean isReadyForCommand() {
        return isEqual(movementProgress, 1f);
    }
}
