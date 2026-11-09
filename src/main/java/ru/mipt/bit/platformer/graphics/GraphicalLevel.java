package ru.mipt.bit.platformer.graphics;

import com.badlogic.gdx.graphics.g2d.Batch;
import ru.mipt.bit.platformer.entities.GameEntity;
import ru.mipt.bit.platformer.level.LevelListener;
import ru.mipt.bit.platformer.level.LogicalLevel;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/** Observer of the logical level; never creates or removes logical objects. */
public class GraphicalLevel implements LevelListener {
    private final LogicalLevel level;
    private final EntityViewFactory factory;
    private final Map<GameEntity, EntityView> views = new LinkedHashMap<>();
    private boolean disposed;

    public GraphicalLevel(LogicalLevel level, EntityViewFactory factory) {
        this.level = Objects.requireNonNull(level);
        this.factory = Objects.requireNonNull(factory);
        level.addListener(this);
    }

    @Override
    public void entityAdded(GameEntity entity) {
        views.computeIfAbsent(entity, factory::create);
    }

    @Override
    public void entityRemoved(GameEntity entity) {
        EntityView view = views.remove(entity);
        if (view != null) {
            view.dispose();
        }
    }

    public void render(Batch batch) {
        views.values().forEach(view -> view.render(batch));
    }

    public void dispose() {
        if (disposed) {
            return;
        }
        disposed = true;
        level.removeListener(this);
        views.values().forEach(EntityView::dispose);
        views.clear();
        factory.dispose();
    }
}
