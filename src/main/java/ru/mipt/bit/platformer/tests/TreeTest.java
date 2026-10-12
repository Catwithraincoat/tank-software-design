package ru.mipt.bit.platformer.tests;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.mipt.bit.platformer.entities.GameField;
import ru.mipt.bit.platformer.entities.Tree;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

class TreeTest {

    @Mock
    private GameField field;

    private AutoCloseable mocks;

    @BeforeEach
    void setUp() {
        mocks = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() throws Exception {
        mocks.close();
    }

    @Test
    void shouldHaveInitialPosition() {
        GridPoint2 position =
                new GridPoint2(5, 7);

        Tree tree = new Tree(
                field,
                position
        );

        assertEquals(
                position,
                tree.getPosition()
        );
    }

    @Test
    void getPositionShouldReturnCopy() {
        Tree tree = new Tree(
                field,
                new GridPoint2(5, 7)
        );

        GridPoint2 position =
                tree.getPosition();

        position.set(100, 100);

        assertEquals(
                new GridPoint2(5, 7),
                tree.getPosition()
        );
    }

    @Test
    void shouldAddObstacleWhenCreated() {
        GridPoint2 position =
                new GridPoint2(5, 7);

        new Tree(
                field,
                position
        );

        verify(field).addObstacle(position);
    }
}