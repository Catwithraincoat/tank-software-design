package ru.mipt.bit.platformer.command;

import ru.mipt.bit.platformer.entities.Orientation;
import ru.mipt.bit.platformer.entities.Tank;

import java.util.Objects;

public class MoveTankCommand implements TankCommand {

    private final Tank tank;
    private final Orientation orientation;

    public MoveTankCommand(
            Tank tank,
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
