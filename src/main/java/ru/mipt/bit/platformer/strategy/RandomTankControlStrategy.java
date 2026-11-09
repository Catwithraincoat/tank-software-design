package ru.mipt.bit.platformer.strategy;

import ru.mipt.bit.platformer.command.MoveTankCommand;
import ru.mipt.bit.platformer.command.TankCommand;
import ru.mipt.bit.platformer.command.ShootTankCommand;
import ru.mipt.bit.platformer.entities.Orientation;
import ru.mipt.bit.platformer.entities.Tank;

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
    public TankCommand nextCommand(Tank tank) {
        Orientation[] orientations = Orientation.values();
        int choice = random.nextInt(orientations.length + 1);
        if (choice == orientations.length) {
            return new ShootTankCommand(tank);
        }
        Orientation orientation = orientations[choice];

        return new MoveTankCommand(tank, orientation);
    }
}
