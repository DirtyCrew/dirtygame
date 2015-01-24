package com.dirtycrew.dirtyame;

import com.badlogic.gdx.math.Vector2;

import java.util.Vector;

/**
 * Created by Kern on 1/24/2015.
 */
public class Conversions {

    public static float convertToPixels(float meters) {
        return meters * Constants.PIXELS_PER_METER;
    }

    public static float convertToMeters(float pixels) {
        return pixels * Constants.METERS_PER_PIXEL;
    }

    public static Vector2 vctSpriteToPhysics(Vector2 sprite)
    {
        Vector2 tmpVCT = new Vector2((sprite.x + 25),(sprite.y + 25));
        return tmpVCT;
    }

    public static Vector2 vctPhysicsToSprite(Vector2 physics)
    {
        Vector2 tmpVCT = new Vector2((physics.x - 25),(physics.y - 25));
        return tmpVCT;
    }

    public static float fltSpriteToPhysics(float sprite)
    {
        float tmpFLT = sprite + 25;
        return tmpFLT;
    }

    public static float fltPhysicsToSprite(float physics)
    {
        float tmpFLT = physics - 25;
        return tmpFLT;
    }
}
