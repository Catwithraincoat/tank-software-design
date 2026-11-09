package ru.mipt.bit.platformer.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Rectangle;
import ru.mipt.bit.platformer.input.InputController;
import ru.mipt.bit.platformer.strategy.KeyboardTankControlStrategy;
import ru.mipt.bit.platformer.strategy.TankControlStrategy;

import static ru.mipt.bit.platformer.util.GdxGameUtils.createBoundingRectangle;
import static ru.mipt.bit.platformer.util.GdxGameUtils.drawTextureRegionUnscaled;

public class Tank implements GameEntity {

    private final Texture texture;
    private final TextureRegion graphics;
    private final Rectangle rectangle;

    private final GameField field;
    private final TankMovement movement;

    public Tank(
            GameField field,
            GridPoint2 initialPosition,
            InputController inputController
    ) {
        this(
                field,
                initialPosition,
                new KeyboardTankControlStrategy(inputController)
        );
    }

    public Tank(
            GameField field,
            GridPoint2 initialPosition,
            TankControlStrategy controlStrategy
    ) {
        this.field = field;
        this.movement = new TankMovement(
                field,
                initialPosition,
                controlStrategy
        );

        texture = new Texture("images/tank_blue.png");
        graphics = new TextureRegion(texture);
        rectangle = createBoundingRectangle(graphics);

        field.moveRectangleToTileCenter(
                rectangle,
                movement.getPosition()
        );
    }

    @Override
    public void update(float deltaTime) {
        movement.update(deltaTime);

        field.moveRectangleBetweenTileCenters(
                rectangle,
                movement.getPosition(),
                movement.getDestination(),
                movement.getMovementProgress()
        );
    }

    @Override
    public void render(Batch batch) {
        drawTextureRegionUnscaled(
                batch,
                graphics,
                rectangle,
                movement.getOrientation().getRotation()
        );
    }

    public GridPoint2 getPosition() {
        return movement.getPosition();
    }

    public Orientation getOrientation() {
        return movement.getOrientation();
    }

    @Override
    public void dispose() {
        movement.dispose();
        texture.dispose();
    }
}
