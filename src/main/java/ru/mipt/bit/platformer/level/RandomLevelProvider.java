
package ru.mipt.bit.platformer.level;

import com.badlogic.gdx.math.GridPoint2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class RandomLevelProvider implements LevelProvider {

    private static final int TREE_COUNT = 12;
    private static final int AI_TANK_COUNT = 4;

    private final Random random;
    private final int requestedTreeCount;
    private final int requestedAiTankCount;

    public RandomLevelProvider() {
        this(new Random(), TREE_COUNT, AI_TANK_COUNT);
    }

    public RandomLevelProvider(
            Random random,
            int treeCount,
            int aiTankCount
    ) {
        if (treeCount < 0 || aiTankCount < 0) {
            throw new IllegalArgumentException(
                    "Entity counts cannot be negative"
            );
        }

        this.random = Objects.requireNonNull(random, "random");
        this.requestedTreeCount = treeCount;
        this.requestedAiTankCount = aiTankCount;
    }

    @Override
    public LevelData createLevel(int width, int height) {
        if (width <= 0 || height <= 0) {
            throw new IllegalArgumentException(
                    "Размеры поля должны быть положительными"
            );
        }

        long totalCellsLong = (long) width * height;
        if (totalCellsLong > Integer.MAX_VALUE) {
            throw new IllegalArgumentException(
                    "Field is too large"
            );
        }

        List<GridPoint2> positions = new ArrayList<>();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                positions.add(new GridPoint2(x, y));
            }
        }

        Collections.shuffle(positions, random);

        int availableAfterPlayer = positions.size() - 1;
        int aiTankCount = Math.min(
                requestedAiTankCount,
                availableAfterPlayer
        );
        int treeCount = Math.min(
                requestedTreeCount,
                availableAfterPlayer - aiTankCount
        );

        GridPoint2 playerPosition = positions.get(0);
        List<GridPoint2> aiTanks = new ArrayList<>(
                positions.subList(1, 1 + aiTankCount)
        );
        List<GridPoint2> trees = new ArrayList<>(
                positions.subList(
                        1 + aiTankCount,
                        1 + aiTankCount + treeCount
                )
        );

        return new LevelData(
                playerPosition,
                trees,
                aiTanks
        );
    }
}
