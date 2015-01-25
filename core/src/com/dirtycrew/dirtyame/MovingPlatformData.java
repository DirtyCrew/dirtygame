package com.dirtycrew.dirtyame;

import com.badlogic.gdx.math.Vector2;

/**
 * Created by z084254 on 1/25/15.
 */
public class MovingPlatformData {
    Vector2 position;
    long movementDuration;
    boolean horizontal;
    boolean oppositeStart;

    public MovingPlatformData(Vector2 position, long movementDuration, boolean horizontal, boolean oppositeStart){
        this.position = position;
        this.movementDuration = movementDuration;
        this.horizontal = horizontal;
        this.oppositeStart = oppositeStart;
    }
}
