package ru.mipt.bit.platformer.level;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LevelDataTest {

    @Test
    void shouldDefensivelyCopyEveryPosition() {
        GridPoint2 player = new GridPoint2(0, 0);
        GridPoint2 tree = new GridPoint2(1, 0);
        GridPoint2 aiTank = new GridPoint2(2, 0);
        LevelData level = new LevelData(
                player,
                List.of(tree),
                List.of(aiTank)
        );

        player.set(9, 9);
        tree.set(9, 9);
        aiTank.set(9, 9);
        level.getPlayerPosition().set(8, 8);
        level.getTreePositions().get(0).set(8, 8);
        level.getAiTankPositions().get(0).set(8, 8);

        assertEquals(
                new GridPoint2(0, 0),
                level.getPlayerPosition()
        );
        assertEquals(
                List.of(new GridPoint2(1, 0)),
                level.getTreePositions()
        );
        assertEquals(
                List.of(new GridPoint2(2, 0)),
                level.getAiTankPositions()
        );
    }
}
