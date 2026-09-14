package ru.mipt.bit.platformer;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.GridPoint2;
import ru.mipt.bit.platformer.entities.Field;
import ru.mipt.bit.platformer.entities.Tank;
import ru.mipt.bit.platformer.entities.Tree;

import static com.badlogic.gdx.graphics.GL20.GL_COLOR_BUFFER_BIT;

public class Game implements ApplicationListener {

    private static final int WINDOW_WIDTH = 1280;
    private static final int WINDOW_HEIGHT = 1024;

    private static final GridPoint2 PLAYER_START_POSITION =
            new GridPoint2(1, 1);

    private static final GridPoint2 TREE_POSITION =
            new GridPoint2(1, 3);

    private Batch batch;

    private Field field;
    private Tank tank;
    private Tree tree;

    @Override
    public void create() {
        batch = new SpriteBatch();

        field = new Field(batch);

        tree = new Tree(field, TREE_POSITION);

        tank = new Tank(
                field,
                PLAYER_START_POSITION
        );
    }

    @Override
    public void render() {
        clearScreen();

        float deltaTime = Gdx.graphics.getDeltaTime();

        tank.update(deltaTime);

        field.render();

        batch.begin();

        tank.render(batch);
        tree.render(batch);

        batch.end();
    }

    private void clearScreen() {
        Gdx.gl.glClearColor(0f, 0f, 0.2f, 1f);
        Gdx.gl.glClear(GL_COLOR_BUFFER_BIT);
    }

    @Override
    public void resize(int width, int height) {
        // The game does not react to window resizing.
    }

    @Override
    public void pause() {
        // The game does not support pausing.
    }

    @Override
    public void resume() {
        // The game does not support pausing.
    }

    @Override
    public void dispose() {
        tank.dispose();
        tree.dispose();
        field.dispose();
        batch.dispose();
    }
}