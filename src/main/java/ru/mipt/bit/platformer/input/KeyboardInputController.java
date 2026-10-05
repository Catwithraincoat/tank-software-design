package ru.mipt.bit.platformer.input;

import com.badlogic.gdx.Gdx;
import ru.mipt.bit.platformer.entities.Orientation;

public class KeyboardInputController implements InputController {

    @Override
    public Orientation getPressedOrientation() {
        for (Orientation orientation : Orientation.values()) {
            if (orientation.isPressed()) {
                return orientation;
            }
        }

        return null;
    }
}