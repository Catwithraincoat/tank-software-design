package ru.mipt.bit.platformer.tests;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.mipt.bit.platformer.entities.Field;
import ru.mipt.bit.platformer.entities.Orientation;
import ru.mipt.bit.platformer.entities.Tank;
import ru.mipt.bit.platformer.util.TileMovement;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TankTest {

    @Mock
    private Field field;

    @Mock
    private TileMovement tileMovement;

    private AutoCloseable mocks;

    @BeforeEach
    void setUp() {
        mocks = MockitoAnnotations.openMocks(this);

        when(field.getTileMovement()).thenReturn(tileMovement);
    }

    @AfterEach
    void tearDown() throws Exception {
        mocks.close();
    }

    @Test
    void shouldHaveInitialPosition() {
        GridPoint2 initialPosition = new GridPoint2(3, 4);

        Tank tank = new Tank(field, initialPosition);

        assertEquals(initialPosition, tank.getPosition());
    }

    @Test
    void shouldHaveRightOrientationInitially() {
        Tank tank = new Tank(field, new GridPoint2(3, 4));

        assertEquals(Orientation.RIGHT, tank.getOrientation());
    }

    @Test
    void getPositionShouldReturnCopy() {
        Tank tank = new Tank(field, new GridPoint2(3, 4));

        GridPoint2 position = tank.getPosition();
        position.set(100, 100);

        assertEquals(new GridPoint2(3, 4), tank.getPosition());
    }

    @Test
    void shouldNotMoveWhenNoKeyIsPressed() {
        Tank tank = new Tank(field, new GridPoint2(3, 4));

        tank.update(1f);

        assertEquals(new GridPoint2(3, 4), tank.getPosition());
        assertEquals(Orientation.RIGHT, tank.getOrientation());
    }

    @Test
    void shouldCheckTileMovementDuringUpdate() {
        Tank tank = new Tank(field, new GridPoint2(3, 4));

        tank.update(1f);

        verify(tileMovement).moveRectangleBetweenTileCenters(
                any(),
                any(),
                any(),
                anyFloat()
        );
    }
}