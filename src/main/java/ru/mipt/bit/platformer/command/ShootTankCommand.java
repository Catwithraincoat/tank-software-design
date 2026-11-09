package ru.mipt.bit.platformer.command;

import ru.mipt.bit.platformer.entities.Tank;
import java.util.Objects;

public class ShootTankCommand implements TankCommand {
    private final Tank tank;

    public ShootTankCommand(Tank tank) {
        this.tank = Objects.requireNonNull(tank);
    }

    @Override
    public void execute() {
        tank.shoot();
    }
}
