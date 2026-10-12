package ru.mipt.bit.platformer.entities;

import com.badlogic.gdx.graphics.g2d.Batch;

public interface GameEntity {

    void update(float deltaTime);

    void render(Batch batch);

    void dispose();
}
