package ru.mipt.bit.platformer.level;

public interface LevelProvider {

    LevelData createLevel(int width, int height);
}
