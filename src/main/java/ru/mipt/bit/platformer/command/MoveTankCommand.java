package ru.mipt.bit.platformer.command;

import ru.mipt.bit.platformer.entities.Orientation;
import ru.mipt.bit.platformer.entities.TankMovement;

import java.util.Objects;

public class MoveTankCommand implements TankCommand {

    private final TankMovement tank;
    private final Orientation orientation;

    public MoveTankCommand(
            TankMovement tank,
            Orientation orientation
    ) {
        this.tank = Objects.requireNonNull(tank, "tank");
        this.orientation = Objects.requireNonNull(
                orientation,
                "orientation"
        );
    }

    @Override
    public void execute() {
        tank.tryMove(orientation);
    }
}
