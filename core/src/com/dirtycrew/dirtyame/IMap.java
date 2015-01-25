package com.dirtycrew.dirtyame;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;

import java.util.List;

/**
 * Created by Jared on 1/25/15.
 */
interface IMap {
    void drawMap(OrthographicCamera camera);
    List<Vector2> getMonsterSpawnLocations();
    List<Vector2> getBeeSpawnLocations();
    Vector2 getPlayerSpawnLocation();
    float getWidth();
    float getHeight();
}
