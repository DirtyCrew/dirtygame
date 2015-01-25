package com.dirtycrew.dirtyame;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

import java.util.Random;

/**
 * Created by z084254 on 1/24/15.
 */
public class Bee extends Entity{
    public Body body;
    public Sprite sprite;
    private static final float MAX_HORIZONTAL_VELOCITY = 3;
    private static final float HORIZONTAL_FORCE = 1;
    private boolean changeMovement = false;
    EventHandler eventHandler;

    BetterThanBrandonsTimer timer2;
    Random random = new Random();


    public Bee(Body body, BetterThanBrandonsTimer timer2)
    {
        this.timer2 = timer2;
        this.body = body;

        this.timer2.startRecurringRandomTimer(2000, 1000, new BetterThanBrandonsTimer.TimerListener() {
            @Override
            public void onTimerExpired() {
                changeMovement = !changeMovement;
            }
        });

    }

    @Override
    public void update(float delta)
    {
        if (body.getLinearVelocity().x > MAX_HORIZONTAL_VELOCITY * -1 && changeMovement) {

            //  body.applyForceToCenter(-HORIZONTAL_FORCE, 0.0f, true);
            body.applyLinearImpulse(-HORIZONTAL_FORCE,0, body.getLocalCenter().x,body.getLocalCenter().y,true);

        }
        else if (body.getLinearVelocity().x < MAX_HORIZONTAL_VELOCITY && !changeMovement)
        {
            //body.applyForceToCenter(HORIZONTAL_FORCE, 0.0f, true);
            body.applyLinearImpulse(HORIZONTAL_FORCE,0, body.getLocalCenter().x,body.getLocalCenter().y,true);
        }






        Vector2 spritePos = Conversions.createSpritePosition(body.getPosition(), new Vector2(sprite.getWidth(), sprite.getHeight()));
        sprite.setPosition(spritePos.x, spritePos.y);

    }

}
