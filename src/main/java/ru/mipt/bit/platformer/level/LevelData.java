
package ru.mipt.bit.platformer.level;

import com.badlogic.gdx.math.GridPoint2;

import java.util.ArrayList;
import java.util.List;

public class LevelData {

    private final GridPoint2 playerPosition;
    private final List<GridPoint2> treePositions;
    private final List<GridPoint2> aiTankPositions;

    public LevelData(
            GridPoint2 playerPosition,
            List<GridPoint2> treePositions
    ) {
        this(playerPosition, treePositions, List.of());
    }

    public LevelData(
            GridPoint2 playerPosition,
            List<GridPoint2> treePositions,
            List<GridPoint2> aiTankPositions
    ) {
        this.playerPosition = new GridPoint2(playerPosition);
        this.treePositions = copyPositions(treePositions);
        this.aiTankPositions = copyPositions(aiTankPositions);
    }

    public GridPoint2 getPlayerPosition() {
        return new GridPoint2(playerPosition);
    }

    public List<GridPoint2> getTreePositions() {
        return copyPositions(treePositions);
    }

    public List<GridPoint2> getAiTankPositions() {
        return copyPositions(aiTankPositions);
    }

    private static List<GridPoint2> copyPositions(
            List<GridPoint2> positions
    ) {
        List<GridPoint2> result = new ArrayList<>();

        for (GridPoint2 position : positions) {
            result.add(new GridPoint2(position));
        }

        return result;
    }
}
