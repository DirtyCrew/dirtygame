package com.dirtycrew.dirtyame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by z084254 on 1/23/15.
 */
public class Player {
    private Vector2 position = new Vector2(0,0);
    private Vector2 velocity = new Vector2(0,0);

    private static final float JUMP_VELOCITY = 50;
    private static final float MAX_HORIZONTAL_VELOCITY = 5;
    private static final float GRAVITY = 10;
    public static final int PLAYER_WIDTH = 50;
    public static final int PLAYER_HEIGHT = 50;

    public Vector2 getPosition(){
        return position;
    }
    
    public Vector2 getVelocity(){
        return velocity;
    }

    public Player (){

        velocity.x = 0;
        velocity.y = 0;

        position.x = 0;
        position.y = 0;
    }

    public void update(float deltaTime, Map map){
        if (deltaTime == 0) return;

        //Apply VERY SIMPLE gravity
        if (position.y > 0){
            velocity.y -= GRAVITY;
        }
        else {
            velocity.y = 0;
        }
        // check input and apply to velocity & state
        if ((Gdx.input.isKeyPressed(Keys.SPACE)) && (position.y == 0)) {
            velocity.y += JUMP_VELOCITY;
        }

        boolean stop = true;
        if (Gdx.input.isKeyPressed(Keys.LEFT) || Gdx.input.isKeyPressed(Keys.A)) {
            velocity.x = -MAX_HORIZONTAL_VELOCITY;
            stop = false;
        }

        if (Gdx.input.isKeyPressed(Keys.RIGHT) || Gdx.input.isKeyPressed(Keys.D)) {
            velocity.x = MAX_HORIZONTAL_VELOCITY;
            stop = false;
        }
        if (stop){
            velocity.x = 0;
        }

        position.x += velocity.x;
        if (position.x < 0){
            position.x = 0;
            velocity.x = 0;
        }
        if ((position.x + PLAYER_WIDTH) > map.getWidth()){
            position.x = map.getWidth() - PLAYER_WIDTH;
            velocity.x = 0;
        }
        position.y += velocity.y;
    }
}
