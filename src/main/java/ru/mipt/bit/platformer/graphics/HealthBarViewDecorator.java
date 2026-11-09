package ru.mipt.bit.platformer.graphics;

import com.badlogic.gdx.graphics.g2d.Batch;
import ru.mipt.bit.platformer.entities.Tank;

import java.util.Objects;

/** Adds an optional health bar without changing the tank sprite view. */
public class HealthBarViewDecorator implements EntityView {

    private final EntityView delegate;
    private final Tank tank;
    private final HealthBarVisibility visibility;
    private final HealthBarRenderer healthBarRenderer;

    public HealthBarViewDecorator(
            EntityView delegate,
            Tank tank,
            HealthBarVisibility visibility,
            HealthBarRenderer healthBarRenderer
    ) {
        this.delegate = Objects.requireNonNull(delegate, "delegate");
        this.tank = Objects.requireNonNull(tank, "tank");
        this.visibility = Objects.requireNonNull(visibility, "visibility");
        this.healthBarRenderer = Objects.requireNonNull(
                healthBarRenderer,
                "healthBarRenderer"
        );
    }

    @Override
    public void render(Batch batch) {
        delegate.render(batch);
        if (visibility.isVisible()) {
            healthBarRenderer.render(batch, tank);
        }
    }

    @Override
    public void dispose() {
        delegate.dispose();
    }
}
