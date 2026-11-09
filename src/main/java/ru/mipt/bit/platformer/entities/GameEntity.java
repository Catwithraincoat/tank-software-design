package ru.mipt.bit.platformer.entities;

import com.badlogic.gdx.math.GridPoint2;

/** An object of the logical level. Has no dependency on graphics. */
public interface GameEntity {
    GridPoint2 getPosition();
    void live(float tick);
}
