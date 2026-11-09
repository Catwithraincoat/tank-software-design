package ru.mipt.bit.platformer;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import ru.mipt.bit.platformer.ai.SimpleRecommendationEngine;
import ru.mipt.bit.platformer.command.TankCommand;
import ru.mipt.bit.platformer.entities.Field;
import ru.mipt.bit.platformer.entities.GameField;
import ru.mipt.bit.platformer.graphics.GraphicalLevel;
import ru.mipt.bit.platformer.graphics.SpriteViewFactory;
import ru.mipt.bit.platformer.input.KeyboardInputController;
import ru.mipt.bit.platformer.level.LogicalLevel;
import ru.mipt.bit.platformer.level.RandomLevelProvider;
import ru.mipt.bit.platformer.strategy.KeyboardTankControlStrategy;
import ru.mipt.bit.platformer.strategy.RecommendationTankControlStrategy;

import java.util.List;
import java.util.Random;

import static com.badlogic.gdx.graphics.GL20.GL_COLOR_BUFFER_BIT;

public class Game implements ApplicationListener {
    private Batch batch;
    private GameField field;
    private LogicalLevel level;
    private GraphicalLevel graphics;

    @Override
    public void create() {
        batch = new SpriteBatch();
        field = new Field(batch);
        level = new LogicalLevel(field.getWidth(), field.getHeight());
        // Subscribe before loading: initial objects use the same notifications
        // as bullets and destroyed tanks created/removed during the game.
        graphics = new GraphicalLevel(level, new SpriteViewFactory(field));
        level.initialize(
                new RandomLevelProvider().createLevel(field.getWidth(), field.getHeight()),
                new KeyboardTankControlStrategy(new KeyboardInputController()),
                () -> new RecommendationTankControlStrategy(
                        new SimpleRecommendationEngine(new Random()), level));
    }

    @Override
    public void render() {
        // 1. Create commands from keyboard / AI.
        List<TankCommand> commands = level.createCommands();
        // 2. Execute the collected commands.
        level.executeCommands(commands);
        // 3. Advance every object's logical state.
        level.live(Gdx.graphics.getDeltaTime());
        // 4. Draw the resulting state.
        Gdx.gl.glClearColor(0f, 0f, 0.2f, 1f);
        Gdx.gl.glClear(GL_COLOR_BUFFER_BIT);
        field.render();
        batch.begin();
        graphics.render(batch);
        batch.end();
    }

    @Override
    public void resize(int width, int height) { }

    @Override
    public void pause() { }

    @Override
    public void resume() { }

    @Override
    public void dispose() {
        level.clear();
        graphics.dispose();
        field.dispose();
        batch.dispose();
    }
}
