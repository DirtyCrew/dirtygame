package com.dirtycrew.dirtyame;

import com.badlogic.gdx.math.Vector2;

/**
 * Created by sturm on 1/24/15.
 */
public class Constants {
    public final static float METERS_PER_PIXEL = 1.f/10.f;
    public final static float PIXELS_PER_METER = 10.f;
    public final static int SCREEN_WIDTH = 800;
    public final static int SCREEN_HEIGHT = 600;
    public final static int VIEWPORT_WIDTH = (int)(SCREEN_WIDTH * METERS_PER_PIXEL);
    public final static int VIEWPORT_HEIGHT = (int)(SCREEN_HEIGHT *  METERS_PER_PIXEL);

    public final static Vector2 GRAVITY = new Vector2(0, -18);
}
