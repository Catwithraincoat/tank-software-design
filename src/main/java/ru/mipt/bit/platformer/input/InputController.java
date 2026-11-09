package ru.mipt.bit.platformer.input;

import ru.mipt.bit.platformer.entities.Orientation;

public interface InputController {

    Orientation getPressedOrientation();

    default boolean isShootPressed() {
        return false;
    }
}
