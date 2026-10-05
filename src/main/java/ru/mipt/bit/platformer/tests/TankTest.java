package ru.mipt.bit.platformer.tests;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.mipt.bit.platformer.entities.GameField;
import ru.mipt.bit.platformer.entities.Orientation;
import ru.mipt.bit.platformer.entities.Tank;
import ru.mipt.bit.platformer.input.InputController;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class TankTest {

    @Mock
    private GameField field;

    @Mock
    private InputController inputController;

    private AutoCloseable mocks;

    @BeforeEach
    void setUp() {
        mocks = MockitoAnnotations.openMocks(this);

        when(inputController.getPressedOrientation())
                .thenReturn(null);
    }

    @AfterEach
    void tearDown() throws Exception {
        mocks.close();
    }

    @Test
    void shouldHaveInitialPosition() {
        GridPoint2 initialPosition =
                new GridPoint2(3, 4);

        Tank tank = new Tank(
                field,
                initialPosition,
                inputController
        );

        assertEquals(
                initialPosition,
                tank.getPosition()
        );
    }

    @Test
    void shouldHaveRightOrientationInitially() {
        Tank tank = new Tank(
                field,
                new GridPoint2(3, 4),
                inputController
        );

        assertEquals(
                Orientation.RIGHT,
                tank.getOrientation()
        );
    }

    @Test
    void getPositionShouldReturnCopy() {
        Tank tank = new Tank(
                field,
                new GridPoint2(3, 4),
                inputController
        );

        GridPoint2 position = tank.getPosition();

        position.set(100, 100);

        assertEquals(
                new GridPoint2(3, 4),
                tank.getPosition()
        );
    }

    @Test
    void shouldNotMoveWhenNoKeyIsPressed() {
        Tank tank = new Tank(
                field,
                new GridPoint2(3, 4),
                inputController
        );

        tank.update(1f);

        assertEquals(
                new GridPoint2(3, 4),
                tank.getPosition()
        );

        assertEquals(
                Orientation.RIGHT,
                tank.getOrientation()
        );
    }

    @Test
    void shouldCheckTileMovementDuringUpdate() {
        Tank tank = new Tank(
                field,
                new GridPoint2(3, 4),
                inputController
        );

        tank.update(1f);

        verify(field).moveRectangleBetweenTileCenters(
                any(),
                any(),
                any(),
                any(Float.class)
        );
    }
}