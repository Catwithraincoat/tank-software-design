package ru.mipt.bit.platformer.ai;

import java.util.Objects;
import java.util.Random;

public class SimpleRecommendationEngine implements RecommendationEngine {
    private final Random random;

    public SimpleRecommendationEngine(Random random) {
        this.random = Objects.requireNonNull(random);
    }

    @Override
    public Recommendation recommend(boolean targetAhead) {
        if (targetAhead) {
            return Recommendation.FIRE;
        }
        // Four directions plus a random shot, just like the random strategy.
        return Recommendation.values()[random.nextInt(5)];
    }
}
