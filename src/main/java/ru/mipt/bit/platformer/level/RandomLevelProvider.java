
package ru.mipt.bit.platformer.level;

import com.badlogic.gdx.math.GridPoint2;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class RandomLevelProvider implements LevelProvider {

    private static final int TREE_COUNT = 12;

    private final Random random = new Random();

    @Override
    public LevelData createLevel(int width, int height) {
        if (width <= 0 || height <= 0) {
            throw new IllegalArgumentException(
                    "Размеры поля должны быть положительными"
            );
        }

        int totalCells = width * height;
        int treeCount = Math.min(TREE_COUNT, totalCells - 1);

        GridPoint2 playerPosition = new GridPoint2(
                random.nextInt(width),
                random.nextInt(height)
        );

        Set<GridPoint2> occupied = new HashSet<>();
        occupied.add(playerPosition);

        List<GridPoint2> trees = new ArrayList<>();

        while (trees.size() < treeCount) {
            GridPoint2 position = new GridPoint2(
                    random.nextInt(width),
                    random.nextInt(height)
            );

            if (occupied.add(position)) {
                trees.add(position);
            }
        }

        return new LevelData(playerPosition, trees);
    }
}
