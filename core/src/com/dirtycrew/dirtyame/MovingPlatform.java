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
    private static final float MAX_VELOCITY = 5;
    private static final float FORCE = .3f;
    private boolean rightOrUp = true;
    private boolean horizontal; //else vertical
    private long movementDuration = 3000;

    EventHandler eventHandler;

    BetterThanBrandonsTimer timer;
    Random random = new Random();


    public MovingPlatform(Body body, BetterThanBrandonsTimer timer, boolean horizontal, final long movementDuration, boolean oppositeStart)
    {
        this.timer = timer;
        this.movementDuration = movementDuration;
        this.horizontal = horizontal;
        this.rightOrUp = !oppositeStart;
        this.body = body;
        this.body.setGravityScale(0);
        this.timer.startRecurringTimer(movementDuration, new BetterThanBrandonsTimer.TimerListener() {
            @Override
            public void onTimerExpired() {
                rightOrUp = !rightOrUp;
            }
        });

    }

    @Override
    public void update(float delta)
    {
        Vector2 impluseToApply = new Vector2(0,0);
        boolean sameDirection = (rightOrUp && ((body.getLinearVelocity().x > 0) || (body.getLinearVelocity().y > 0))) || (!rightOrUp && ((body.getLinearVelocity().x < 0) || (body.getLinearVelocity().y < 0)));

        impluseToApply.x = (horizontal ? 1 : 0) * (rightOrUp ? 1 : -1) * (((Math.abs(body.getLinearVelocity().x) >= MAX_VELOCITY) && sameDirection) ? 0 : 1) * FORCE;
        impluseToApply.y = (!horizontal ? 1 : 0) * (rightOrUp ? 1 : -1) * (((Math.abs(body.getLinearVelocity().y) >= MAX_VELOCITY) && sameDirection) ? 0 : 1) * FORCE;

        impluseToApply.add(body.getLinearVelocity());
        body.setLinearVelocity(impluseToApply);

        Vector2 spritePos = Conversions.createSpritePosition(body.getPosition(), new Vector2(sprite.getWidth(), sprite.getHeight()));
        sprite.setPosition(spritePos.x, spritePos.y);
    }

}
