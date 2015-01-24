package com.dirtycrew.dirtyame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
//import com.sun.javafx.tools.packager.Log;

/**
 * Created by z084254 on 1/23/15.
 */
public class Player extends Entity {
    public Body body;
    public Sprite sprite;
    private static final float JUMP_FORCE = 410;
    private static final float MAX_HORIZONTAL_VELOCITY = 20;
    private static final float HORIZONTAL_FORCE = 20;
    private boolean canJump = true;
    public InputController inputController;
    public OrthographicCamera camera;


    public Player (Body newBody, InputController newInputController, OrthographicCamera camera) {
        body = newBody;
        inputController = newInputController;
        this.camera = camera;
    }

    @Override
    public void onCollide(Entity e) {
        super.onCollide(e);
    }

    public void update(float deltaTime) {

        //if (deltaTime == 0) return;

//        // check input and apply to velocity & state
//        if ((Gdx.input.isKeyPressed(Keys.SPACE)) && (position.y == 0)) {
//            velocity.y += JUMP_VELOCITY;
//        }
        boolean stop = true;
        if (inputController.isLeftPressed()) {
            if (body.getLinearVelocity().x >= MAX_HORIZONTAL_VELOCITY * -1) {
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
            if(body.getLinearVelocity().y == 0)
            {
                body.applyForceToCenter(0,JUMP_FORCE,false);
            }
        }
        //TODO: Implement stopping with friction instead
        if (stop) {
            if (body.getLinearVelocity().x != 0) {
                Vector2 stopHorizontal = new Vector2(0, body.getLinearVelocity().y);
                body.setLinearVelocity(stopHorizontal);
            }
        }

        Vector2 spritePos = Conversions.createSpritePosition(body.getPosition(), new Vector2(sprite.getWidth(), sprite.getHeight()));
        sprite.setPosition(spritePos.x, spritePos.y);

    }
}
