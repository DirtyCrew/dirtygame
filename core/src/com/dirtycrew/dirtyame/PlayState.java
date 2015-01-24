package com.dirtycrew.dirtyame;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.Input.Keys;

/**
 * Created by sturm on 1/24/15.
 */
public class PlayState implements IGameState {

    Player player;
    KoopaKoopa koopa;
    OrthographicCamera camera;
    OrthographicCamera hudCamera;
    SpriteBatch hudBatch;
    Map map;

    TiledMapRenderer tiledMapRenderer;
    InputController inputController;
    EventHandler eventHandler;

    BitmapFont font;
    Timer timer;
    long timeForLevel;

    public PlayState(long time){
        timeForLevel = time;
    }

    @Override
    public void update(Dirty game, float delta) {
        player.update(delta);

        camera.position.set(player.body.getPosition(), 0.f);

        koopa.update(delta);
        camera.update();

    }

    @Override
    public void render(Dirty game) {

        map.drawMap(camera);

        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();
        player.sprite.draw(game.batch);
        koopa.sprite.draw(game.batch);
//        font.draw(game.batch, "Hello World", 0, 0);
        game.batch.end();

        game.debugRenderer.render(game.world, camera.combined);

        hudCamera = new OrthographicCamera(Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT);
        hudCamera.update();
        hudBatch.setProjectionMatrix(hudCamera.projection);
        hudBatch.begin();
        font.setScale(0.2f);
        String timerString = "Time: " + timer.timeRemainingInMilliseconds();
        font.draw(hudBatch, timerString, -(Constants.VIEWPORT_WIDTH / 2.0f), (Constants.VIEWPORT_HEIGHT / 2.0f));
        hudBatch.end();
    }


    void createPlayer(World world) {
        Vector2 playerDims = new Vector2(1.f, 1.f);
        Vector2 playerCenter = new Vector2(map.spawnLocation);
        Vector2 playerPos = Conversions.createSpritePosition(playerCenter, playerDims);
        Vector2 playerBodyDims = Conversions.convertToBox2DSize(playerDims);

        // First we create a body definition
        BodyDef playerBodyDef = new BodyDef();
        playerBodyDef.type = BodyDef.BodyType.DynamicBody;
        playerBodyDef.position.set(playerCenter);
        Body playerBody = world.createBody(playerBodyDef);
        PolygonShape playerBox = new PolygonShape();
        playerBox.setAsBox(playerBodyDims.x, playerBodyDims.y);

        FixtureDef fixtureDef = new FixtureDef();

        fixtureDef.shape = playerBox;
        fixtureDef.density = 0.5f;
        fixtureDef.friction = 0.001f;
        fixtureDef.restitution = .001f; // Make it bounce a little bit
        playerBody.createFixture(fixtureDef);
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
        player = new Player(playerBody, inputController);
        player.sprite = new Sprite(playerTexture);
        player.sprite.setPosition(playerPos.x, playerPos.y);
        player.sprite.setSize(playerDims.x, playerDims.y);

    }

    @Override
    public void init(Dirty game) {
        eventHandler = new EventHandler();

        map = new Map("Lonely_Trees.tmx", game.world);

        createPlayer(game.world);

        camera = new OrthographicCamera(Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT);
        camera.position.set(camera.viewportWidth / 2.f, camera.viewportHeight / 2.f, 0);
        camera.update();

        font = new BitmapFont();
        font.setColor(Color.RED);

        hudBatch = new SpriteBatch();

        EventHandler.Event event = new EventHandler.Event();
        event.setState("Timer");
        Listener listener = new Listener();
        eventHandler.subscribe(event, listener);
        timer = new Timer(timeForLevel, eventHandler, event);

        //Creating Enemy
        BodyDef koopaBodyDef = new BodyDef();
        koopaBodyDef.fixedRotation = true;
        koopaBodyDef.type = BodyDef.BodyType.DynamicBody;
        koopaBodyDef.position.set(Constants.VIEWPORT_WIDTH / 2.f, Constants.VIEWPORT_HEIGHT / 2.f);
        Body koopaBody = game.world.createBody(koopaBodyDef);
        PolygonShape koopaBox = new PolygonShape();
        koopaBox.setAsBox(Conversions.convertToMeters(32) / 2.f, Conversions.convertToMeters(32) / 2.f);

        FixtureDef koopafixtureDef = new FixtureDef();
        koopafixtureDef.shape = koopaBox;
        koopafixtureDef.density = 0.5f;
        koopafixtureDef.friction = 0.001f;
        koopafixtureDef.restitution = 0; // Make it bounce a little bit
        koopaBody.createFixture(koopafixtureDef);

        Texture koopaTexture = new Texture("badlogic.jpg");
        koopa = new KoopaKoopa(koopaBody,eventHandler);
        koopa.sprite = new Sprite(koopaTexture);
        koopa.sprite.setPosition(40, 30);
        koopa.sprite.setSize(1,1);

        //End Creating Enemy



    }

    @Override
    public void shutdown() {

    }

    private class Listener implements EventHandler.EventListener{

        @Override
        public void onEvent(EventHandler.Event e)
        {
            //failed the stage
            System.out.println("Failed");
        }
    };
}