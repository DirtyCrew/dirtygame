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

    public static KoopaKoopa createKoopaKoopa(World world, Vector2 pos, EventHandler eventHandler) {
        //Creating Enemy
        BodyDef koopaBodyDef = new BodyDef();
        koopaBodyDef.fixedRotation = true;
        koopaBodyDef.type = BodyDef.BodyType.DynamicBody;
        koopaBodyDef.position.set(Constants.VIEWPORT_WIDTH / 2.f, Constants.VIEWPORT_HEIGHT / 2.f);
        Body koopaBody = world.createBody(koopaBodyDef);
        PolygonShape koopaBox = new PolygonShape();
        koopaBox.setAsBox(Conversions.convertToMeters(32) / 2.f, Conversions.convertToMeters(32) / 2.f);

        FixtureDef koopafixtureDef = new FixtureDef();
        koopafixtureDef.shape = koopaBox;
        koopafixtureDef.density = 0.5f;
        koopafixtureDef.friction = 0.001f;
        koopafixtureDef.restitution = 0; // Make it bounce a little bit
        koopaBody.createFixture(koopafixtureDef);

        Texture koopaTexture = new Texture("badlogic.jpg");
        KoopaKoopa koopa = new KoopaKoopa(koopaBody,eventHandler);
        koopa.sprite = new Sprite(koopaTexture);
        koopa.sprite.setPosition(pos.x, pos.y);
        koopa.sprite.setSize(1,1);

        return koopa;
    }

    public static Player createPlayer(World world, Vector2 playerSpawnLocation) {
        Vector2 playerDims = new Vector2(1.f, 1.f);
        Vector2 playerCenter = new Vector2(playerSpawnLocation);
        Vector2 playerPos = Conversions.createSpritePosition(playerCenter, playerDims);
        Vector2 playerBodyDims = Conversions.convertToBox2DSize(playerDims).sub(.05f, .05f);

        // First we create a body definition
        BodyDef playerBodyDef = new BodyDef();
        playerBodyDef.type = BodyDef.BodyType.DynamicBody;
        playerBodyDef.position.set(playerCenter);
        playerBodyDef.linearDamping = 0f;
        Body playerBody = world.createBody(playerBodyDef);
        PolygonShape playerBox = new PolygonShape();
        playerBox.setAsBox(playerBodyDims.x, playerBodyDims.y);

        FixtureDef fixtureDef = new FixtureDef();

        fixtureDef.shape = playerBox;
        fixtureDef.density = 0.6f;
        fixtureDef.friction = 0.0f;
        fixtureDef.restitution = .001f; // Make it bounce a little bit
        Fixture fixture = playerBody.createFixture(fixtureDef);
        playerBody.setFixedRotation(true);

        InputController inputController = new InputController();
        inputController.inputSets.add(new InputSet(Input.Keys.RIGHT, Input.Keys.LEFT, Input.Keys.UP, Input.Keys.DOWN));
        if(Controllers.getControllers().size == 0)
        {
            inputController.inputSets.add(new InputSet(Input.Keys.D, Input.Keys.A, Input.Keys.W, Input.Keys.S));
        }
        else
            inputController.inputSets.add(new InputSet(Controllers.getControllers().first(), XBox360Pad.DPAD_RIGHT, XBox360Pad.DPAD_LEFT,
                    XBox360Pad.BUTTON_A, XBox360Pad.BUTTON_RB));


        // create player
        Texture playerTexture = new Texture("badlogic.jpg");
        Player player = new Player(playerBody, inputController);
        player.sprite = new Sprite(playerTexture);
        player.sprite.setPosition(playerPos.x, playerPos.y);
        player.sprite.setSize(playerDims.x, playerDims.y);


        return player;
    }
}
