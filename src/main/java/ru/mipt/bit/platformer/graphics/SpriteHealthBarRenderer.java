package ru.mipt.bit.platformer.graphics;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import ru.mipt.bit.platformer.entities.GameField;
import ru.mipt.bit.platformer.entities.Tank;

import java.util.Objects;

public class SpriteHealthBarRenderer implements HealthBarRenderer {

    private static final float BAR_WIDTH = 80f;
    private static final float BAR_HEIGHT = 10f;
    private static final float BAR_BORDER = 2f;
    private static final float BAR_Y_OFFSET = 48f;

    private final GameField field;
    private final Texture backgroundTexture;
    private final Texture healthTexture;
    private final Rectangle bar = new Rectangle(
            0f,
            0f,
            BAR_WIDTH,
            BAR_HEIGHT
    );

    public SpriteHealthBarRenderer(
            GameField field,
            Texture backgroundTexture,
            Texture healthTexture
    ) {
        this.field = Objects.requireNonNull(field, "field");
        this.backgroundTexture = Objects.requireNonNull(
                backgroundTexture,
                "backgroundTexture"
        );
        this.healthTexture = Objects.requireNonNull(
                healthTexture,
                "healthTexture"
        );
    }

    @Override
    public void render(Batch batch, Tank tank) {
        field.moveRectangleBetweenTileCenters(
                bar,
                tank.getPosition(),
                tank.getDestination(),
                tank.getMovementProgress()
        );
        float x = bar.x;
        float y = bar.y + BAR_Y_OFFSET;
        batch.draw(backgroundTexture, x, y, BAR_WIDTH, BAR_HEIGHT);

        float healthRatio = (float) tank.getHealth() / tank.getMaxHealth();
        float availableWidth = BAR_WIDTH - 2f * BAR_BORDER;
        batch.draw(
                healthTexture,
                x + BAR_BORDER,
                y + BAR_BORDER,
                availableWidth * healthRatio,
                BAR_HEIGHT - 2f * BAR_BORDER
        );
    }
}
