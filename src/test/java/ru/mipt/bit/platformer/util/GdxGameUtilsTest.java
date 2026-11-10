package ru.mipt.bit.platformer.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GdxGameUtilsTest {

    @Test
    void shouldAdvanceAndClampMovementProgress() {
        assertEquals(
                0.5f,
                GdxGameUtils.continueProgress(0f, 0.2f, 0.4f)
        );
        assertEquals(
                1f,
                GdxGameUtils.continueProgress(0.8f, 1f, 0.4f)
        );
    }
}
