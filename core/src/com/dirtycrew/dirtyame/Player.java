package com.dirtycrew.dirtyame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;

import java.util.Random;
//import com.sun.javafx.tools.packager.Log;

/**
 * Created by z084254 on 1/23/15.
 */
public class Player extends Entity {
    public Body body;
    public Sprite sprite;
    public boolean facingRight;
    private static final int leftAnimate = 9;
    private static final int rightAnimate = 11;
    private static final int[] jumpAnimate = new int[]{5,20};
    private boolean isJumping = false;
    private int currentAnimateX = 0;
    private int currentAnimateY = 0;
    private static final float SHOT_COOLDOWN = 500f;
    private static final float JUMP_IMPULSE = 8;
    private static final float MAX_HORIZONTAL_VELOCITY = 15;
    private static final float AIR_HORIZONTAL_FORCE = 7;
    private static final float GROUND_HORIZONTAL_FORCE = 14;
    private static final float AIR_SLOWDOWN_MULTIPLIER = .96f;
    private static final float GROUND_SLOWDOWN_MULTIPLIER = .93f;
    private static final float GROUND_FLOATING = .1f;
    private boolean canJump = true;
    private boolean isAttacking;
    public Long lastAttackTime;
    public InputController inputController;
    public BetterThanBrandonsTimer timer;
    public Random random;
    int jumpAnimationX;
    int jumpAnimationY;
    BetterThanBrandonsTimer.TimerListener listener = new BetterThanBrandonsTimer.TimerListener() {
        @Override
        public void onTimerExpired() {
            DLog.debug("RANDOMIZING");
            inputController.randomizeControls();
        }
    };;

    public Player (Body newBody, InputController newInputController, BetterThanBrandonsTimer timer) {
        body = newBody;
        inputController = newInputController;
        isAttacking = false;
        facingRight = true;
        this.timer = timer;
        random = new Random();
        jumpAnimationY =  (jumpAnimate[1]);
        jumpAnimationX =  (jumpAnimate[0]);

       timer.startRecurringRandomTimer(15000, 5000, listener);
    }


    @Override
    public void onCollide(Entity e) {
        super.onCollide(e);
    }

    public void update(float deltaTime) {

        if (deltaTime == 0) return;

        boolean stop = true;

        if(body.getLinearVelocity().x > 1f || body.getLinearVelocity().x < -1f)
        {
            currentAnimateX += 1;
            if (currentAnimateX == 9)
            {
                currentAnimateX = 0;
            }
        }
        else
        {
            currentAnimateX = 0;
        }


        if (inputController.isLeftPressed()) {
            if (body.getLinearVelocity().x >= MAX_HORIZONTAL_VELOCITY * -1) {
                if (body.getLinearVelocity().x > 0){
                    if (onGround()){
                        body.setLinearVelocity(body.getLinearVelocity().x * GROUND_SLOWDOWN_MULTIPLIER, body.getLinearVelocity().y+GROUND_FLOATING);
                        isJumping =false;
                    }else{
                        body.setLinearVelocity(body.getLinearVelocity().x * AIR_SLOWDOWN_MULTIPLIER, body.getLinearVelocity().y+GROUND_FLOATING);
                    }
                }
                if (onGround()) {
                    body.applyForceToCenter(-GROUND_HORIZONTAL_FORCE, GROUND_FLOATING, true);
                    isJumping = false;
                }else{
                    body.applyForceToCenter(-AIR_HORIZONTAL_FORCE, GROUND_FLOATING, true);
                }
            }
            if (body.getLinearVelocity().x < 0){
                if(facingRight)
                {
                    currentAnimateX = 0;
                }
                facingRight = false;

            }
            stop = false;
        }

        if (inputController.isRightPressed()) {
            if (body.getLinearVelocity().x < 0){
                if (onGround()){
                    body.setLinearVelocity(body.getLinearVelocity().x * GROUND_SLOWDOWN_MULTIPLIER, body.getLinearVelocity().y+GROUND_FLOATING);
                    isJumping = false;
                }else{
                    body.setLinearVelocity(body.getLinearVelocity().x * AIR_SLOWDOWN_MULTIPLIER, body.getLinearVelocity().y+GROUND_FLOATING);
                }
            }
            if (body.getLinearVelocity().x < MAX_HORIZONTAL_VELOCITY) {
                if (onGround()) {
                    body.applyForceToCenter(GROUND_HORIZONTAL_FORCE, GROUND_FLOATING, true);
                    isJumping = false;
                }else{
                    body.applyForceToCenter(AIR_HORIZONTAL_FORCE, GROUND_FLOATING, true);
                }
            }
            stop = false;
            if (body.getLinearVelocity().x > 0){
                facingRight = true;
            }
        }
        if (stop){
            if (onGround()){
                body.setLinearVelocity(body.getLinearVelocity().x * GROUND_SLOWDOWN_MULTIPLIER, body.getLinearVelocity().y);
                isJumping = false;
            }else{
                body.setLinearVelocity(body.getLinearVelocity().x * AIR_SLOWDOWN_MULTIPLIER, body.getLinearVelocity().y);
            }

        }
        if (inputController.isJumpPressed()){
            if (onGround())
            {
                body.applyLinearImpulse(0f,JUMP_IMPULSE,body.getLocalCenter().x, body.getLocalCenter().y, true);
                isJumping = true;
                int rand = random.nextInt(3);
                if(rand == 1) {
                    jumpAnimationX =  (jumpAnimate[0]);
                    jumpAnimationY = jumpAnimate[1] - 1;
                } else if(rand == 2) {
                    jumpAnimationX =  (jumpAnimate[0]);
                    jumpAnimationY = jumpAnimate[1];
                } else {
                    jumpAnimationX = 5;
                    jumpAnimationY = 2;
                }
                //body.applyForceToCenter(0,JUMP_FORCE,false);
            }
        }
        if (inputController.isAttackPressed()){
            if (lastAttackTime == null) {
                isAttacking = true;
            }else if (System.currentTimeMillis() - lastAttackTime > SHOT_COOLDOWN ){
                isAttacking = true;
            }
        }

        Vector2 spritePos = Conversions.createSpritePosition(body.getPosition(), new Vector2(sprite.getWidth(), sprite.getHeight()));
        sprite.setPosition(spritePos.x, spritePos.y);

        if(!isJumping)
        {
            if(facingRight)
            {
                currentAnimateY = rightAnimate;
            }
            else
            {
                currentAnimateY = leftAnimate;
            }
            sprite.setRegion(currentAnimateX * 64,currentAnimateY * 64,64,64);
        }
        else
        {
            sprite.setRegion(jumpAnimationX * 64, jumpAnimationY * 64, 64, 64);
        }



    }
    public boolean onGround(){
        return (body.getLinearVelocity().y == 0);
    }

    public boolean isAttacking() {
        return isAttacking;
    }

    public void setAttacking(boolean attack) {
        this.isAttacking = attack;
    }
    
}
