package com.dirtycrew.dirtyame;

import com.badlogic.gdx.math.Vector2;

import java.util.Vector;

/**
 * Created by Kern on 1/24/2015.
 */
public class Conversions {
    /**
     * Sprites are centers on their lower left corner. We want them centered in the middle
     */
    public static Vector2 createSpritePosition(Vector2 pos, Vector2 dims) {
        return new Vector2(pos.x - dims.x / 2.f, pos.y - dims.y / 2.f);
    }

    public static Vector2 createBodyPositionFromSpritePosition(Vector2 spritePos, Vector2 spriteDims) {
        return new Vector2(spritePos.x - spriteDims.x / 2.f, spritePos.y - spriteDims.y / 2.f);
    }




    public static Vector2 convertToBox2DSize(Vector2 dims) {
        return new Vector2(dims.x / 2.f, dims.y / 2.f);
    }

    public static float convertToPixels(float meters) {
        return meters * Constants.FROM_METERS_TO_PIXEL;
    }

    public static float convertToMeters(float pixels) {
        return pixels * Constants.FROM_PIXELS_TO_METER;
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
