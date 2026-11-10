package ru.mipt.bit.platformer.command;

import ru.mipt.bit.platformer.graphics.HealthBarVisibility;

import java.util.Objects;

public class ToggleHealthBarsCommand implements GameCommand {

    private final HealthBarVisibility visibility;

    public ToggleHealthBarsCommand(HealthBarVisibility visibility) {
        this.visibility = Objects.requireNonNull(visibility, "visibility");
    }

    @Override
    public void execute() {
        visibility.toggle();
    }
}
