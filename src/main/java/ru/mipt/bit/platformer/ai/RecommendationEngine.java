package ru.mipt.bit.platformer.ai;

/** A recommendation API independent of game entities and command classes. */
public interface RecommendationEngine {
    Recommendation recommend(boolean targetAhead);
}
