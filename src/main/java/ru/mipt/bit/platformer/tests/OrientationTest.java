package ru.mipt.bit.platformer.tests;

import org.junit.jupiter.api.Test;
import ru.mipt.bit.platformer.entities.Orientation;

import static org.junit.jupiter.api.Assertions.*;

class OrientationTest {

    @Test
    void upShouldHaveCorrectValues() {
        assertEquals(0, Orientation.UP.getDx());
        assertEquals(1, Orientation.UP.getDy());
        assertEquals(90f, Orientation.UP.getRotation());
    }

    @Test
    void downShouldHaveCorrectValues() {
        assertEquals(0, Orientation.DOWN.getDx());
        assertEquals(-1, Orientation.DOWN.getDy());
        assertEquals(-90f, Orientation.DOWN.getRotation());
    }

    @Test
    void leftShouldHaveCorrectValues() {
        assertEquals(-1, Orientation.LEFT.getDx());
        assertEquals(0, Orientation.LEFT.getDy());
        assertEquals(-180f, Orientation.LEFT.getRotation());
    }

    @Test
    void rightShouldHaveCorrectValues() {
        assertEquals(1, Orientation.RIGHT.getDx());
        assertEquals(0, Orientation.RIGHT.getDy());
        assertEquals(0f, Orientation.RIGHT.getRotation());
    }

    @Test
    void shouldHaveFourOrientations() {
        assertEquals(4, Orientation.values().length);
    }
}




