package com.dirtycrew.dirtyame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

/**
 * Created by z084254 on 1/23/15.
 */
public class Player {
    private static Body body;
    private static final float JUMP_VELOCITY = 50;
    private static final float MAX_HORIZONTAL_VELOCITY = 100;
    private static final float HORIZONTAL_FORCE = 1000;
    private static final float GRAVITY = 10;
    public static final int PLAYER_WIDTH = 50;
    public static final int PLAYER_HEIGHT = 50;

    public Vector2 getPosition(){
        return body.getPosition();
    }

    public Player (Body newBody){
        body = newBody;
    }

    public void update(float deltaTime, Map map){
        if (deltaTime == 0) return;

//        // check input and apply to velocity & state
//        if ((Gdx.input.isKeyPressed(Keys.SPACE)) && (position.y == 0)) {
//            velocity.y += JUMP_VELOCITY;
//        }

        boolean stop = true;
        if (Gdx.input.isKeyPressed(Keys.LEFT) || Gdx.input.isKeyPressed(Keys.A)) {
            if (body.getLinearVelocity().x > MAX_HORIZONTAL_VELOCITY*-1) {
                body.applyForceToCenter(-HORIZONTAL_FORCE, 0.0f, true);
            }
            stop = false;
        }

        if (Gdx.input.isKeyPressed(Keys.RIGHT) || Gdx.input.isKeyPressed(Keys.D)) {
            if (body.getLinearVelocity().x < MAX_HORIZONTAL_VELOCITY) {
                body.applyForceToCenter(HORIZONTAL_FORCE, 0.0f, true);
            }
            stop = false;
        }
        if (stop){
            if (body.getLinearVelocity().x > 0) {
                body.applyForceToCenter(-HORIZONTAL_FORCE, 0.0f, true);
            }else if (body.getLinearVelocity().x < 0){
                body.applyForceToCenter(HORIZONTAL_FORCE, 0.0f, true);
            }
        }

//        position.x += velocity.x;
//        if (position.x < 0){
//            position.x = 0;
//            velocity.x = 0;
//        }
//        if ((position.x + PLAYER_WIDTH) > map.getWidth()){
//            position.x = map.getWidth() - PLAYER_WIDTH;
//            velocity.x = 0;
//        }
//        position.y += velocity.y;
    }
}
