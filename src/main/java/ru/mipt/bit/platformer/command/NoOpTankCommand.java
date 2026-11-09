package ru.mipt.bit.platformer.command;

public enum NoOpTankCommand implements TankCommand {

    INSTANCE;

    @Override
    public void execute() {
        // Intentionally does nothing.
    }
}
