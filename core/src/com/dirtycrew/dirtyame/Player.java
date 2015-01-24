package com.dirtycrew.dirtyame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.sun.javafx.tools.packager.Log;

/**
 * Created by z084254 on 1/23/15.
 */
public class Player extends Entity {
    private Body body;
    public Sprite sprite;
    private static final float JUMP_VELOCITY = 1000;
    private static final float MAX_HORIZONTAL_VELOCITY = 100;
    private static final float HORIZONTAL_FORCE = 1000;
    private static final float GRAVITY = 10;
    public static final int PLAYER_WIDTH = 50;
    public static final int PLAYER_HEIGHT = 50;
    public InputController inputController;

    public Vector2 getPosition(){
        return body.getPosition();
    }


    public Player (Body newBody, InputController newInputController) {
        body = newBody;
        inputController = newInputController;
    }

    public void update(float deltaTime) {
        //if (deltaTime == 0) return;

//        // check input and apply to velocity & state
//        if ((Gdx.input.isKeyPressed(Keys.SPACE)) && (position.y == 0)) {
//            velocity.y += JUMP_VELOCITY;
//        }
        boolean stop = true;
        if (inputController.isLeftPressed()) {
            if (body.getLinearVelocity().x > MAX_HORIZONTAL_VELOCITY * -1) {
                body.applyForceToCenter(-HORIZONTAL_FORCE, 0.0f, true);
            }
            stop = false;
        }

        if (inputController.isRightPressed()) {
            if (body.getLinearVelocity().x < MAX_HORIZONTAL_VELOCITY) {
                body.applyForceToCenter(HORIZONTAL_FORCE, 0.0f, true);
            }
            stop = false;
        }
        if (inputController.isJumpPressed()){
            body.applyForceToCenter(0, JUMP_VELOCITY, true);
        }
        if (stop) {
            if (body.getLinearVelocity().x > 0) {
                body.applyForceToCenter(-HORIZONTAL_FORCE, 0.0f, true);
            } else if (body.getLinearVelocity().x < 0) {
                body.applyForceToCenter(HORIZONTAL_FORCE, 0.0f, true);
            }
        }
    }
}
