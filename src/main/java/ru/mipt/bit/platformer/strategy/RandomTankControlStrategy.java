package ru.mipt.bit.platformer.strategy;

import ru.mipt.bit.platformer.command.MoveTankCommand;
import ru.mipt.bit.platformer.command.TankCommand;
import ru.mipt.bit.platformer.entities.Orientation;
import ru.mipt.bit.platformer.entities.TankMovement;

import java.util.Objects;
import java.util.Random;

public class RandomTankControlStrategy
        implements TankControlStrategy {

    private final Random random;

    public RandomTankControlStrategy() {
        this(new Random());
    }

    public RandomTankControlStrategy(Random random) {
        this.random = Objects.requireNonNull(random, "random");
    }

    @Override
    public TankCommand nextCommand(TankMovement tank) {
        Orientation[] orientations = Orientation.values();
        Orientation orientation = orientations[
                random.nextInt(orientations.length)
        ];

        return new MoveTankCommand(tank, orientation);
    }
}
