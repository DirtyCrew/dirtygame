package com.dirtycrew.dirtyame;

import com.badlogic.gdx.math.Vector2;

/**
 * Created by sturm on 1/24/15.
 */
public class Constants {
    public final static int SCREEN_WIDTH = 1024;
    public final static int SCREEN_HEIGHT = 768;
    public final static float ASPECT_RATIO = (float)SCREEN_HEIGHT / (float)SCREEN_WIDTH;

    /**
     * 1 sprite is 32 pixels
     * 1 meter is equal to 32 pixels
     * Screen is 16 meters width
     * So, screen is 16 sprites wide
     */
    public final static float FROM_METERS_TO_PIXEL = 32.f;
    public final static float FROM_PIXELS_TO_METER = 1/32.f;
    public final static int VIEWPORT_WIDTH = 128;
    public final static int VIEWPORT_HEIGHT = (int)(128.f * ASPECT_RATIO);

    
    public final static Vector2 GRAVITY = new Vector2(0, -30);
}
