package com.dirtycrew.dirtyame;


import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

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
    public static final float JUMP_IMPULSE = 8;
    private static final float MAX_HORIZONTAL_VELOCITY = 12;
    private static final float AIR_HORIZONTAL_FORCE = 5;
    private static final float GROUND_HORIZONTAL_FORCE = 10;
    private static final float AIR_SLOWDOWN_MULTIPLIER = .96f;
    private static final float GROUND_SLOWDOWN_MULTIPLIER = .93f;
    private static final float GROUND_FLOATING = .1f;
    private boolean canJump = true;
    private boolean isAttacking;
    private int attackingFrame;
    private int walkingFrame;
    public Long lastAttackTime;
    public InputController inputController;
    public BetterThanBrandonsTimer timer;
    public Random random;
    int jumpAnimationX;
    int jumpAnimationY;
    public Music jumpSound;
    public Music dieSound;
    public Music shootSound;
    public Music emergingSound;

    public Player (Body newBody, InputController newInputController) {
        body = newBody;
        inputController = newInputController;
        isAttacking = false;
        facingRight = true;
        this.timer = timer;
        random = new Random();
        jumpAnimationY =  (jumpAnimate[1]);
        jumpAnimationX =  (jumpAnimate[0]);
        attackingFrame = 0;
        jumpSound = Gdx.audio.newMusic(Gdx.files.getFileHandle("SFX_Jump_07.wav", Files.FileType.Internal));
        jumpSound.setVolume(20f);
        shootSound = Gdx.audio.newMusic(Gdx.files.getFileHandle("Laser_Shoot5.wav",Files.FileType.Internal));
        shootSound.setVolume(20f);
        emergingSound = Gdx.audio.newMusic(Gdx.files.getFileHandle("Emerge1.wav",Files.FileType.Internal));
        emergingSound.setVolume(1f);
        dieSound = Gdx.audio.newMusic(Gdx.files.getFileHandle("Death 3.wav",Files.FileType.Internal));
        dieSound.setVolume(100f);
    }


    @Override
    public void onCollide(Entity e) {
        super.onCollide(e);
    }

    public void update(float deltaTime) {

        if (deltaTime == 0) return;

        boolean stop = true;
        int framesPerImage = 6;
        if (onGround()){
            isJumping = false;
        }
        if(body.getLinearVelocity().x > 1f || body.getLinearVelocity().x < -1f)
        {
            if (Math.abs(body.getLinearVelocity().x) > MAX_HORIZONTAL_VELOCITY * .9){
                framesPerImage = 1;
            }else if (Math.abs(body.getLinearVelocity().x) > MAX_HORIZONTAL_VELOCITY * .5){
            framesPerImage = 3;
            } else {
                framesPerImage = 6;
            }
            walkingFrame += 1;
            if (walkingFrame >= 9*framesPerImage)
            {
                walkingFrame = 0;
            }
            currentAnimateX = 0 + walkingFrame / framesPerImage;
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
                    walkingFrame = 0;
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
                jumpSound.play();
                //body.applyForceToCenter(0,JUMP_FORCE,false);
            }
        }
        if (inputController.isAttackPressed()){
            if (lastAttackTime == null) {
                isAttacking = true;
                attackingFrame = 1;
                shootSound.play();
            }else if (System.currentTimeMillis() - lastAttackTime > SHOT_COOLDOWN ){
                isAttacking = true;
                attackingFrame = 1;
                shootSound.play();
            }
        }

        Vector2 spritePos = Conversions.createSpritePosition(body.getPosition(), new Vector2(sprite.getWidth(), sprite.getHeight()));
        sprite.setPosition(spritePos.x, spritePos.y);

        //final int framesPerImage = 3;
        if(attackingFrame > 0){
            if (attackingFrame <= framesPerImage*1) {
                sprite.setRegion(5 * 64, 13 * 64, 64, 64);
                attackingFrame++;
            } else if (attackingFrame <= framesPerImage*2) {
                sprite.setRegion(4 * 64, 13 * 64, 64, 64);
                attackingFrame++;
            } else if (attackingFrame <= framesPerImage*3) {
                sprite.setRegion(3 * 64, 13 * 64, 64, 64);
                attackingFrame++;
            }
            if (facingRight){
                sprite.flip(true,false);
            }
            if (attackingFrame == framesPerImage*3+1){
                attackingFrame = 0;
            }
        } else if(!isJumping)
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
        } else
        {
            sprite.setRegion(jumpAnimationX * 64, jumpAnimationY * 64, 64, 64);
        }



    }
    public boolean onGround(){
        return (Math.abs(body.getLinearVelocity().y) < 0.001f);
    }

    public boolean isAttacking() {
        return isAttacking;
    }

    public void setAttacking(boolean attack) {
        this.isAttacking = attack;
    }

    public void musicCleanup(){
        jumpSound.dispose();
        dieSound.dispose();
        shootSound.dispose();
        emergingSound.dispose();
    }
}
