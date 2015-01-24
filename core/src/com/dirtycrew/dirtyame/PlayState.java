package com.dirtycrew.dirtyame;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.Input.Keys;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sturm on 1/24/15.
 */
public class PlayState implements IGameState {

    Player player;
    List<Entity> entityList = new ArrayList<Entity>();
    List<Sprite> renderList = new ArrayList<Sprite>();
    OrthographicCamera camera;
    Map map;

    TiledMapRenderer tiledMapRenderer;
    InputController inputController;
    EventHandler eventHandler;

    @Override
    public void update(Dirty game, float delta) {
        player.update(delta);
        camera.position.set(player.body.getPosition().x, player.body.getPosition().y, camera.position.z);
        //DLog.debug("Pos: {} {} {}", camera.position.x, camera.position.y, map.getWidth());

        if (camera.position.x < Constants.VIEWPORT_WIDTH / 2){
            camera.position.set(Constants.VIEWPORT_WIDTH / 2, camera.position.y, camera.position.z);
        }
        if (camera.position.x > map.getWidth() - Constants.VIEWPORT_WIDTH / 2){
            camera.position.set(map.getWidth() - Constants.VIEWPORT_WIDTH / 2, camera.position.y, camera.position.z);
        }
        camera.update();
        for(Entity e : entityList) {
            e.update(delta);
        }
    }

    @Override
    public void render(Dirty game) {
        map.drawMap(camera);

        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();
        for(Sprite s : renderList) {
            s.draw(game.batch);
        }

        game.batch.end();

        game.debugRenderer.render(game.world, camera.combined);
    }

    KoopaKoopa createKoopaKoopa(World world, Vector2 pos) {
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

        entityList.add(koopa);
        renderList.add(koopa.sprite);

        koopaBody.setUserData(koopa);

        return koopa;
    }


    Player createPlayer(World world) {

        Vector2 playerDims = new Vector2(1.f, 1.f);
        Vector2 playerCenter = new Vector2(map.playerSpawnLocation);
        Vector2 playerPos = Conversions.createSpritePosition(playerCenter, playerDims);
        Vector2 playerBodyDims = Conversions.convertToBox2DSize(playerDims);

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
        fixtureDef.density = 0.5f;
        fixtureDef.friction = 0.0f;
        fixtureDef.restitution = .001f; // Make it bounce a little bit
        Fixture fixture = playerBody.createFixture(fixtureDef);
        playerBody.setFixedRotation(true);

        inputController = new InputController();
        inputController.inputSets.add(new InputSet(Keys.RIGHT, Keys.LEFT, Keys.UP, Keys.DOWN));
        if(Controllers.getControllers().size == 0)
        {
            inputController.inputSets.add(new InputSet(Keys.D, Keys.A, Keys.W, Keys.S));
        }
        else
            inputController.inputSets.add(new InputSet(Controllers.getControllers().first(), XBox360Pad.DPAD_RIGHT, XBox360Pad.DPAD_LEFT,
        XBox360Pad.BUTTON_A, XBox360Pad.BUTTON_RB));


        // create player
        Texture playerTexture = new Texture("badlogic.jpg");
        player = new Player(playerBody, inputController, camera);
        player.sprite = new Sprite(playerTexture);
        player.sprite.setPosition(playerPos.x, playerPos.y);
        player.sprite.setSize(playerDims.x, playerDims.y);

        // Player is special..dont add to entity list


        renderList.add(player.sprite);

        playerBody.setUserData(player);

        return player;


    }

    @Override
    public void init(final Dirty game) {
        eventHandler = new EventHandler();
        camera = new OrthographicCamera(Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT);
        camera.position.set(camera.viewportWidth / 2.f, camera.viewportHeight / 2.f, 0);
        camera.update();
        map = new Map("Lonely_Trees.tmx", game.world);

        createPlayer(game.world);

        //End Creating Enemy
        for(Vector2 pos : map.monsterSpawnLocations) {
            createKoopaKoopa(game.world, pos);
        }

        game.world.setContactListener(new ContactListener() {
            @Override
            public void beginContact(Contact contact) {
                Entity e1 = (Entity)contact.getFixtureA().getBody().getUserData();
                Entity e2 = (Entity)contact.getFixtureB().getBody().getUserData();
                if(e1 != player && e2 != player) {
                    // other things colliding
                } else {
                    Player p = e1 == player ? (Player)e1 : (Player)e2;
                    Entity e = p == e1 ? e2 : e1;

                    if(e instanceof Map.Tile) {
                        if (((Map.Tile) e).isDeath) {
                            game.gameManager.transitionToState(game.finishState);

                        }
                    } else if(e instanceof  KoopaKoopa) {
                        
                    }
                }
            }

            @Override
            public void endContact(Contact contact) {

            }

            @Override
            public void preSolve(Contact contact, Manifold oldManifold) {

            }

            @Override
            public void postSolve(Contact contact, ContactImpulse impulse) {

            }
        });



    }

    @Override
    public void shutdown() {

    }
}