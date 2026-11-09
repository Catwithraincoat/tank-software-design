package ru.mipt.bit.platformer.level;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FileLevelProviderTest {

    @Test
    void shouldReadPlayerTreesAndAiTanks() {
        FileLevelProvider provider = new FileLevelProvider(
                "levels/valid-level.txt"
        );

        LevelData level = provider.createLevel(3, 2);

        assertEquals(
                new GridPoint2(0, 1),
                level.getPlayerPosition()
        );
        assertEquals(
                List.of(new GridPoint2(1, 1)),
                level.getAiTankPositions()
        );
        assertEquals(
                List.of(new GridPoint2(2, 0)),
                level.getTreePositions()
        );
    }

    @Test
    void shouldRejectUnknownSymbols() {
        FileLevelProvider provider = new FileLevelProvider(
                "levels/invalid-level.txt"
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> provider.createLevel(2, 1)
        );
    }

    @Test
    void shouldRejectMissingResource() {
        FileLevelProvider provider = new FileLevelProvider(
                "levels/missing.txt"
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> provider.createLevel(1, 1)
        );
    }
}
