package ru.mipt.bit.platformer.level;

import ru.mipt.bit.platformer.entities.GameEntity;

public interface LevelListener {
    void entityAdded(GameEntity entity);
    void entityRemoved(GameEntity entity);
}
