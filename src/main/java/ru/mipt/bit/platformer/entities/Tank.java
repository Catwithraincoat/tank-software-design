package ru.mipt.bit.platformer.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Rectangle;
import ru.mipt.bit.platformer.util.TileMovement;

import static com.badlogic.gdx.math.MathUtils.isEqual;
import static ru.mipt.bit.platformer.util.GdxGameUtils.continueProgress;
import static ru.mipt.bit.platformer.util.GdxGameUtils.createBoundingRectangle;
import static ru.mipt.bit.platformer.util.GdxGameUtils.drawTextureRegionUnscaled;

public class Tank {

    private static final float MOVEMENT_SPEED = 0.4f;

    private final Texture texture;
    private final TextureRegion graphics;
    private final Rectangle rectangle;

    private final Field field;
    private final TileMovement tileMovement;

    private final GridPoint2 position;
    private final GridPoint2 destination;

    private Orientation orientation;

    private float movementProgress = 1f;

    public Tank(Field field, GridPoint2 initialPosition) {
        this.field = field;
        this.tileMovement = field.getTileMovement();

        this.position = new GridPoint2(initialPosition);
        this.destination = new GridPoint2(initialPosition);

        this.orientation = Orientation.RIGHT;

        texture = new Texture("images/tank_blue.png");
        graphics = new TextureRegion(texture);
        rectangle = createBoundingRectangle(graphics);

        field.moveRectangleToTileCenter(
                rectangle,
                position
        );
    }

    public void update(float deltaTime) {
        handleInput();

        tileMovement.moveRectangleBetweenTileCenters(
                rectangle,
                position,
                destination,
                movementProgress
        );

        movementProgress = continueProgress(
                movementProgress,
                deltaTime,
                MOVEMENT_SPEED
        );

        if (isEqual(movementProgress, 1f)) {
            position.set(destination);
        }
    }

    private void handleInput() {
        if (!isEqual(movementProgress, 1f)) {
            return;
        }

        for (Orientation orientation : Orientation.values()) {
            if (orientation.isPressed()) {
                move(orientation);
                return;
            }
        }
    }

    private void move(Orientation orientation) {
        GridPoint2 newDestination = new GridPoint2(position)
                .add(
                        orientation.getDx(),
                        orientation.getDy()
                );

        if (field.isBlocked(newDestination)) {
            return;
        }

        this.orientation = orientation;
        this.destination.set(newDestination);
        this.movementProgress = 0f;
    }

    public void render(Batch batch) {
        drawTextureRegionUnscaled(
                batch,
                graphics,
                rectangle,
                orientation.getRotation()
        );
    }

    public GridPoint2 getPosition() {
        return new GridPoint2(position);
    }

    public Orientation getOrientation() {
        return orientation;
    }

    public void dispose() {
        texture.dispose();
    }
}