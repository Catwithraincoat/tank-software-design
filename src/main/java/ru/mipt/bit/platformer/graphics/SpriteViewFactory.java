package ru.mipt.bit.platformer.graphics;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import ru.mipt.bit.platformer.entities.*;

import static ru.mipt.bit.platformer.util.GdxGameUtils.*;

/** Owns shared textures; individual views only reference them. */
public class SpriteViewFactory implements EntityViewFactory {
    private final GameField field;
    private final Texture tankTexture = new Texture("images/tank_blue.png");
    private final Texture treeTexture = new Texture("images/greenTree.png");
    private final Texture bulletTexture;

    public SpriteViewFactory(GameField field) {
        this.field = field;
        Pixmap pixmap = new Pixmap(16, 16, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.YELLOW);
        pixmap.fillCircle(8, 8, 6);
        bulletTexture = new Texture(pixmap);
        pixmap.dispose();
    }

    @Override
    public EntityView create(GameEntity entity) {
        Texture texture;
        if (entity instanceof Tank) {
            texture = tankTexture;
        } else if (entity instanceof Tree) {
            texture = treeTexture;
        } else if (entity instanceof Bullet) {
            texture = bulletTexture;
        } else {
            throw new IllegalArgumentException("Unsupported entity: " + entity);
        }
        TextureRegion region = new TextureRegion(texture);
        Rectangle rectangle = createBoundingRectangle(region);
        return new EntityView() {
            @Override
            public void render(Batch batch) {
                float rotation = 0f;
                if (entity instanceof Tank tank) {
                    field.moveRectangleBetweenTileCenters(rectangle, tank.getPosition(),
                            tank.getDestination(), tank.getMovementProgress());
                    rotation = tank.getOrientation().getRotation();
                } else if (entity instanceof Bullet bullet) {
                    field.moveRectangleBetweenTileCenters(rectangle, bullet.getPosition(),
                            bullet.getNextPosition(), bullet.getProgress());
                    rotation = bullet.getOrientation().getRotation();
                } else {
                    field.moveRectangleToTileCenter(rectangle, entity.getPosition());
                }
                drawTextureRegionUnscaled(batch, region, rectangle, rotation);
            }

            @Override
            public void dispose() {
                // Textures are shared and disposed by the factory.
            }
        };
    }

    @Override
    public void dispose() {
        tankTexture.dispose();
        treeTexture.dispose();
        bulletTexture.dispose();
    }
}
