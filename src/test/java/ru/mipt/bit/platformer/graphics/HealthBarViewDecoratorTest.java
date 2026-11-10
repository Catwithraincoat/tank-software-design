package ru.mipt.bit.platformer.graphics;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.Test;
import ru.mipt.bit.platformer.command.NoOpTankCommand;
import ru.mipt.bit.platformer.entities.Tank;
import ru.mipt.bit.platformer.level.LogicalLevel;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HealthBarViewDecoratorTest {

    @Test
    void shouldAlwaysRenderDelegateAndRenderBarOnlyWhenVisible() {
        LogicalLevel level = new LogicalLevel(2, 1);
        Tank tank = level.addTank(
                new GridPoint2(),
                ignored -> NoOpTankCommand.INSTANCE
        );
        AtomicInteger spriteRenders = new AtomicInteger();
        AtomicInteger barRenders = new AtomicInteger();
        AtomicInteger disposals = new AtomicInteger();
        EntityView sprite = new EntityView() {
            @Override
            public void render(Batch batch) {
                spriteRenders.incrementAndGet();
            }

            @Override
            public void dispose() {
                disposals.incrementAndGet();
            }
        };
        HealthBarVisibility visibility = new HealthBarVisibility();
        EntityView decorated = new HealthBarViewDecorator(
                sprite,
                tank,
                visibility,
                (batch, renderedTank) -> {
                    assertEquals(tank, renderedTank);
                    barRenders.incrementAndGet();
                }
        );

        decorated.render(null);
        visibility.toggle();
        decorated.render(null);
        visibility.toggle();
        decorated.render(null);
        decorated.dispose();

        assertEquals(3, spriteRenders.get());
        assertEquals(1, barRenders.get());
        assertEquals(1, disposals.get());
    }
}
