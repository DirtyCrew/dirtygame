package com.dirtycrew.dirtyame;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

import java.util.Random;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

import java.util.Random;

/**
 * Created by z084254 on 1/24/15.
 */
public class MovingPlatform extends Entity{
    public Body body;
    public Sprite sprite;
    private static final float MAX_VELOCITY = 3;
    private static final float FORCE = 1;
    private boolean rightOrUp = true;
    private boolean horizontal; //else vertical
    private long movementDuration = 3000;

    EventHandler eventHandler;

    BetterThanBrandonsTimer timer;
    Random random = new Random();


    public MovingPlatform(Body body, BetterThanBrandonsTimer timer, boolean horizontal, long movementDuration, boolean oppositeStart)
    {
        this.timer = timer;
        this.movementDuration = movementDuration;
        this.horizontal = horizontal;
        this.rightOrUp = !oppositeStart;
        this.body = body;
        this.body.setGravityScale(0);
        this.timer.startTimer(movementDuration, new BetterThanBrandonsTimer.TimerListener() {
            @Override
            public void onTimerExpired() {
                rightOrUp = !rightOrUp;
            }
        });

    }

    @Override
    public void update(float delta)
    {
        Vector2 impluseToApply = new Vector2(0,1);
        boolean sameDirection = (rightOrUp && ((body.getLinearVelocity().x > 0) || (body.getLinearVelocity().y > 0)));
        impluseToApply.x = (horizontal ? 1 : 0) * (rightOrUp ? 1 : -1) * (((body.getLinearVelocity().x >= MAX_VELOCITY) && sameDirection) ? 0 : 1) * FORCE;
        impluseToApply.x = (!horizontal ? 1 : 0) * (rightOrUp ? 1 : -1) * (((body.getLinearVelocity().y >= MAX_VELOCITY) && sameDirection) ? 0 : 1) * FORCE;
        body.applyLinearImpulse(impluseToApply, body.getLocalCenter(), false);

        Vector2 spritePos = Conversions.createSpritePosition(body.getPosition(), new Vector2(sprite.getWidth(), sprite.getHeight()));
        sprite.setPosition(spritePos.x, spritePos.y);
    }

}
