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
        playerBodyDef.position.set(100,200);
        Body playerBody = game.world.createBody(playerBodyDef);
        PolygonShape playerBox = new PolygonShape();
        playerBox.setAsBox(25, 25);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = playerBox;
        fixtureDef.density = 0.0f;
        fixtureDef.friction = 0.1f;
        fixtureDef.restitution = .5f; // Make it bounce a little bit
        playerBody.createFixture(fixtureDef);

        BodyDef boxBodyDef = new BodyDef();
        playerBodyDef.type = BodyDef.BodyType.StaticBody;
        boxBodyDef.position.set(100, 190);
        Body blockBody = game.world.createBody(boxBodyDef);
        PolygonShape blockBox = new PolygonShape();
        blockBox.setAsBox(25, 25);
        blockBody.createFixture(blockBox, 0.0f);
        blockBox.dispose();
        playerBox.dispose();

        inputController = new InputController();
        inputController.inputSets.add(new InputSet(Keys.RIGHT, Keys.LEFT, Keys.UP, Keys.DOWN));
        inputController.inputSets.add(new InputSet(Keys.D, Keys.A, Keys.W, Keys.S));

        // create player
        Texture playerTexture = new Texture("badlogic.jpg");
        player = new Player(playerBody, inputController);
        player.sprite = new Sprite(playerTexture);
        player.sprite.setPosition(Conversions.fltPhysicsToSprite(playerBody.getPosition().x), Conversions.fltPhysicsToSprite(playerBody.getPosition().y));
        player.sprite.setSize(50, 50);

        float viewportWidth = 800;
        float viewportHeight = 600;
        camera = new OrthographicCamera(viewportWidth, viewportHeight);
        camera.position.set(camera.viewportWidth / 2.f, camera.viewportHeight / 2.f, 0);
        camera.update();

        map = new Map("example_map.tmx", game.world);
    }

    @Override
    public void shutdown() {

    }
}