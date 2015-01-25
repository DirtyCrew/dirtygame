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
    Timer timer;


    public Bee(Body body, EventHandler e)
    {
        this.body = body;
        eventHandler = e;
        BeeTimedEvent event = new BeeTimedEvent();
        event.setState("Timer");
        Listener listener = new Listener();
        e.subscribe(event, listener);

        timer = new Timer(new Random().nextInt(5000) + 1000,eventHandler,event);
        //changeMovement = !changeMovement;

    }

    @Override
    public void update(float delta)
    {
        timer.update();

        if (body.getLinearVelocity().x > MAX_HORIZONTAL_VELOCITY * -1 && changeMovement)
        {

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


    private class BeeTimedEvent extends EventHandler.Event{

    }

    private class Listener implements EventHandler.EventListener{

        @Override
        public void onEvent(EventHandler.Event e)
        {
            changeMovement = !changeMovement;
        }
    };
}
