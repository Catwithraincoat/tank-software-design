package ru.mipt.bit.platformer.entities;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class OrientationTest {

    @Test
    void shouldExposeMovementAndRotation() {
        assertAll(
                () -> assertOrientation(Orientation.UP, 0, 1, 90f),
                () -> assertOrientation(Orientation.LEFT, -1, 0, -180f),
                () -> assertOrientation(Orientation.DOWN, 0, -1, -90f),
                () -> assertOrientation(Orientation.RIGHT, 1, 0, 0f)
        );
    }

    @Test
    void shouldHaveFourOrientations() {
        assertEquals(4, Orientation.values().length);
    }

    private static void assertOrientation(
            Orientation orientation,
            int dx,
            int dy,
            float rotation
    ) {
        assertEquals(dx, orientation.getDx());
        assertEquals(dy, orientation.getDy());
        assertEquals(rotation, orientation.getRotation());
    }
}
