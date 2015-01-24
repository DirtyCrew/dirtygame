package com.dirtycrew.dirtyame;

import com.badlogic.gdx.Input;
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

/**
 * Created by sturm on 1/24/15.
 */
public class PlayState implements IGameState {

    Player player;
    OrthographicCamera camera;
    Map map;

    TiledMapRenderer tiledMapRenderer;
    InputController inputController;

    @Override
    public void update(Dirty game, float delta) {
        player.update(delta);
        camera.update();

    }

    @Override
    public void render(Dirty game) {

        map.drawMap(camera);

        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();
        player.sprite.draw(game.batch);
        game.batch.end();

        game.debugRenderer.render(game.world, camera.combined);
    }



    @Override
    public void init(Dirty game) {
        // First we create a body definition
        BodyDef playerBodyDef = new BodyDef();
        playerBodyDef.type = BodyDef.BodyType.DynamicBody;
        playerBodyDef.position.set(Constants.VIEWPORT_WIDTH / 2.f, Constants.VIEWPORT_HEIGHT / 2.f);
        Body playerBody = game.world.createBody(playerBodyDef);
        PolygonShape playerBox = new PolygonShape();
        playerBox.setAsBox(Conversions.convertToMeters(32) / 2.f, Conversions.convertToMeters(32) / 2.f);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = playerBox;
        fixtureDef.density = 0.5f;
        fixtureDef.friction = 0.001f;
        fixtureDef.restitution = .2f; // Make it bounce a little bit
        playerBody.createFixture(fixtureDef);


        inputController = new InputController();
        inputController.inputSets.add(new InputSet(Keys.RIGHT, Keys.LEFT, Keys.UP, Keys.DOWN));
        inputController.inputSets.add(new InputSet(Keys.D, Keys.A, Keys.W, Keys.S));

        // create player
        Texture playerTexture = new Texture("badlogic.jpg");
        player = new Player(playerBody, inputController);
        player.sprite = new Sprite(playerTexture);
        player.sprite.setPosition(40, 30);
        player.sprite.setSize(32 * Constants.METERS_PER_PIXEL, 32 * Constants.METERS_PER_PIXEL);

        camera = new OrthographicCamera(Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT);
        camera.position.set(camera.viewportWidth / 2.f, camera.viewportHeight / 2.f, 0);
        camera.update();

        map = new Map("example_map.tmx", game.world);
    }

    @Override
    public void shutdown() {

    }
}