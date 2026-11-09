package ru.mipt.bit.platformer.level;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RandomLevelProviderTest {

    @Test
    void shouldGenerateUniquePlayerTanksAndTreesInsideField() {
        RandomLevelProvider provider = new RandomLevelProvider(
                new Random(1234L),
                12,
                4
        );

        LevelData level = provider.createLevel(10, 8);

        assertEquals(12, level.getTreePositions().size());
        assertEquals(4, level.getAiTankPositions().size());

        Set<GridPoint2> allPositions = new HashSet<>();
        assertTrue(allPositions.add(level.getPlayerPosition()));
        addUnique(allPositions, level.getAiTankPositions());
        addUnique(allPositions, level.getTreePositions());
        assertEquals(17, allPositions.size());

        for (GridPoint2 position : allPositions) {
            assertTrue(position.x >= 0 && position.x < 10);
            assertTrue(position.y >= 0 && position.y < 8);
        }
    }

    @Test
    void shouldPreferGeneratingAiTankOnSmallField() {
        RandomLevelProvider provider = new RandomLevelProvider(
                new Random(1L),
                12,
                4
        );

        LevelData level = provider.createLevel(2, 1);

        assertEquals(1, level.getAiTankPositions().size());
        assertTrue(level.getTreePositions().isEmpty());
    }

    @Test
    void shouldRejectInvalidDimensions() {
        RandomLevelProvider provider = new RandomLevelProvider();

        assertThrows(
                IllegalArgumentException.class,
                () -> provider.createLevel(0, 1)
        );
        assertThrows(
                IllegalArgumentException.class,
                () -> provider.createLevel(1, -1)
        );
    }

    private static void addUnique(
            Set<GridPoint2> result,
            List<GridPoint2> positions
    ) {
        for (GridPoint2 position : positions) {
            assertTrue(
                    result.add(position),
                    "Duplicate position: " + position
            );
        }
    }
}
