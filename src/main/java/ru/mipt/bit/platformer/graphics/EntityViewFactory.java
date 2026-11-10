package ru.mipt.bit.platformer.graphics;

import ru.mipt.bit.platformer.entities.GameEntity;

public interface EntityViewFactory {
    EntityView create(GameEntity entity);
    default void dispose() { }
}
