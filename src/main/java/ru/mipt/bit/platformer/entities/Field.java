package ru.mipt.bit.platformer.entities;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.MapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Rectangle;
import ru.mipt.bit.platformer.util.TileMovement;

import java.util.HashSet;
import java.util.Set;

import static ru.mipt.bit.platformer.util.GdxGameUtils.createSingleLayerMapRenderer;
import static ru.mipt.bit.platformer.util.GdxGameUtils.getSingleLayer;
import static ru.mipt.bit.platformer.util.GdxGameUtils.moveRectangleAtTileCenter;

public class Field implements GameField {

    private final TiledMap map;
    private final TiledMapTileLayer tileLayer;
    private final MapRenderer renderer;
    private final TileMovement tileMovement;

    private final Set<GridPoint2> obstacles = new HashSet<>();

    public Field(Batch batch) {
        map = new TmxMapLoader().load("level.tmx");

        tileLayer = getSingleLayer(map);

        renderer = createSingleLayerMapRenderer(
                map,
                batch
        );

        tileMovement = new TileMovement(
                tileLayer,
                Interpolation.smooth
        );
    }

    @Override
    public void render() {
        renderer.render();
    }

    @Override
    public boolean isBlocked(GridPoint2 position) {
        return isOutsideField(position)
                || obstacles.contains(position);
    }

    @Override
    public void addObstacle(GridPoint2 position) {
        obstacles.add(new GridPoint2(position));
    }

    @Override
    public void moveRectangleToTileCenter(
            Rectangle rectangle,
            GridPoint2 position
    ) {
        moveRectangleAtTileCenter(
                tileLayer,
                rectangle,
                position
        );
    }

    @Override
    public void moveRectangleBetweenTileCenters(
            Rectangle rectangle,
            GridPoint2 fromPosition,
            GridPoint2 toPosition,
            float progress
    ) {
        tileMovement.moveRectangleBetweenTileCenters(
                rectangle,
                fromPosition,
                toPosition,
                progress
        );
    }

    private boolean isOutsideField(GridPoint2 position) {
        return position.x < 0
                || position.x >= tileLayer.getWidth()
                || position.y < 0
                || position.y >= tileLayer.getHeight();
    }

    @Override
    public void dispose() {
        map.dispose();
    }

    @Override
    public int getWidth() {
        return tileLayer.getWidth();
    }

    @Override
    public int getHeight() {
        return tileLayer.getHeight();
    }
}