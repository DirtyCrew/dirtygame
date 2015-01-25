package com.dirtycrew.dirtyame;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

/**
 * Created by sturm on 1/24/15.
 */
public class EntityFactory {

    public static KoopaKoopa createKoopaKoopa(World world, Vector2 pos, EventHandler eventHandler, BetterThanBrandonsTimer timer, boolean jumpyKoopas) {
        //Creating Enemy
        BodyDef koopaBodyDef = new BodyDef();
        koopaBodyDef.fixedRotation = true;
        koopaBodyDef.type = BodyDef.BodyType.DynamicBody;
        koopaBodyDef.position.set(pos.x, pos.y);
        Body koopaBody = world.createBody(koopaBodyDef);
        Vector2[] koopaVect = new Vector2[4];
        koopaVect[0] = new Vector2(-.5f,.5f);
        koopaVect[1] = new Vector2(.5f,.5f);
        koopaVect[2] = new Vector2(-.5f,-1f);
        koopaVect[3] = new Vector2(.5f,-1f);
        PolygonShape koopaBox = new PolygonShape();
        koopaBox.set(koopaVect);

        FixtureDef koopafixtureDef = new FixtureDef();
        koopafixtureDef.shape = koopaBox;
        koopafixtureDef.density = 0.5f;
        koopafixtureDef.friction = 0.001f;
        koopafixtureDef.restitution = 0; // Make it bounce a little bit
        koopaBody.createFixture(koopafixtureDef);

        Texture koopaTexture = new Texture("knight_6.png");
        KoopaKoopa koopa = new KoopaKoopa(koopaBody, timer, 2, jumpyKoopas);
        koopa.sprite = new Sprite(koopaTexture);
        koopa.sprite.setPosition(pos.x, pos.y);
        koopa.sprite.setSize(2.f, 2.f);
        koopa.sprite.setRegion(0, 0, 64, 64);

        return koopa;
    }

    public static Bee createBee(World world, Vector2 pos, BetterThanBrandonsTimer timer) {
        //Creating Enemy
        BodyDef beeBodyDef = new BodyDef();
        beeBodyDef.fixedRotation = true;
        beeBodyDef.type = BodyDef.BodyType.DynamicBody;
        beeBodyDef.position.set(pos.x, pos.y);
        Body beeBody = world.createBody(beeBodyDef);
        beeBody.setGravityScale(0);
        Vector2[] beeVect = new Vector2[4];
        beeVect[0] = new Vector2(-.5f,.5f);
        beeVect[1] = new Vector2(.5f,.5f);
        beeVect[2] = new Vector2(-.5f,-1f);
        beeVect[3] = new Vector2(.5f,-1f);
        PolygonShape beeBox = new PolygonShape();
        beeBox.set(beeVect);

        FixtureDef beefixtureDef = new FixtureDef();
        beefixtureDef.shape = beeBox;
        beefixtureDef.density = 0.5f;
        beefixtureDef.friction = 0.001f;
        beefixtureDef.restitution = 0; // Make it bounce a little bit
        beeBody.createFixture(beefixtureDef);

        Texture beeTexture = new Texture("bee.png");
        Bee bee = new Bee(beeBody,timer);
        bee.sprite = new Sprite(beeTexture);
        bee.sprite.setPosition(pos.x, pos.y);
        bee.sprite.setSize(1.5f, 1.5f);
        bee.sprite.setRegion(0, 0, 80, 80);
        bee.sprite.flip(true, false);

        return bee;
    }

    public static MovingPlatform createMovingPlatform(World world, MovingPlatformData data, BetterThanBrandonsTimer timer) {
        //Creating Enemy
        BodyDef platformBodyDef = new BodyDef();
        platformBodyDef.fixedRotation = true;
        platformBodyDef.type = BodyDef.BodyType.KinematicBody;
        platformBodyDef.position.set(data.position);
        Body platformBody = world.createBody(platformBodyDef);
        platformBody.setGravityScale(0);
        Vector2[] platformVect = new Vector2[4];
        platformVect[0] = new Vector2(-1f,.5f);
        platformVect[1] = new Vector2(1f,.5f);
        platformVect[2] = new Vector2(-1f,-.5f);
        platformVect[3] = new Vector2(1f,-.5f);
        PolygonShape platformBox = new PolygonShape();
        platformBox.set(platformVect);

        FixtureDef platformfixtureDef = new FixtureDef();
        platformfixtureDef.shape = platformBox;
        platformfixtureDef.density = 0.9f;
        platformfixtureDef.friction = 0f;
        platformfixtureDef.restitution = 0; // Make it bounce a little bit
        platformBody.createFixture(platformfixtureDef);

        Texture platformTexture = new Texture("platformtiles.bmp");
        MovingPlatform platform = new MovingPlatform(platformBody,timer, data.horizontal, data.movementDuration, data.oppositeStart);
        platform.sprite = new Sprite(platformTexture);
        platform.sprite.setPosition(data.position.x, data.position.y);
        platform.sprite.setSize(2f, 1f);

        return platform;
    }
    

    public static Player createPlayer(World world, Vector2 playerSpawnLocation, BetterThanBrandonsTimer timer, int numUsers) {
        Vector2 playerDims = new Vector2(2.f, 2.f);
        Vector2 playerCenter = new Vector2(playerSpawnLocation);
        Vector2 playerPos = Conversions.createSpritePosition(playerCenter, playerDims);
        Vector2 playerBodyDims = Conversions.convertToBox2DSize(playerDims).sub(.05f, .05f);

        // First we create a body definition
        BodyDef playerBodyDef = new BodyDef();
        playerBodyDef.type = BodyDef.BodyType.DynamicBody;
        playerBodyDef.position.set(playerCenter);
        playerBodyDef.linearDamping = 0f;
        Body playerBody = world.createBody(playerBodyDef);
         Vector2[] playerVect = new Vector2[4];
        playerVect[0] = new Vector2(-.3f,.5f);
        playerVect[1] = new Vector2(.25f,.5f);
        playerVect[2] = new Vector2(-.3f,-.8f);
        playerVect[3] = new Vector2(.25f,-.8f);
        PolygonShape playerBox = new PolygonShape();
        playerBox.set(playerVect);

        FixtureDef fixtureDef = new FixtureDef();

        fixtureDef.shape = playerBox;
        fixtureDef.density = 0.625f;
        fixtureDef.friction = 0.0f;
        fixtureDef.restitution = .001f; // Make it bounce a little bit
        Fixture fixture = playerBody.createFixture(fixtureDef);
        playerBody.setFixedRotation(true);

        final InputController inputController = new InputController();
        if(Controllers.getControllers().size == 0)
        {
            inputController.inputSets.add(new InputSet(Input.Keys.D, Input.Keys.A, Input.Keys.W, Input.Keys.S));
        }
        else {
            inputController.inputSets.add(new InputSet(Controllers.getControllers().first(), XBox360Pad.DPAD_RIGHT, XBox360Pad.DPAD_LEFT,
                    XBox360Pad.BUTTON_A, XBox360Pad.BUTTON_B));
        }
        if(Controllers.getControllers().size <= 1) {
            inputController.inputSets.add(new InputSet(Input.Keys.RIGHT, Input.Keys.LEFT, Input.Keys.UP, Input.Keys.DOWN));
        } else {
            inputController.inputSets.add(new InputSet(Controllers.getControllers().get(1) , XBox360Pad.DPAD_RIGHT, XBox360Pad.DPAD_LEFT,
                    XBox360Pad.BUTTON_A, XBox360Pad.BUTTON_B));
        }
        if(numUsers > 1) {
            inputController.randomizeControls();
            timer.startRecurringRandomTimer(15000, 5000, new BetterThanBrandonsTimer.TimerListener() {
                @Override
                public void onTimerExpired() {
                    inputController.randomizeControls();
                }
            });
        } else {
            inputController.inputSets.get(1).setAllInactive();
        }


        // create player
        Texture playerTexture = new Texture("player.png");
        Player player = new Player(playerBody, inputController);
        player.sprite = new Sprite(playerTexture);
        player.sprite.setPosition(playerPos.x, playerPos.y);
        player.sprite.setSize(playerDims.x, playerDims.y);
        player.sprite.setRegion(0,0,64,64);

        return player;
    }
}
