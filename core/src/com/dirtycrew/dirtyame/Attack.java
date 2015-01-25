package com.dirtycrew.dirtyame;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

/**
 * Created by z084254 on 1/24/15.
 */
public class Attack extends Entity {

    private Body body;
    public boolean right;
    public Sprite sprite;
    public static final float SHOT_VELOCITY = 1000;
    private static final long SHOT_DURATION_MILLE = 250;
    public boolean destroy;
    public static final int SHOT_WIDTH = 10;
    public static final int SHOT_HEIGHT = 10;

    private boolean changeMovement = false;

    public Attack(Body body, BetterThanBrandonsTimer timer, boolean right)
    {
        this.body = body;

        timer.startTimer(SHOT_DURATION_MILLE, new BetterThanBrandonsTimer.TimerListener() {
            @Override
            public void onTimerExpired() {
                destroy = true;
            }
        });
        this.right = right;
        destroy = false;
    }

    @Override
    public void update(float delta)
    {
        if (body.getLinearVelocity().x == 0) {
            if (right){
                body.applyForceToCenter(SHOT_VELOCITY, 0, true);
            }else{
                body.applyForceToCenter(-SHOT_VELOCITY, 0, true);
            }
        }
        Vector2 spritePos = Conversions.createSpritePosition(body.getPosition(), new Vector2(sprite.getWidth(), sprite.getHeight()));
        sprite.setPosition(spritePos.x, spritePos.y);

    }

    public Body getBody() {
        return body;
    }

    public void setBody(Body body) {
        this.body = body;
    }
}
