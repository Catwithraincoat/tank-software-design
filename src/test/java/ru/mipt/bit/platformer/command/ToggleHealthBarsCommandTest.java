package ru.mipt.bit.platformer.command;

import org.junit.jupiter.api.Test;
import ru.mipt.bit.platformer.graphics.HealthBarVisibility;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ToggleHealthBarsCommandTest {

    @Test
    void shouldToggleVisibilityEveryTimeItIsExecuted() {
        HealthBarVisibility visibility = new HealthBarVisibility();
        ToggleHealthBarsCommand command =
                new ToggleHealthBarsCommand(visibility);

        assertFalse(visibility.isVisible());
        command.execute();
        assertTrue(visibility.isVisible());
        command.execute();
        assertFalse(visibility.isVisible());
    }
}
