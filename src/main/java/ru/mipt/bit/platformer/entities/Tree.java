package ru.mipt.bit.platformer.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Rectangle;

import static ru.mipt.bit.platformer.util.GdxGameUtils.createBoundingRectangle;
import static ru.mipt.bit.platformer.util.GdxGameUtils.drawTextureRegionUnscaled;

public class Tree implements GameEntity {

    private final Texture texture;
    private final TextureRegion graphics;
    private final Rectangle rectangle;

    private final GridPoint2 position;

    public Tree(GameField field, GridPoint2 position) {
        this.position = new GridPoint2(position);

        texture = new Texture("images/greenTree.png");
        graphics = new TextureRegion(texture);
        rectangle = createBoundingRectangle(graphics);

        field.moveRectangleToTileCenter(
                rectangle,
                this.position
        );

        field.addObstacle(this.position);
    }

    @Override
    public void update(float deltaTime) {
        // Tree is static.
    }

    @Override
    public void render(Batch batch) {
        drawTextureRegionUnscaled(
                batch,
                graphics,
                rectangle,
                0f
        );
    }

    public GridPoint2 getPosition() {
        return new GridPoint2(position);
    }

    @Override
    public void dispose() {
        texture.dispose();
    }
}