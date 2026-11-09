package ru.mipt.bit.platformer.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ru.mipt.bit.platformer.entities.Orientation;

import java.lang.reflect.Proxy;
import java.util.Set;
import java.util.stream.Stream;
import static com.badlogic.gdx.Input.Keys.*;
import static org.junit.jupiter.api.Assertions.*;

class KeyboardInputControllerTest {
    private final Input previousInput = Gdx.input;

    @AfterEach
    void restoreInput() {
        Gdx.input = previousInput;
    }

    private void press(Set<Integer> keys) {
        Gdx.input = (Input) Proxy.newProxyInstance(Input.class.getClassLoader(),
                new Class<?>[]{Input.class}, (proxy, method, args) -> {
                    if (method.getName().equals("isKeyPressed")) {
                        return keys.contains((Integer) args[0]);
                    }
                    throw new AssertionError("Unexpected input method: " + method.getName());
                });
    }

    static Stream<Arguments> movementKeys() {
        return Stream.of(
                Arguments.of(UP, Orientation.UP), Arguments.of(W, Orientation.UP),
                Arguments.of(LEFT, Orientation.LEFT), Arguments.of(A, Orientation.LEFT),
                Arguments.of(DOWN, Orientation.DOWN), Arguments.of(S, Orientation.DOWN),
                Arguments.of(RIGHT, Orientation.RIGHT), Arguments.of(D, Orientation.RIGHT));
    }

    @ParameterizedTest
    @MethodSource("movementKeys")
    void shouldReadArrowsAndWasd(int key, Orientation direction) {
        press(Set.of(key));
        KeyboardInputController input = new KeyboardInputController();
        assertEquals(direction, input.getPressedOrientation());
        assertFalse(input.isShootPressed());
    }

    @Test
    void spaceShouldRequestShooting() {
        press(Set.of(SPACE));
        KeyboardInputController input = new KeyboardInputController();
        assertTrue(input.isShootPressed());
        assertNull(input.getPressedOrientation());
    }

    @Test
    void noKeysShouldMeanNoCommand() {
        press(Set.of());
        KeyboardInputController input = new KeyboardInputController();
        assertFalse(input.isShootPressed());
        assertNull(input.getPressedOrientation());
    }
}
