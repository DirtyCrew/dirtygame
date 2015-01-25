package com.dirtycrew.dirtyame;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

import java.util.Random;

/**
 * Created by Kern on 1/24/2015.
 */
public class KoopaKoopa extends Entity {

    public Body body;
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
    BetterThanBrandonsTimer timer2;
    Random random;
    private int walkingFrame;
    private int totalHitPoints = 1;



    public KoopaKoopa(Body body, BetterThanBrandonsTimer timer2, int totalHitPoints) {
        this(body, timer2);
        this.totalHitPoints = totalHitPoints;

    }
    public KoopaKoopa(Body body, BetterThanBrandonsTimer timer2)
    {
        random = new Random();
        this.body = body;
        //changeMovement = !changeMovement;

        this.timer2 = timer2;
        timer2.startRecurringRandomTimer(3000, 1000, new BetterThanBrandonsTimer.TimerListener() {
            @Override
            public void onTimerExpired() {
                changeMovement = !changeMovement;
            }
        });
        walkingFrame = 0;

    }

    public void decrementHitPoints() {
        totalHitPoints--;
    }

    public boolean isDead() {
        return totalHitPoints <= 0;
    }

    @Override
    public void update(float delta) {

//        int roll = random.nextInt(100);
//        int roll2 = random.nextInt(250);
//        if (body.getLinearVelocity().x > MAX_HORIZONTAL_VELOCITY * -1 && changeMovement) {
//
//          //  body.applyForceToCenter(-HORIZONTAL_FORCE, 0.0f, true);
//            body.applyLinearImpulse(-HORIZONTAL_FORCE,0, body.getLocalCenter().x,body.getLocalCenter().y,true);
//
//        }
//        else if (body.getLinearVelocity().x < MAX_HORIZONTAL_VELOCITY && !changeMovement)
//        {
//            //body.applyForceToCenter(HORIZONTAL_FORCE, 0.0f, true);
//            body.applyLinearImpulse(HORIZONTAL_FORCE,0, body.getLocalCenter().x,body.getLocalCenter().y,true);
//        }
//
//
//
//        if(roll == 1) {
//            if (body.getLinearVelocity().x > 0)  {
//                body.applyLinearImpulse(MAX_HORIZONTAL_VELOCITY,0, body.getLocalCenter().x,body.getLocalCenter().y,true);
//            }
//            else if (body.getLinearVelocity().x < 0)
//            {
//                body.applyLinearImpulse(-MAX_HORIZONTAL_VELOCITY,0, body.getLocalCenter().x,body.getLocalCenter().y,true);
//            }
//
//            if(random.nextInt(50) == 1) {
//                body.applyLinearImpulse(0f,8,body.getLocalCenter().x, body.getLocalCenter().y, true);
//            }
//        }
//
//        if(roll2 == 1) {
//            body.applyLinearImpulse(0f,8,body.getLocalCenter().x, body.getLocalCenter().y, true);
//        }
//
        Vector2 spritePos = Conversions.createSpritePosition(body.getPosition(), new Vector2(sprite.getWidth(), sprite.getHeight()));
        sprite.setPosition(spritePos.x, spritePos.y);
//
//        final int framesPerImage = 10;
//        int imageWidth = 70;
//        int imageHeight = 90;
//        int rowNumber = 3;
//        if (body.getLinearVelocity().x == 0) {
//            walkingFrame = 0;
//        } else if (walkingFrame == 0){
//            walkingFrame = 1;
//        }
//        if(walkingFrame > 0){
//            if (walkingFrame <= framesPerImage*1) {
//                sprite.setRegion(0 * imageWidth, rowNumber * imageHeight, imageWidth, imageHeight);
//                walkingFrame++;
//            } else if (walkingFrame <= framesPerImage*2) {
//                sprite.setRegion(1 * imageWidth, rowNumber * imageHeight, imageWidth, imageHeight);
//                walkingFrame++;
//            } else if (walkingFrame <= framesPerImage*3) {
//                sprite.setRegion(2 * imageWidth, rowNumber * imageHeight, imageWidth, imageHeight);
//                walkingFrame++;
//            } else if (walkingFrame <= framesPerImage*4) {
//                sprite.setRegion(3 * imageWidth, rowNumber * imageHeight, imageWidth, imageHeight);
//                walkingFrame++;
//            }
//            else if (walkingFrame <= framesPerImage*5) {
//                sprite.setRegion(4 * imageWidth, 1 * imageHeight, imageWidth, imageHeight);
//                walkingFrame++;
//            }
//            else if (walkingFrame <= framesPerImage*6) {
//                sprite.setRegion(5 * imageWidth, 1 * imageHeight, imageWidth, imageHeight);
//                walkingFrame++;
//            }
//            else if (walkingFrame <= framesPerImage*7) {
//                sprite.setRegion(6 * imageWidth, 1 * imageHeight, imageWidth, imageHeight);
//                walkingFrame++;
//            }
//            else if (walkingFrame <= framesPerImage*8) {
//                sprite.setRegion(7 * imageWidth, 1 * imageHeight, imageWidth, imageHeight);
//                walkingFrame++;
//            }
//            if (body.getLinearVelocity().x > 0){
//                sprite.flip(true,false);
//            }
//            if (walkingFrame == framesPerImage*8+1){
//                walkingFrame = 1;
//            }
//        }
    }

}
