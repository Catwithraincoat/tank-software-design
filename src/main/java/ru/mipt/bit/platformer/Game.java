package ru.mipt.bit.platformer;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.GridPoint2;
import ru.mipt.bit.platformer.entities.Field;
import ru.mipt.bit.platformer.entities.GameEntity;
import ru.mipt.bit.platformer.entities.GameField;
import ru.mipt.bit.platformer.entities.Tank;
import ru.mipt.bit.platformer.entities.Tree;
import ru.mipt.bit.platformer.input.InputController;
import ru.mipt.bit.platformer.input.KeyboardInputController;

import java.util.ArrayList;
import java.util.List;
import ru.mipt.bit.platformer.level.FileLevelProvider;
import ru.mipt.bit.platformer.level.LevelData;
import ru.mipt.bit.platformer.level.LevelProvider;
import ru.mipt.bit.platformer.level.RandomLevelProvider;

import static com.badlogic.gdx.graphics.GL20.GL_COLOR_BUFFER_BIT;

public class Game implements ApplicationListener {

    private Batch batch;

    private GameField field;
    private List<GameEntity> entities;


    @Override
    public void create() {
        batch = new SpriteBatch();

        field = new Field(batch);
        entities = new ArrayList<>();

        InputController inputController =
                new KeyboardInputController();

        // Выбери один из двух способов создания уровня.
        LevelProvider levelProvider =
                new FileLevelProvider("level.txt");

        // Для случайной генерации используй вместо
        // предыдущей строки:
        // LevelProvider levelProvider =
        //         new RandomLevelProvider();

        LevelData level = levelProvider.createLevel(
                field.getWidth(),
                field.getHeight()
        );

        Tank tank = new Tank(
                field,
                level.getPlayerPosition(),
                inputController
        );

        entities.add(tank);

        for (GridPoint2 position : level.getTreePositions()) {
            entities.add(new Tree(field, position));
        }
    }


    @Override
    public void render() {
        clearScreen();

        float deltaTime = Gdx.graphics.getDeltaTime();

        for (GameEntity entity : entities) {
            entity.update(deltaTime);
        }

        field.render();

        batch.begin();

        for (GameEntity entity : entities) {
            entity.render(batch);
        }

        batch.end();
    }

    private void clearScreen() {
        Gdx.gl.glClearColor(
                0f,
                0f,
                0.2f,
                1f
        );

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
        for (GameEntity entity : entities) {
            entity.dispose();
        }

        field.dispose();
        batch.dispose();
    }
}