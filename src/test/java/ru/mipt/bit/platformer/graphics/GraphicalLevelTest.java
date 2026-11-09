package ru.mipt.bit.platformer.graphics;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.Test;
import ru.mipt.bit.platformer.entities.*;
import ru.mipt.bit.platformer.level.LogicalLevel;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

class GraphicalLevelTest {
    static class FakeView implements EntityView {
        int draws;
        int disposals;
        public void render(Batch batch) { draws++; }
        public void dispose() { disposals++; }
    }

    @Test
    void observerShouldCreateRenderRemoveAndDisposeViewsWithoutGraphicsContext() {
        LogicalLevel level = new LogicalLevel(4, 1);
        Tree initial = level.addTree(new GridPoint2());
        Map<GameEntity, FakeView> created = new LinkedHashMap<>();
        GraphicalLevel graphics = new GraphicalLevel(level, entity -> {
            FakeView view = new FakeView();
            created.put(entity, view);
            return view;
        });
        Tree later = level.addTree(new GridPoint2(1, 0));
        graphics.render(null);
        assertEquals(1, created.get(initial).draws);
        assertEquals(1, created.get(later).draws);
        level.remove(initial);
        graphics.render(null);
        assertEquals(1, created.get(initial).draws);
        assertEquals(1, created.get(initial).disposals);
        assertEquals(2, created.get(later).draws);
        graphics.dispose();
        graphics.dispose();
        assertEquals(1, created.get(later).disposals);
        level.addTree(new GridPoint2(2, 0));
        assertEquals(2, created.size());
    }
}
