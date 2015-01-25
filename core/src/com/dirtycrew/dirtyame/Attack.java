package com.dirtycrew.dirtyame;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

/**
 * Created by z084254 on 1/24/15.
 */
public class Attack extends Entity {

    private Body body;
    public Sprite sprite;
    public static final float SHOT_VELOCITY = 500;
    private static final long SHOT_DURATION_MILLE = 1000;
    public boolean destroy;
    public static final int SHOT_WIDTH = 10;
    public static final int SHOT_HEIGHT = 10;

    private boolean changeMovement = false;
    EventHandler eventHandler;
    Timer timer;


    public Attack(Body body, EventHandler e)
    {
        this.body = body;
        eventHandler = e;
        EventHandler.Event event = new EventHandler.Event();
        event.setState("Timer");
        Listener listener = new Listener();
        e.subscribe(event, listener);
        timer = new Timer(SHOT_DURATION_MILLE, eventHandler,event);
        destroy = false;
    }

    @Override
    public void update(float delta)
    {
        timer.update();
        if (body.getLinearVelocity().x == 0) {
            body.applyForceToCenter(SHOT_VELOCITY, 0, true);
        }
        Vector2 spritePos = Conversions.createSpritePosition(body.getPosition(), new Vector2(sprite.getWidth(), sprite.getHeight()));
        sprite.setPosition(spritePos.x, spritePos.y);

    }

    private class Listener implements EventHandler.EventListener{

        @Override
        public void onEvent(EventHandler.Event e)
        {
            destroy = true;
        }
    };

    public Body getBody() {
        return body;
    }

    public void setBody(Body body) {
        this.body = body;
    }
}
