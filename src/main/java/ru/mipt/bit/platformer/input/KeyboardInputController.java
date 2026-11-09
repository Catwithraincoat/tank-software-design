package ru.mipt.bit.platformer.input;

import com.badlogic.gdx.Gdx;
import ru.mipt.bit.platformer.entities.Orientation;
import ru.mipt.bit.platformer.input.InputController;

import static com.badlogic.gdx.Input.Keys.A;
import static com.badlogic.gdx.Input.Keys.D;
import static com.badlogic.gdx.Input.Keys.DOWN;
import static com.badlogic.gdx.Input.Keys.LEFT;
import static com.badlogic.gdx.Input.Keys.RIGHT;
import static com.badlogic.gdx.Input.Keys.S;
import static com.badlogic.gdx.Input.Keys.UP;
import static com.badlogic.gdx.Input.Keys.W;

public class KeyboardInputController implements InputController {

    @Override
    public Orientation getPressedOrientation() {

        if (Gdx.input.isKeyPressed(UP)
                || Gdx.input.isKeyPressed(W)) {
            return Orientation.UP;
        }

        if (Gdx.input.isKeyPressed(LEFT)
                || Gdx.input.isKeyPressed(A)) {
            return Orientation.LEFT;
        }

        if (Gdx.input.isKeyPressed(DOWN)
                || Gdx.input.isKeyPressed(S)) {
            return Orientation.DOWN;
        }

        if (Gdx.input.isKeyPressed(RIGHT)
                || Gdx.input.isKeyPressed(D)) {
            return Orientation.RIGHT;
        }

        return null;
    }
}