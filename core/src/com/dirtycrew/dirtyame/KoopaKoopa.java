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
    private static final float MAX_HORIZONTAL_VELOCITY = 1;
    private static final float HORIZONTAL_FORCE = 10;
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
        EventHandler.Event event = new EventHandler.Event();
        event.setState("Timer");
        Listener listener = new Listener();
        e.suscribe(event,listener);
        timer = new Timer(10,eventHandler,event);

    }

    @Override
    public void update(float delta)
    {
        timer.update();;
        if (body.getLinearVelocity().x >= MAX_HORIZONTAL_VELOCITY * -1 && changeMovement)
        {
            body.applyForceToCenter(-HORIZONTAL_FORCE, 0.0f, true);

        }
        else if (body.getLinearVelocity().x < MAX_HORIZONTAL_VELOCITY && !changeMovement)
        {
            body.applyForceToCenter(HORIZONTAL_FORCE, 0.0f, true);
        }

        sprite.setPosition(body.getPosition().x - 16 * Constants.METERS_PER_PIXEL, body.getPosition().y - 16 * Constants.METERS_PER_PIXEL);

    }

    private class Listener implements EventHandler.EventListener{

        @Override
        public void onEvent(EventHandler.Event e)
        {
            changeMovement = !changeMovement;
        }
    };
}
