package ru.mipt.bit.platformer.strategy;

import ru.mipt.bit.platformer.command.MoveTankCommand;
import ru.mipt.bit.platformer.command.NoOpTankCommand;
import ru.mipt.bit.platformer.command.TankCommand;
import ru.mipt.bit.platformer.command.ShootTankCommand;
import ru.mipt.bit.platformer.entities.Orientation;
import ru.mipt.bit.platformer.entities.Tank;
import ru.mipt.bit.platformer.input.InputController;

import java.util.Objects;

public class KeyboardTankControlStrategy
        implements TankControlStrategy {

    private final InputController inputController;

    public KeyboardTankControlStrategy(
            InputController inputController
    ) {
        this.inputController = Objects.requireNonNull(
                inputController,
                "inputController"
        );
    }

    @Override
    public TankCommand nextCommand(Tank tank) {
        if (inputController.isShootPressed()) {
            return new ShootTankCommand(tank);
        }
        Orientation pressedOrientation =
                inputController.getPressedOrientation();

        if (pressedOrientation == null) {
            return NoOpTankCommand.INSTANCE;
        }

        return new MoveTankCommand(
                tank,
                pressedOrientation
        );
    }
}
