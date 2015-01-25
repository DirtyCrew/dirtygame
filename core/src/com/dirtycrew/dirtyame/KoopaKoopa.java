package com.dirtycrew.dirtyame;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

import java.util.Random;

/**
 * Created by Kern on 1/24/2015.
 */
public class KoopaKoopa extends Entity {

    public Body body;
    public Sprite sprite;
    private static final float MAX_JUMP_VELOCITY = 30;
    private static final float JUMP_FORCE = 1000;
    private static final float MAX_HORIZONTAL_VELOCITY = 5;
    private static final float HORIZONTAL_FORCE = 2;
    private static final float GRAVITY = 10;
    private boolean canJump = true;
    public static final int PLAYER_WIDTH = 50;
    public static final int PLAYER_HEIGHT = 50;
    private boolean changeMovement = false;
    EventHandler eventHandler;
    Timer timer;
    BetterThanBrandonsTimer timer2;
    Random random;

    public KoopaKoopa(Body body, EventHandler e, BetterThanBrandonsTimer timer2)
    {
        random = new Random();
        this.body = body;
        //eventHandler = e;
        //KoopaTimedEvent event = new KoopaTimedEvent();
        //event.setState("Timer");
        //Listener listener = new Listener();
        //e.subscribe(event, listener);

        //timer = new Timer(new Random().nextInt(10000) + 1000,eventHandler,event);
        changeMovement = !changeMovement;

        this.timer2 = timer2;
        timer2.startRecurringRandomTimer(3000, 1000, new BetterThanBrandonsTimer.TimerListener() {
            @Override
            public void onTimerExpired() {
                DLog.debug("Koopa changed direction");
                if(random.nextInt(4) == 1) {
                    changeMovement = !changeMovement;
                }
            }
        });

    }

    @Override
    public void update(float delta) {
        int roll = random.nextInt(100);
        int roll2 = random.nextInt(250);
        if (body.getLinearVelocity().x > MAX_HORIZONTAL_VELOCITY * -1 && changeMovement) {

          //  body.applyForceToCenter(-HORIZONTAL_FORCE, 0.0f, true);
            body.applyLinearImpulse(-HORIZONTAL_FORCE,0, body.getLocalCenter().x,body.getLocalCenter().y,true);

        }
        else if (body.getLinearVelocity().x < MAX_HORIZONTAL_VELOCITY && !changeMovement)
        {
            //body.applyForceToCenter(HORIZONTAL_FORCE, 0.0f, true);
            body.applyLinearImpulse(HORIZONTAL_FORCE,0, body.getLocalCenter().x,body.getLocalCenter().y,true);
        }



        if(roll == 1) {
            DLog.debug("Skadoosh");
            if (body.getLinearVelocity().x > 0)  {
                body.applyLinearImpulse(MAX_HORIZONTAL_VELOCITY,0, body.getLocalCenter().x,body.getLocalCenter().y,true);
            }
            else if (body.getLinearVelocity().x < 0)
            {
                body.applyLinearImpulse(-MAX_HORIZONTAL_VELOCITY,0, body.getLocalCenter().x,body.getLocalCenter().y,true);
            }

            if(random.nextInt(50) == 1) {
                body.applyLinearImpulse(0f,8,body.getLocalCenter().x, body.getLocalCenter().y, true);
            }
        }

        if(roll2 == 1) {
            body.applyLinearImpulse(0f,8,body.getLocalCenter().x, body.getLocalCenter().y, true);
        }

        Vector2 spritePos = Conversions.createSpritePosition(body.getPosition(), new Vector2(sprite.getWidth(), sprite.getHeight()));
        sprite.setPosition(spritePos.x, spritePos.y);

    }


    private class KoopaTimedEvent extends EventHandler.Event{

    }

    private class Listener implements EventHandler.EventListener{

        @Override
        public void onEvent(EventHandler.Event e)
        {
            changeMovement = !changeMovement;
        }
    };
}
