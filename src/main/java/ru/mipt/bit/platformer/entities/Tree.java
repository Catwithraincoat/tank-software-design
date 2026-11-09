package ru.mipt.bit.platformer.entities;

import com.badlogic.gdx.math.GridPoint2;

public class Tree implements GameEntity {
    private final GridPoint2 position;

    public Tree(GridPoint2 position) {
        this.position = new GridPoint2(position);
    }

    @Override
    public GridPoint2 getPosition() { return new GridPoint2(position); }

    @Override
    public void live(float tick) {
        // Trees are static and indestructible.
    }
}
