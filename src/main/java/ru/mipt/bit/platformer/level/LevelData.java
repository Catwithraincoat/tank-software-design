
package ru.mipt.bit.platformer.level;

import com.badlogic.gdx.math.GridPoint2;

import java.util.ArrayList;
import java.util.List;

public class LevelData {

    private final GridPoint2 playerPosition;
    private final List<GridPoint2> treePositions;

    public LevelData(
            GridPoint2 playerPosition,
            List<GridPoint2> treePositions
    ) {
        this.playerPosition = new GridPoint2(playerPosition);
        this.treePositions = new ArrayList<>();

        for (GridPoint2 position : treePositions) {
            this.treePositions.add(new GridPoint2(position));
        }
    }

    public GridPoint2 getPlayerPosition() {
        return new GridPoint2(playerPosition);
    }

    public List<GridPoint2> getTreePositions() {
        List<GridPoint2> result = new ArrayList<>();

        for (GridPoint2 position : treePositions) {
            result.add(new GridPoint2(position));
        }

        return result;
    }
}
