package ru.mipt.bit.platformer.graphics;

import com.badlogic.gdx.graphics.g2d.Batch;
import ru.mipt.bit.platformer.entities.Tank;

@FunctionalInterface
public interface HealthBarRenderer {

    void render(Batch batch, Tank tank);
}
