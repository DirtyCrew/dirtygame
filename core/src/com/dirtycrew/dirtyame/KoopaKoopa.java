package com.dirtycrew.dirtyame;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

/**
 * Created by Kern on 1/24/2015.
 */
public class KoopaKoopa extends Entity {

    private Body body;
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


    public KoopaKoopa(Body body, EventHandler e)
    {
        this.body = body;
        eventHandler = e;
        EventHandler.Event event = new KoopaTimedEvent();
        event.setState("Timer");
        Listener listener = new Listener();
        e.subscribe(event, listener);
        timer = new Timer(2000,eventHandler,event);

    }

    @Override
    public void update(float delta)
    {
        timer.update();

        if (body.getLinearVelocity().x > MAX_HORIZONTAL_VELOCITY * -1 && changeMovement)
        {
            DLog.debug("Left");
          //  body.applyForceToCenter(-HORIZONTAL_FORCE, 0.0f, true);
            body.applyLinearImpulse(-HORIZONTAL_FORCE,0, body.getLocalCenter().x,body.getLocalCenter().y,true);

        }
        else if (body.getLinearVelocity().x < MAX_HORIZONTAL_VELOCITY && !changeMovement)
        {
            DLog.debug("Right");
            //body.applyForceToCenter(HORIZONTAL_FORCE, 0.0f, true);
            body.applyLinearImpulse(HORIZONTAL_FORCE,0, body.getLocalCenter().x,body.getLocalCenter().y,true);
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
