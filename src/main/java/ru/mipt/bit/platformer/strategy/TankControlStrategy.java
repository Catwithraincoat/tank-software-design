package ru.mipt.bit.platformer.strategy;

import ru.mipt.bit.platformer.command.TankCommand;
import ru.mipt.bit.platformer.entities.Tank;

public interface TankControlStrategy {

    TankCommand nextCommand(Tank tank);
}
